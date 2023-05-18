package com.reggya.faintek

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class RemoteDataSource private constructor(private val apiService: ApiService){


    @SuppressLint("LongLogTag")
    fun getListUser(): Flowable<ApiResponse<List<User>>>{
        val result = PublishSubject.create<ApiResponse<List<User>>>()

        val client = apiService.getListUser()
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    (if (response.isNotEmpty()) ApiResponse.success(response)
                    else ApiResponse.empty()) as ApiResponse<List<User>>?
                )
            },{ throwable ->
                result.onNext(ApiResponse.error(throwable.message.toString()))
                Log.e("RemoteDataSource getListUser", throwable.toString())
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this){
                instance ?: RemoteDataSource(apiService)
            }

    }
}