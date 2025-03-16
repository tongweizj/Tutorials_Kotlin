package com.example.apparchitectureexample.di

import com.example.apparchitectureexample.data.PetsRepository
import com.example.apparchitectureexample.data.PetsRepositoryImpl
import com.example.apparchitectureexample.viewmodel.PetsViewModel
import org.koin.dsl.module

val appModules = module {
    // 注册 PetsRepositoryImpl 作为 PetsRepository
    single<PetsRepository> { PetsRepositoryImpl() }
    // 注册 PetsViewModel，并自动注入 PetsRepository
    single { PetsViewModel(get()) }
}