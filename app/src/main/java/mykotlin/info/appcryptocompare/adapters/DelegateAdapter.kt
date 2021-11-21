package mykotlin.info.appcryptocompare.adapters

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


internal class DelegateAdapterItemDiffCallback<T: Any>: DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            areContentsTheSame(oldItem, newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem

}

abstract class DelegateAdapter<M, in VH : RecyclerView.ViewHolder>() {

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
    abstract fun checkDelegate(model: M): Boolean

    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
    open fun onViewAttachedToWindow(viewHolder: VH) = Unit
}

class CompositeAdapter(
        private val delegates: SparseArray<DelegateAdapter<Any, RecyclerView.ViewHolder>>
) : ListAdapter<Any, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].checkDelegate(getItem(position))) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegates[viewType].createViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            delegateAdapter.bindViewHolder(getItem(position), holder)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewAttachedToWindow(holder)
        super.onViewAttachedToWindow(holder)
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<Any, RecyclerView.ViewHolder>> = SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out Any, *>): Builder {
            delegates.put(count++, delegateAdapter as DelegateAdapter<Any, RecyclerView.ViewHolder>)
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }
}