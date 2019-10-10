package com.example.tentativatest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tentativatest.R
import com.example.tentativatest.models.Transaction

class DetailsAdapter(private var data: List<Transaction>): RecyclerView.Adapter<DetailsAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_details_view, parent, false)
        Log.d("D Adapter", "data: ${data.size}")
        return VH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val x = data[position]
        holder.bind(x, position+1)
    }

    fun updateData(newList: List<Transaction>){
        data = newList
        notifyDataSetChanged()
        //Log.d("RECYCLERVIEW CHANGED","$newList")
    }

    inner class VH(view: View): RecyclerView.ViewHolder(view){
        private val sku = view.findViewById<TextView>(R.id.sku)
        private val amount = view.findViewById<TextView>(R.id.amount)
        private val currency = view.findViewById<TextView>(R.id.currency)
        private val number = view.findViewById<TextView>(R.id.number)
        private val toEuro = view.findViewById<TextView>(R.id.to_euro)
        private lateinit var product: Transaction

        fun bind(product: Transaction, position: Int){
            this.product = product
            sku.text = product.sku
            amount.text = product.amount
            currency.text = product.currency
            number.text = "#${position}"
            toEuro.text = product.toEuro
        }
    }
}