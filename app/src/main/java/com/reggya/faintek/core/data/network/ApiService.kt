package com.reggya.faintek.core.data.network

import com.reggya.faintek.core.data.model.User
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.*

interface ApiService {


    @GET("users.json")
    fun getListUser(): Flowable<List<User>>

}