package com.example.apparchitectureexample.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apparchitectureexample.data.PetsRepository

//- `PetsViewModel` **依赖 `PetsRepository`**，
// 但**它自己不创建这个对象**，
// 而是让 **Koin 注入 `PetsRepository`**。
// 当 Koin 创建 `PetsViewModel` 时，它会自动找到 `PetsRepository` 并传递进去。
class PetsViewModel(
    private val petsRepository: PetsRepository
): ViewModel() {

    fun getPets() = petsRepository.getPets()
}