package mykotlin.info.appcryptocompare

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import mykotlin.info.appcryptocompare.api.ApiFactory
import mykotlin.info.appcryptocompare.db.DataBaseModule
import mykotlin.info.appcryptocompare.viewModel.ActivityScope


class MyApplication: Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(this)
    }
}

@Component(modules = [ApiFactory::class, DataBaseModule::class])
@ActivityScope
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(detailInfoActivity: DetailInfoActivity)
}


