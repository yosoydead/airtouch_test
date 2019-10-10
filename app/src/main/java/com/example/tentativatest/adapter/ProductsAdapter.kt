package com.example.tentativatest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tentativatest.R
import com.example.tentativatest.models.Transaction

class ProductsAdapter(private var prods: List<Transaction>,
                      val clickListener: MyListener
): RecyclerView.Adapter<ProductsAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).
            inflate(R.layout.product_item_view,parent, false)

        return VH(view, clickListener)
    }

    override fun getItemCount(): Int {
        if(prods.size > 0)
            return prods.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if(prods.isNotEmpty()){
            val x = prods[position]
            holder.bind(x)
        }
    }

    fun updateData(newList: List<Transaction>){
        prods = newList
        notifyDataSetChanged()
        //Log.d("RECYCLERVIEW CHANGED","$newList")
    }


    inner class VH(view: View, private val listener: MyListener): RecyclerView.ViewHolder(view),
    View.OnClickListener{
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.clicked(adapterPosition, itemView)
        }

        private val sku = view.findViewById<TextView>(R.id.sku)
//        private val amount = view.findViewById<TextView>(R.id.amount)
//        private val currency = view.findViewById<TextView>(R.id.currency)
        private lateinit var product: Transaction

        fun bind(product: Transaction){
            this.product = product
            sku.text = product.sku
//            amount.text = product.amount
//            currency.text = product.currency
        }
    }

    interface MyListener{
        fun clicked(position: Int, view: View)
    }
}