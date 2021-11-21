package mykotlin.info.appcryptocompare.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mykotlin.info.appcryptocompare.viewModel.MainViewModel


class OnScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private var mainViewModel: MainViewModel
) : RecyclerView.OnScrollListener() {

    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = adapter.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (!recyclerView.canScrollVertically(1)) {
            mainViewModel.loadData(totalItemCount)
        }

    }

}