package com.example.tentativatest.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tentativatest.R
import com.example.tentativatest.viewmodel.VM
import com.example.tentativatest.adapter.ProductsAdapter
import kotlinx.android.synthetic.main.products_fragment.*

class ProductsFragment: Fragment(), ProductsAdapter.MyListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductsAdapter
    private lateinit var viewModel: VM
    private lateinit var spinner: ProgressBar
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)

        viewModel = activity.let { ViewModelProviders.of(it!!).get(VM::class.java) }
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        spinner = view.findViewById(R.id.loading)
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = viewModel.distinct.value!!
        adapter = ProductsAdapter(data, this)
        recyclerView.adapter = adapter

        viewModel.distinct.observe(this, Observer {
            Log.d("Fragm", "transactions updated ${viewModel.distinct.value!!.size}")
            adapter.updateData(it)
        })

        viewModel.loadedProducts.observe(this, Observer{
            recyclerView.visibility = View.VISIBLE
            spinner.visibility = View.GONE
        })

        viewModel.showErrorView.observe(this, Observer{
            error_layout.visibility = View.VISIBLE
            spinner.visibility = View.GONE
        })
    }

    override fun clicked(position: Int, view: View) {
        val x= viewModel.distinct.value!![position]
        Log.d("ProductsFragment", "clicked on item ${x.sku}")
//        Log.d("ProductFragment", "view: ${view.amount.text}")
        val bundle = Bundle()
        bundle.putString("SKU", x.sku)

        view.let {
            findNavController().navigate(R.id.action_productsFragment_to_productDetailsFragment, bundle)
        }
    }
}