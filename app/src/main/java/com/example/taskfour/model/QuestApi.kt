package com.example.taskfour.model

import retrofit2.Call
import retrofit2.http.GET

interface QuestApi {
    @GET("/api/atm?city=Гомель")
    fun getQuestList(): Call<AtmList>

}