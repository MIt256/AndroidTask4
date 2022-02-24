package com.example.taskfour.vm

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

class MainViewModel: ViewModel() {
    private var atmList: MutableLiveData<ArrayList<AtmListItem>> = MutableLiveData()

    init {
        getData()
    }

    fun getAtmList() = atmList

    private fun getData() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestApi::class.java)

        CoroutineScope(Dispatchers.IO).launch() {
            val response = api.getQuestList().awaitResponse()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) { atmList.value = response.body()!! }
            }
        }
    }

    companion object{
        val BASE_URL = "https://belarusbank.by"
    }
}