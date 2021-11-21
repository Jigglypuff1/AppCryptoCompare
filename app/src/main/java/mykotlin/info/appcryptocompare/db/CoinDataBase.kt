package mykotlin.info.appcryptocompare.db


import androidx.room.Database
import androidx.room.RoomDatabase
import mykotlin.info.appcryptocompare.pojo.CoinDetailInfo


@Database(entities = [CoinDetailInfo::class], version = 1, exportSchema = false)
abstract class CoinDataBase:RoomDatabase() {
    abstract fun coinDetailInfoDao(): CoinDetailInfoDao
}