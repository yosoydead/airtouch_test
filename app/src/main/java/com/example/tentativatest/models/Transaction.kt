package com.example.tentativatest.models

//transaction object model
data class Transaction (var sku: String,
                        var amount: String,
                        var currency: String,
                        var toEuro: String = "0")