package com.example.to_dolist.network

import com.example.to_dolist.network.converter.getUnsafeOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuoteService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(getUnsafeOkHttpClient()) // Use unsafe client
        .build()

    val api: QuoteApi = retrofit.create(QuoteApi::class.java)
}