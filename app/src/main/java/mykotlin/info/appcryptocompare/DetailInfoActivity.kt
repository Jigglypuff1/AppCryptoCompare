package mykotlin.info.appcryptocompare

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import mykotlin.info.appcryptocompare.databinding.ActivityDetailInfoBinding
import mykotlin.info.appcryptocompare.pojo.CoinDetailInfo
import mykotlin.info.appcryptocompare.viewModel.MainViewModel
import javax.inject.Inject


class DetailInfoActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    companion object{
        private const val DETAIL_INFO = "detail_info"

        fun newIntent(context:Context, coinDetailInfo: CoinDetailInfo): Intent {

            val map = hashMapOf<String, Any?>()
            map["price"] = coinDetailInfo.price
            map["lowDay"] = coinDetailInfo.lowDay
            map["highDay"] = coinDetailInfo.highDay
            map["lastMarket"] = coinDetailInfo.lastMarket
            map["lastUpdate"] = coinDetailInfo.getFormattedTime()
            map["fromSymbol"] = coinDetailInfo.fromSymbol
            map["toSymbol"] = coinDetailInfo.toSymbol
            map["urlImage"] = coinDetailInfo.getFullImageUrl()

            val intent = Intent(context, DetailInfoActivity::class.java)
            intent.putExtra(DETAIL_INFO, map)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        (applicationContext as MyApplication).appComponent.inject(this)

        val binding = DataBindingUtil.setContentView<ActivityDetailInfoBinding>(this, R.layout.activity_detail_info)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        if (!intent.hasExtra(DETAIL_INFO)){
            finish()
            return
        }
            val mapDetailInfo = intent.getSerializableExtra(DETAIL_INFO)

            if (mapDetailInfo != null) {
                mainViewModel.getDetailInfo(mapDetailInfo as HashMap<String, Any?>, binding)
            }

    }
}