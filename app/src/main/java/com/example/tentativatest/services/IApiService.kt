package com.example.tentativatest.services

import kotlinx.coroutines.CoroutineScope
import org.json.JSONArray

//an object that inherits from this interface will make the repository able to
//fetch data in different ways depending on the implementation
interface IApiService {
    suspend fun getTransactions(link: String): JSONArray
    suspend fun getRates(link: String): JSONArray

}