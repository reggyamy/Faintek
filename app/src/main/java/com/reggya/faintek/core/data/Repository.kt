package com.reggya.faintek.core.data

import com.reggya.faintek.core.domain.IRepository
import com.reggya.faintek.core.data.model.User
import com.reggya.faintek.core.data.remote.RemoteDataSource
import com.reggya.faintek.utils.ApiResponse
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