package com.packt.chaptersix.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.packt.chaptersix.data.CatsAPI
import com.packt.chaptersix.data.PetsRepository
import com.packt.chaptersix.data.PetsRepositoryImpl
import com.packt.chaptersix.viewmodel.PetsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val appModules = module {
    single<PetsRepository> { PetsRepositoryImpl(get(), get()) }
    // single<PetsRepository> 表示注册一个 PetsRepository 类型的单例。
    // { PetsRepositoryImpl(get(), get()) } 是一个 lambda 表达式，
    // 它创建 PetsRepositoryImpl 的实例。
    // get() 函数用于从 Koin 容器中获取依赖项。
    // 这里假设 PetsRepositoryImpl 的构造函数需要两个依赖项，
    // 这两个依赖项也应该在 Koin 模块中注册。
    // 这意味着PetsRepositoryImpl的构造函数需要两个参数，并且这两个参数已经被注册到koin中。
    single { Dispatchers.IO }
    //Dispatchers.IO 是 Kotlin Coroutines 中的一个调度器，用于执行 I/O 密集型任务。
    //这使得在应用程序的任何地方都可以通过 get<CoroutineDispatcher>() 获取 Dispatchers.IO。
    single { PetsViewModel(get()) }

    single {
        Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory(contentType = "application/json".toMediaType())
            )
            .baseUrl("https://cataas.com/api/")
            .build()
    }
    // network
    // Retrofit.Builder() ... .build():
    // Retrofit 构建器创建一个 Retrofit 实例。
    //.addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))：
    // 添加一个 JSON 转换工厂，用于将 JSON 数据转换为 Kotlin 对象。
    //.baseUrl("https://cataas.com/api/")：设置 Retrofit 的基础 URL。
    //.build()：构建 Retrofit 实例。

    single { get<Retrofit>().create(CatsAPI::class.java) }
    // 这行代码注册了一个 CatsAPI 接口的单例。
    //get<Retrofit>() 从 Koin 容器中获取之前注册的 Retrofit 实例。
    //.create(CatsAPI::class.java) 使用 Retrofit 创建 CatsAPI 接口的实现。
    //CatsAPI 应该是一个定义了网络请求的接口。
}