package com.jaka.insult

import android.app.Application
import com.jaka.domain.repos.InsultRepository
import com.jaka.insult.presenters.AppInsultPresenter
import com.jaka.insult.presenters.InsultPresenter
import com.jaka.remote.RemoteInsultRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        val insultModule = module {
            single {RemoteInsultRepository()}
            single { AppInsultPresenter(get()) as InsultPresenter }
        }
        startKoin {
            modules(insultModule)
        }
    }
}