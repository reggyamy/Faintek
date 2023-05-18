package com.reggya.faintek.core.domain

import com.reggya.faintek.utils.ApiResponse
import com.reggya.faintek.core.data.model.User
import io.reactivex.rxjava3.core.Flowable


interface UseCase {
    fun getListUser(): Flowable<ApiResponse<List<User>>>
}
