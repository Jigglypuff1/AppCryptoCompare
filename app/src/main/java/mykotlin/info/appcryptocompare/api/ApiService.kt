package mykotlin.info.appcryptocompare.api

import mykotlin.info.appcryptocompare.pojo.CoinMultipleInfo
import mykotlin.info.appcryptocompare.pojo.TopListCoin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    companion object{
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val CURRENCY = "USD"
    }

    @GET("top/totalvolfull")
    fun getTopCoinInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query("tsym") tSym: String = CURRENCY,
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 10

    ): Call<TopListCoin>

    @GET("pricemultifull")
    fun getCoinMultipleInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query("tsyms") tSyms: String = CURRENCY,
        @Query("fsyms") fSyms: String,
    ):Call<CoinMultipleInfo>

}