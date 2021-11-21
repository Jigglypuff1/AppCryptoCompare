package mykotlin.info.appcryptocompare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_crypto.view.*
import mykotlin.info.appcryptocompare.DetailInfoActivity
import mykotlin.info.appcryptocompare.R
import mykotlin.info.appcryptocompare.pojo.CoinDetailInfo


class CoinItemsDelegateAdapter(val context:Context) : DelegateAdapter<CoinDetailInfo, CoinItemsDelegateAdapter.CoinItemsViewHolder>() {

    inner class CoinItemsViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        private val ivLogoCoin: ImageView = itemView.ivLogoCoin
        private val tvSymbols: TextView = itemView.tvSymbols
        private val tvPrice: TextView = itemView.tvPrice
        private val tvUpdate: TextView = itemView.tvUpdate

        fun bind(item: CoinDetailInfo) {

            val symbolsTemplate = context.resources.getString(R.string.symbols_template)
            val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)

            tvSymbols.text = String.format(symbolsTemplate, item.fromSymbol, item.toSymbol)
            tvPrice.text = item.price.toString()
            tvUpdate.text = String.format(lastUpdateTemplate, item.getFormattedTime())
            Picasso.get().load(item.getFullImageUrl()).into(ivLogoCoin)

            itemView.setOnClickListener {
                val intent = DetailInfoActivity.newIntent(context, item)
                startActivity(context, intent, null)
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CoinItemsViewHolder(view)
    }

    override fun bindViewHolder(model: CoinDetailInfo, viewHolder: CoinItemsViewHolder) {
        viewHolder.bind(model)
    }

    override fun checkDelegate(model: CoinDetailInfo): Boolean  = true

}



