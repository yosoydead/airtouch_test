package com.example.tentativatest.services

import android.util.Log
import org.json.JSONArray
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

class RealService: IApiService {

    override suspend fun getTransactions(link: String): JSONArray {
        //i try to make a connection to the internet
        try {
            //creating the url and opening a connection
            val url = URL(link)
            val urlConn: URLConnection = url.openConnection()

            //setting up the type of connection
            val httpsConn: HttpURLConnection = urlConn as HttpURLConnection
            httpsConn.allowUserInteraction = false
            httpsConn.instanceFollowRedirects = true
            httpsConn.requestMethod = "GET"
            httpsConn.connect()

            //storing the connection response code
            val resCode = httpsConn.responseCode

            //if everything goes well, i should be able to fetch the json document from the internet
            if(resCode == HttpsURLConnection.HTTP_OK){
                //fetching the entire json document
                val str = httpsConn.inputStream.buffered().reader().use {
                    it.readText()
                }

                //the json document is a JSON Array so i convert it into an object
                val json = JSONArray(str)

                //close the connection and return the json array object
                httpsConn.disconnect()
                return json
            }
        } catch (e: Exception){
            //if something goes wrong, first, i create an error message
            val errorMessage: String = when(e){
                is MalformedURLException -> "download fail: invalid url ${e.message}"
                is IOException -> "download fail: IO Exception reading data: ${e.message}"
                is SecurityException -> "download fail: security exception. Needs permision? ${e.message}"
                else -> "Unknown error: ${e.message}"
            }
            //log the error message + the error itself
            Log.e("TransactionsError", errorMessage)
        }

        //if there is an error, i just return an empty json array object
        //i check for this in the view model and display an error view on the fragment
        return JSONArray("[]")
    }

    override suspend fun getRates(link: String): JSONArray {
        //i try to make a connection to the internet
        try {
            //creating the url and opening a connection
            val url = URL(link)
            val urlConn: URLConnection = url.openConnection()

            //setting up the type of connection
            val httpsConn: HttpURLConnection = urlConn as HttpURLConnection
            httpsConn.allowUserInteraction = false
            httpsConn.instanceFollowRedirects = true
            httpsConn.requestMethod = "GET"
            httpsConn.connect()

            //storing the connection response code
            val resCode = httpsConn.responseCode

            //if everything goes well, i should be able to fetch the json document from the internet
            if(resCode == HttpsURLConnection.HTTP_OK){
                //fetching the entire json document
                val str = httpsConn.inputStream.buffered().reader().use {
                    it.readText()
                }

                //the json document is a JSON Array so i convert it into an object
                val json = JSONArray(str)

                //close the connection and return the json array object
                httpsConn.disconnect()
                return json
            }
        } catch (e: Exception){
            //if something goes wrong, first, i create an error message
            val errorMessage: String = when(e){
                is MalformedURLException -> "download fail: invalid url ${e.message}"
                is IOException -> "download fail: IO Exception reading data: ${e.message}"
                is SecurityException -> "download fail: security exception. Needs permision? ${e.message}"
                else -> "Unknown error: ${e.message}"
            }
            //log the error message + the error itself
            Log.e("RatesError", errorMessage)
        }

        //if there is an error, i just return an empty json array object
        //i check for this in the view model and display an error view on the fragment
        return JSONArray("[]")
    }
}