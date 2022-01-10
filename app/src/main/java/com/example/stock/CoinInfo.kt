package com.example.stock

data class CoinInfo (val name : String,
                     val ticker : String,
                     val price : String,
                     val rate : String,
                     val total : String)

// coinname, ticker, gain, yield, amount, avg, total, buy
data class CoinListInfo(val coinname: String, val ticker: String, val gain: String, val yield: String,
                        val amount: String, val avg: String, val total: String, val buy: String)