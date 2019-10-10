package com.example.tentativatest.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.CoroutineContext

//this was just a test service
class DummyService: IApiService {

    //couldn't paste the entire transactions json here because it was giving compile errors
    override suspend fun getTransactions(link: String): JSONArray {
        try {
            val url = URL(link)
            val urlConn: URLConnection = url.openConnection()

            Log.d("URL", "$url")
            val httpsConn: HttpURLConnection = urlConn as HttpURLConnection
            httpsConn.allowUserInteraction = false
            httpsConn.instanceFollowRedirects = true
            httpsConn.requestMethod = "GET"
            httpsConn.connect()

            val resCode = httpsConn.responseCode

            if(resCode == HttpsURLConnection.HTTP_OK){
                Log.d("DOWNLOAD", "am reusit sa descarc")
                val str = httpsConn.inputStream.buffered().reader().use {
                    it.readText()
                }

                val json = JSONArray(str)
                httpsConn.disconnect()
                return json
            }
        }catch (e: Exception){

            val errorMessage: String = when(e){
                is MalformedURLException -> "downloadXML: invalid url ${e.message}"
                is IOException -> "downloadXML: IO Exception reading data: ${e.message}"
                is SecurityException -> "downloadXML: security exception. Needs permision? ${e.message}"
                else -> "Unknown error: ${e.message}"
            }
            Log.e("EROARE", errorMessage)
        }

        return JSONArray("[{}]")
    }

    //returning the rates json manually for testing
    override suspend fun getRates(link: String): JSONArray {
        val json =  "[{\"from\":\"AUD\",\"to\":\"USD\",\"rate\":\"1.37\"}," +
                "{\"from\":\"USD\",\"to\":\"AUD\",\"rate\":\"0.73\"}," +
                "{\"from\":\"AUD\",\"to\":\"EUR\",\"rate\":\"1.05\"}," +
                "{\"from\":\"EUR\",\"to\":\"AUD\",\"rate\":\"0.95\"}," +
                "{\"from\":\"USD\",\"to\":\"CAD\",\"rate\":\"0.51\"}," +
                "{\"from\":\"CAD\",\"to\":\"USD\",\"rate\":\"1.96\"}]"

        return JSONArray(json)
    }
}