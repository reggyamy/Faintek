package com.reggya.faintek

import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.*

interface ApiService {


    @GET("users.json")
    fun getListUser(): Flowable<List<User>>

}