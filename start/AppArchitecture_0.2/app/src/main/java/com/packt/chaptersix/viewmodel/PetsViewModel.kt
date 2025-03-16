package com.packt.chaptersix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.chaptersix.data.NetworkResult
import com.packt.chaptersix.data.PetsRepository
import com.packt.chaptersix.views.PetsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetsViewModel(
    private val petsRepository: PetsRepository
): ViewModel() {
    val petsUIState = MutableStateFlow(PetsUIState())
    // 创建一个 MutableStateFlow 实例 petsUIState，初始值为 PetsUIState()。
    // MutableStateFlow 用于管理 UI 的状态，当状态发生变化时，会自动通知观察者。
    init {
        getPets()
    }

    private fun getPets() {
        petsUIState.value = PetsUIState(isLoading = true)
        viewModelScope.launch {
            when (val result = petsRepository.getPets()) {
                is NetworkResult.Success -> {
                    petsUIState.update {
                        it.copy(isLoading = false, pets = result.data)
                    }
                }
                is NetworkResult.Error -> {
                    petsUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }
}