package mykotlin.info.appcryptocompare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import mykotlin.info.appcryptocompare.adapters.CoinItemsDelegateAdapter
import mykotlin.info.appcryptocompare.adapters.CompositeAdapter
import mykotlin.info.appcryptocompare.adapters.OnScrollListener
import mykotlin.info.appcryptocompare.databinding.ActivityMainBinding
import mykotlin.info.appcryptocompare.viewModel.MainViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        (applicationContext as MyApplication).appComponent.inject(this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        val coinAdapter = CompositeAdapter.Builder()
            .add(CoinItemsDelegateAdapter(this))
            .build()

        binding.rvCrypto.layoutManager = LinearLayoutManager(this)
        binding.rvCrypto.adapter = coinAdapter
        binding.rvCrypto.addOnScrollListener(OnScrollListener(LinearLayoutManager(this), coinAdapter, mainViewModel))

        binding.swipeRefresCoinList.setOnRefreshListener {
            mainViewModel.loadData()
            binding.swipeRefresCoinList.isRefreshing = false
        }

        mainViewModel.coinList.observe(this, {
            if (it != null) {
                coinAdapter.submitList(it as List<Any>?) {
                    coinAdapter.notifyDataSetChanged()
                }
            }
        })

        mainViewModel.loadData()

    }
}