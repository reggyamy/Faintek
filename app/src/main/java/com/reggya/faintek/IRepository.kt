package com.reggya.faintek

import io.reactivex.rxjava3.core.Flowable

interface IRepository {

    fun getListUser(): Flowable<ApiResponse<List<User>>>

}
