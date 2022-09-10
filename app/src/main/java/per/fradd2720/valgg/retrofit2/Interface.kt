package per.fradd2720.valgg.retrofit2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ValorantContents {
    @GET("v1/agents")
    fun getAgents(
        @Query("language") language: String,
    ): Call<Agent>
}