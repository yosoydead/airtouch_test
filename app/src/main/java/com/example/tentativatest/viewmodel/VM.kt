package com.example.tentativatest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tentativatest.models.Rate
import com.example.tentativatest.models.Transaction
import com.example.tentativatest.repository.Repo
import com.example.tentativatest.services.RealService
import com.example.tentativatest.util.CurrencyConverter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

//i use this view model to share data between the two fragments
class VM: ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    //private val dummyService = DummyService()
    //private val repo = Repo(dummyService)
    private val service = RealService()
    private val repo = Repo(service)

    //this is a boolean which will tell the spinning progress bar to hide itself
    //a listener is in the products fragment
    private val _loadedProducts = MutableLiveData<Boolean>()
    val loadedProducts: LiveData<Boolean>
        get() = _loadedProducts

    private val _loadedTransactions = MutableLiveData<Boolean>()
    val loadedTransactions:LiveData<Boolean>
        get() = _loadedTransactions

    //this will contain the actual list of transactions provided by the repository
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>>
        get() = _transactions

    //this is a list that will provide only distinct values from the transactions list
    //each SKU will be unique
    private val _distinct = MutableLiveData<List<Transaction>>()
    val distinct: LiveData<List<Transaction>>
        get() = _distinct

    private val _rates = MutableLiveData<List<Rate>>()
    val rates: LiveData<List<Rate>>
        get() = _rates

    //this will be used by the view in case there is a need to show the error view
    private val _showErrorView = MutableLiveData<Boolean>()
    val showErrorView: LiveData<Boolean>
        get() = _showErrorView

    //this will be the list for the details fragment
    //it will be computed whenever the user clicks on an item to see all of its transactions
    private val _detailsList = MutableLiveData<List<Transaction>>()
    val detailsList: LiveData<List<Transaction>>
        get() = _detailsList

    //this will be computed in the details fragment
    private val _totalInEuro = MutableLiveData<String>()
    val totalInEuro: LiveData<String>
        get() = _totalInEuro

    //when the view model is first created, initialize some empty lists to contain data
    //and call the updateTransactions() function which will communicate with the repo
            //to get and parse the data needed
    init {
        _transactions.value = arrayListOf()
        _distinct.value = arrayListOf()
        _rates.value = arrayListOf()
        _detailsList.value = arrayListOf()
        updateTransactions()
    }

    //the invokeOnCompletion() function will execute AFTER the coroutine has finished its computations
    fun updateTransactions(){
        launch(coroutineContext) {
            _transactions.value = repo.parseTransactions()
            _distinct.value = repo.giveUniqueAndSorted()
            _rates.value = repo.parseRates()
//            Log.d("ViewModel", "transactions size: ${transactions.value}")
        }.invokeOnCompletion {
            //now, i check to see if the transactions list contains items
            //if it does, hide the spinning progress bar
            if(_transactions.value!!.size > 0){
                Log.d("ViewModel", "updateTransactions finished")
                hideLoadingForProducts()
            }else{
                //if the transactions list is empty, it means that something went wrong
                //so display the error view and hide the spinning progressbar
                Log.d("ViewModel","no data downloaded")
                hideLoadingForProducts()
                showErrorView()
            }
        }
    }

    //this method is for the details fragment
    //first, filter the transactions list to get only the wanted skus
    fun filderBySku(sku: String){
        _detailsList.value = arrayListOf()
        if(_loadedTransactions.value == true){
            _loadedTransactions.value = false
        }
        launch {
            _detailsList.value = withContext(Dispatchers.IO){
                _transactions.value!!.filter { it.sku == sku}
            }
        }.invokeOnCompletion {
            //after the filtering is done, convert each currency into euro
            val conv = CurrencyConverter(_rates.value!!)
            for(item in _detailsList.value!!){
                item.toEuro = String.format("%.2f", conv
                    .convert(item.amount.toFloat(), item.currency, "EUR"))
            }
            hideLoadingForTransactions()
        }
    }

    //this function calculates the total transactions in euro
    fun calculateTotalEuro(){
        _totalInEuro.value = ""
        launch {
            _totalInEuro.value = withContext(Dispatchers.IO){
                var result = 0
                for(item in _detailsList.value!!){
                    result += item.toEuro.toFloat().roundToInt()
                }

                result.toString()
            }
        }
    }

    //used to hide the progress spinner
    private fun hideLoadingForProducts(){
        _loadedProducts.value = true
    }

    private fun hideLoadingForTransactions(){
        _loadedTransactions.value = true
    }

    //used to show the error view
    private fun showErrorView(){
        _showErrorView.value = true
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        Log.d("ViewModel", "processing ended")
    }
}