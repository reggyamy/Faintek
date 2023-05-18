package com.reggya.faintek.core.ui

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.reggya.faintek.core.domain.UseCase

class MainViewModel(private val useCase: UseCase): ViewModel(){

    fun getListUser() = LiveDataReactiveStreams.fromPublisher(useCase.getListUser())
}
