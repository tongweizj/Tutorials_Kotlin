package com.example.apparchitectureexample;

import android.app.Application
import com.example.apparchitectureexample.di.appModules
import org.koin.core.context.startKoin

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        // 启动Koin，DI 开始运行
        startKoin {
            modules(appModules)
        }
    }
}