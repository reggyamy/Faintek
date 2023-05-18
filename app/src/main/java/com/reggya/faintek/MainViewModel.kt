package com.reggya.faintek

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel

class MainViewModel(private val useCase: UseCase): ViewModel(){

    fun getListUser() = LiveDataReactiveStreams.fromPublisher(useCase.getListUser())
}
