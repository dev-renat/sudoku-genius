package com.mussayev.sudoku

import android.app.Application
import com.facebook.FacebookSdk
import com.mussayev.sudoku.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class SudokuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@SudokuApplication)
            modules(appModule)
        }
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
    }
}