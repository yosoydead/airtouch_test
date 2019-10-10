package com.example.tentativatest.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tentativatest.R
import com.example.tentativatest.viewmodel.VM
import com.example.tentativatest.adapter.DetailsAdapter
import kotlinx.android.synthetic.main.product_details_fragment.*
import kotlin.math.roundToInt

class ProductDetailsFragment: Fragment() {
    private lateinit var viewModel: VM
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailsAdapter
    private lateinit var sumTextView: TextView
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.product_details_fragment, container, false)

        viewModel = activity.let{ ViewModelProviders.of(it!!).get(VM::class.java) }
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        sumTextView = view.findViewById(R.id.sum)

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Details", "sku = ${arguments?.getString("SKU")}")

//        val lst = viewModel.transactions.value!!.filter {
//            it.sku == arguments?.get("SKU")
//        }

        val lst = viewModel.detailsList.value!!

        adapter = DetailsAdapter(lst)
        recyclerView.adapter = adapter
        viewModel.filderBySku(arguments?.get("SKU").toString())

        viewModel.detailsList.observe(this, Observer {
            adapter.updateData(it)
            num_of_items.text = "Number of items: ${it.size}"
            viewModel.calculateTotalEuro()
        })

        viewModel.totalInEuro.observe(this, Observer {
            sum.text = "Sum in Euro: $it"
        })

        viewModel.loadedTransactions.observe(this, Observer {
            if(it == true){
                loading.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })
//        sum.text = "Sum in Euro: ${lst.sumBy { it.amount.toFloat().roundToInt() }}"
    }
}