package pl.boarderoo.mobileapp.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofitClient(): APIService {
    val client = Retrofit.Builder()
        .baseUrl("https://boarderoo-928336702407.europe-central2.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
    return client.create(APIService::class.java)
}