package per.fradd2720.valgg.retrofit2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    val valorantContentsApi: ValorantContents by lazy {
        Retrofit.Builder()
            .baseUrl("https://valorant-api.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ValorantContents::class.java)
    }
}