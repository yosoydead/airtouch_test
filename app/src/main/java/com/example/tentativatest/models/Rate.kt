package com.example.tentativatest.models

//rate object model
data class Rate(var from: String,
                var to: String,
                var rate: String) {
    override fun toString(): String {
        return "From = $from, To = $to, Rate = $rate"
    }
}