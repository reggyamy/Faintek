package com.reggya.faintek.core.di

import com.reggya.faintek.core.data.Repository
import com.reggya.faintek.core.domain.IRepository
import com.reggya.faintek.core.domain.Interactor
import com.reggya.faintek.core.domain.UseCase
import com.reggya.faintek.core.data.network.ApiConfig
import com.reggya.faintek.core.data.remote.RemoteDataSource

object Injection {

    private fun provideRepository(): IRepository {
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        return Repository.getInstance(remoteDataSource)
    }

    fun provideUseCase(): UseCase {
        val repository = provideRepository()
        return Interactor(repository)
    }
}