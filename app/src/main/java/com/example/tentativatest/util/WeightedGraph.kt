package com.example.tentativatest.util

import java.lang.Exception

//this graph will be used to map how each currency will convert into euro
class WeightedGraph {
    private lateinit var graph: MutableMap<String, MutableMap<String, Float>>

    init {
        graph = mutableMapOf<String, MutableMap<String, Float>>()
    }

    fun addConnection(firstNode: String, secondNode: String, weight: Float){
        if(graph[firstNode] == null){
            graph[firstNode] = mutableMapOf<String, Float>()
        }
        graph[firstNode]!![secondNode] = weight
        println("${firstNode} -> ${secondNode}, weight: ${weight}")
    }

    fun nodes(node: String): Array<String>{
//        return graph[node]?.keys ?: arrayOf<String>()
        var bla = graph[node]?.keys

        return bla?.toTypedArray() ?: arrayOf<String>()
    }

    fun weight(firstNode: String, secondNode: String): Float{
        println("Getting weight from: ${firstNode} to ${secondNode}")
        var weight = graph[firstNode]!![secondNode]

        return weight ?: throw Exception()
    }
}