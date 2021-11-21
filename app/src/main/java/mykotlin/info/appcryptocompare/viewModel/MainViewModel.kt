package mykotlin.info.appcryptocompare.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mykotlin.info.appcryptocompare.R
import mykotlin.info.appcryptocompare.api.ApiService
import mykotlin.info.appcryptocompare.databinding.ActivityDetailInfoBinding
import mykotlin.info.appcryptocompare.db.CoinDataBase
import mykotlin.info.appcryptocompare.pojo.CoinDetailInfo
import mykotlin.info.appcryptocompare.pojo.CoinMultipleInfo
import mykotlin.info.appcryptocompare.pojo.TopListCoin
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject
import javax.inject.Scope


@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
class MainViewModel @Inject constructor(
        private var apiService: ApiService,
        val db: CoinDataBase,
        val context: Context
): ViewModel()  {

    var isLoading = MutableLiveData<Boolean>()
    var coinList = MutableLiveData<MutableList<CoinDetailInfo>>(ArrayList())
    var detailInfo = MutableLiveData<HashMap<String, Any?>>(HashMap())

    fun loadData(offset: Int = 0) {

        isLoading.postValue(true)

        if (offset == 0) {
            coinList.value?.clear()
        }

        if(isOnline()) {
            executeRequestCoinList(offset)
        }else{
            loadCacheData(offset)
        }
    }

    private fun executeRequestCoinList(offset: Int){

        apiService.getTopCoinInfo(page = offset).enqueue(object : Callback<TopListCoin> {
            override fun onFailure(call: Call<TopListCoin>, t: Throwable) {
                Log.d("getTopCoinInfo", "Error ${t.message}")
                isLoading.postValue(false)
            }
            override fun onResponse(
                call: Call<TopListCoin>, response: retrofit2.Response<TopListCoin>
            ) {
                if (response.body() != null) {

                    val coins = response.body()?.data
                    var currencyString = ""

                    coins?.forEach { currencyString = currencyString + it.coinInfo?.name + "," }
                    executeRequestCoinMultipleInfo(currencyString)
                }
                isLoading.postValue(false)
            }
        })
    }

    private fun executeRequestCoinMultipleInfo(currencyString: String){

        apiService.getCoinMultipleInfo(fSyms = currencyString).enqueue(object :
            Callback<CoinMultipleInfo> {
            override fun onFailure(call: Call<CoinMultipleInfo>, t: Throwable) {
                Log.d("getCoinMultipleInfo", "Error ${t.message}")
                isLoading.postValue(false)
            }

            override fun onResponse(
                call: Call<CoinMultipleInfo>,
                response: retrofit2.Response<CoinMultipleInfo>
            ) {
                response.body()?.let {

                    val currentList: MutableList<CoinDetailInfo> = getDetailInfoFromRawData(it)
                    val oldList: MutableList<CoinDetailInfo> = ArrayList(coinList.value)

                    oldList.addAll(currentList)
                    coinList.postValue(oldList)

                    viewModelScope.launch(Dispatchers.IO) {
                        db.coinDetailInfoDao().insertCoinList(currentList)
                    }
                }
            }
        })
    }

    private fun loadCacheData(offset: Int){

        viewModelScope.launch(Dispatchers.IO) {
            coinList.postValue(db.coinDetailInfoDao().getCoinList(offset))
        }

        val text = context.resources.getString(R.string.load_cache)

        val handler = Handler(Looper.getMainLooper())
        handler.post { Toast.makeText(context, text, Toast.LENGTH_SHORT).show() }
    }

    private fun getDetailInfoFromRawData(coinMultipleInfo: CoinMultipleInfo):MutableList<CoinDetailInfo>{
        val result = ArrayList<CoinDetailInfo>()
        val jsonObject: JsonObject = coinMultipleInfo.coinPriceInfoDataJsonObject ?: return result
        val coinKeys = jsonObject.keySet()
        for (coinKey in coinKeys) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeys = currencyJson.keySet()
            for (currencyKey in currencyKeys) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinDetailInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun getDetailInfo(mapDetailInfo: HashMap<String, Any?>, binding: ActivityDetailInfoBinding){
        detailInfo.value = mapDetailInfo
        Picasso.get().load(mapDetailInfo["urlImage"].toString()).into(binding.ivLogoCoin)
    }

    private fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}