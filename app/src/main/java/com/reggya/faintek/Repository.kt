package com.reggya.faintek

import io.reactivex.rxjava3.core.Flowable

class Repository(
    private val remoteDataSource: RemoteDataSource
): IRepository {


    companion object{
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(remoteDataSource)
            }
    }

    override fun getListUser(): Flowable<ApiResponse<List<User>>> {
        return remoteDataSource.getListUser()
    }
}