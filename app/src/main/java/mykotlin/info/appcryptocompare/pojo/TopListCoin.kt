package mykotlin.info.appcryptocompare.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TopListCoin(
    @SerializedName("Data")
    @Expose
    val data:List<ArrayData>?  = null
)
