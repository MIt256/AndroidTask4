package com.example.taskfour.vm

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskfour.model.AtmListItem
import com.example.taskfour.model.QuestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainViewModel: ViewModel() {
    val BASE_URL = "https://belarusbank.by"
    private var atmList: MutableLiveData<ArrayList<AtmListItem>> = MutableLiveData()

    fun getAtmList() = atmList

    fun getData() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestApi::class.java)

        CoroutineScope(Dispatchers.IO).launch() {
            try {
                val response = api.getQuestList().awaitResponse()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main){ atmList.value = response.body()!!}
                }
            } catch (e: Exception) {
               //TODO make message
            }
        }
    }
}