package mykotlin.info.appcryptocompare.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class CoinMultipleInfo(
    @SerializedName("RAW")
    @Expose
    val coinPriceInfoDataJsonObject: JsonObject? = null
)
