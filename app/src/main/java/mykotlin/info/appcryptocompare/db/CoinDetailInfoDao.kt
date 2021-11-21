package mykotlin.info.appcryptocompare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mykotlin.info.appcryptocompare.pojo.CoinDetailInfo


@Dao
interface CoinDetailInfoDao {
    @Query("SELECT  * FROM coin_detail_info ORDER BY lastUpdate DESC LIMIT 10 OFFSET :offset")
    fun getCoinList(offset: Int): MutableList<CoinDetailInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinList(coinList:MutableList<CoinDetailInfo>)
}