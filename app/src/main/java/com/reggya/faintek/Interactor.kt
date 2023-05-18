package com.reggya.faintek

import io.reactivex.rxjava3.core.Flowable

class Interactor (private val iRepository: IRepository): UseCase {
    override fun getListUser(): Flowable<ApiResponse<List<User>>> {
        return iRepository.getListUser()
    }
}