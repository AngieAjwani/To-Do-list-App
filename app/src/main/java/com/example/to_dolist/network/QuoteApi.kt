package com.example.to_dolist.network

import retrofit2.http.GET

interface QuoteApi {
    @GET("quotes")
    suspend fun getQuotes(): List<Quote>
}
