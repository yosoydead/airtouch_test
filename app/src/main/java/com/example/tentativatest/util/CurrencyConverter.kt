package com.example.tentativatest.util

import com.example.tentativatest.models.Rate
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty0

//this class will do the conversion to each currency
class CurrencyConverter(rates: List<Rate>) {
    private var graph = WeightedGraph()

    private var result: Float? = null

    init {
        for(item in rates){
            graph.addConnection(item.from, item.to, item.rate.toFloat())
        }
    }

    fun convert(sum: Float, from: String, to: String): Float?{
        if(sum == 0f){
            return 0f
        }

        var visited = mutableMapOf<String, Boolean>()
        var SUM = sum

        findValue(sum, from, to, visited)

        return result
    }

    fun findValue(sum: Float, from: String, to: String, visited: MutableMap<String, Boolean>){
        if(from == to){
            result = sum
            return
        }

        visited[from] = true
        println("visited: ${from}")

        var connectedNodes = graph.nodes(from)
        for(item in connectedNodes){
            if(visited[item] != true){
                var convertedResult = sum * graph.weight(from, item)
                println("convertedResult: $convertedResult")
                findValue(convertedResult, item, to, visited)
            }
        }
    }
}