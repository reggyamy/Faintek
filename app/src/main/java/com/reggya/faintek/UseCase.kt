package com.reggya.faintek

import com.reggya.faintek.ApiResponse
import io.reactivex.rxjava3.core.Flowable


interface UseCase {
    fun getListUser(): Flowable<ApiResponse<List<User>>>
}
