package mykotlin.info.appcryptocompare.pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import mykotlin.info.appcryptocompare.api.ApiFactory.Companion.BASE_IMAGE_URL
import mykotlin.info.appcryptocompare.utils.convertTimestampToTime

@Entity(tableName = "coin_detail_info")
data class CoinDetailInfo(
    @PrimaryKey
    @NonNull
    @SerializedName("FROMSYMBOL")
    @Expose
    val fromSymbol: String,

    @SerializedName("TOSYMBOL")
    @Expose
    val toSymbol: String? = null,

    @SerializedName("PRICE")
    @Expose
    val price: Double? = null,

    @SerializedName("LASTUPDATE")
    @Expose
    val lastUpdate: Long? = null,

    @SerializedName("HIGHDAY")
    @Expose
    val highDay: Double? = null,

    @SerializedName("LOWDAY")
    @Expose
    val lowDay: Double? = null,

    @SerializedName("LASTMARKET")
    @Expose
    val lastMarket: String? = null,

    @SerializedName("IMAGEURL")
    @Expose
    val imageUrl: String? = null
){
    fun getFormattedTime():String{
        return convertTimestampToTime(lastUpdate)
    }

    fun getFullImageUrl():String{
        return BASE_IMAGE_URL + imageUrl
    }
}
