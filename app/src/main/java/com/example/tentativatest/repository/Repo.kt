package com.example.tentativatest.repository

import android.util.Log
import com.example.tentativatest.models.Rate
import com.example.tentativatest.models.Transaction
import com.example.tentativatest.services.IApiService
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class Repo(private val service: IApiService): CoroutineScope {
    private val ratesLink = "http://gnb.dev.airtouchmedia.com/rates.json"
    private val transactionLink = "http://gnb.dev.airtouchmedia.com/transactions.json"

    //provided a context in which the coroutines should run
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    //this function takes the json array object provided by the service
    suspend fun parseTransactions(): List<Transaction>{
        //an async coroutine builder returns a Deffered object which needs to be awaited
        //if you want the coroutine to actually run
        var result = async(coroutineContext){
            val array = service.getTransactions(transactionLink)

            //here, i check to see if the json array object contains 0 elements
            var res = arrayListOf<Transaction>()
            if(array.length() == 0){
                //if it does, i just return an empty list meaning there was something wrong
                //when downloading the json document
                res
            }else{
                //if everything goes well
                //i convert each json object from the json array into a transaction object
                for(i in 0 until array.length()){
                    val obj = array.getJSONObject(i)
                    val sku = obj.getString("sku")
                    val amount = obj.getString("amount")
                    val currency = obj.getString("currency")

                    val transaction = Transaction(sku, amount, currency)

                    //add the transaction object into the result lust
                    res.add(transaction)
                }
                res
            }

        }

        //by calling await() on the deferred object, i actually run the coroutine
        return result.await()
    }

    suspend fun giveUniqueAndSorted(): List<Transaction>{
        return parseTransactions().distinctBy { it.sku }.sortedBy { it.sku }
    }

    //pretty much the same as parseTransactions()
    suspend fun parseRates():List<Rate>{
        var result = async(coroutineContext) {
            var array = service.getRates(ratesLink)
            var res = arrayListOf<Rate>()
            if(array.length() == 0){
                res
            }else{
                for(i in 0 until array.length()){
                    val obj = array.getJSONObject(i)
                    val from = obj.getString("from")
                    val to = obj.getString("to")
                    val rate = obj.getString("rate")

                    val rateObj = Rate(from, to, rate)
                    res.add(rateObj)
                }
                res
            }
        }

        return result.await()
    }
}