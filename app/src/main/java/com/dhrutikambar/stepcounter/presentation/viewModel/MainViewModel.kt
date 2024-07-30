package com.dhrutikambar.stepcounter.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val stepsCount = MutableLiveData<String>("")
    fun updateStepCount(string: String) {
        stepsCount.value = string
    }
}