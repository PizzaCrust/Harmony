package org.teamlyon.harmony.util

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface MayhemService {

    data class LoginResponse(var error: String?,
                             var api_token: String?,
                             var user_slug: String?,
                             var user_username: String?)

    @FormUrlEncoded
    @POST("sign_in.json")
    fun login(@Field("email") email: String, @Field("password") password: String,
              @Field("time_zone") timezone: String): Call<LoginResponse>

    data class GQLMayhemQuery(var api_token: String, var query: String, var variables: Map<String, Any> = mapOf())

    data class DataResponse(var data: String?)

    @POST("graphql")
    fun queryService(@Body query: GQLMayhemQuery): Call<DataResponse>

}

private var service: MayhemService? = null

fun getMayhemRestService(): MayhemService {
    if (service == null) {
        service = Retrofit.Builder()
                .baseUrl("https://api.mayhem.gg/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MayhemService::class.java)
    }
    return service!!
}