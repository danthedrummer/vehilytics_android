package com.ddowney.vehilytics.services

import com.ddowney.vehilytics.models.LoginResponse
import com.ddowney.vehilytics.models.RegistrationRequest
import com.ddowney.vehilytics.models.RegistrationResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationService {

    @FormUrlEncoded
    @POST("v1/sessions")
    fun login(@Field("email") email: String,
              @Field("password") password: String): Call<LoginResponse>

    @DELETE("v1/sessions")
    fun logout(@Header("X-User-Email") email: String,
               @Header("X-User-Token") token: String): Call<Void>

    @GET("v1/sessions")
    fun validate(@Header("X-User-Email") email: String,
                 @Header("X-User-Token") token: String): Call<Void>

    @POST("/users")
    fun register(@Body registration: RegistrationRequest): Call<RegistrationResponse>
}