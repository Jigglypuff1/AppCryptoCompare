package mykotlin.info.appcryptocompare.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ArrayData (
    @SerializedName("CoinInfo")
    @Expose
    val coinInfo: CoinInfo? = null
)