package mykotlin.info.appcryptocompare.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides


@Module
class DataBaseModule {

    companion object{
        private var db:CoinDataBase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()
    }

    @Provides
    fun provideInstans(context: Context):CoinDataBase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    CoinDataBase::class.java,
                    DB_NAME
                ).build()
                db = instance
                return instance
            }
        }

}