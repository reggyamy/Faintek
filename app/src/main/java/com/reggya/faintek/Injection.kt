package com.reggya.faintek

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