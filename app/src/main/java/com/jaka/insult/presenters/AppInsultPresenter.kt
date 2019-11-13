package com.jaka.insult.presenters

import com.jaka.insult.views.InsultView
import com.jaka.remote.RemoteInsultRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AppInsultPresenter(
    private val remoteInsultRepository: RemoteInsultRepository,
    private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InsultPresenter, CoroutineScope {

    private val coroutineJob = Job()
    override val coroutineContext: CoroutineContext = uiDispatcher + coroutineJob
    private lateinit var insultView: InsultView

    override fun attach(insultView: InsultView) {
        this.insultView = insultView
    }

    override fun insult() {
        launch(uiDispatcher) {
            val deferred = async(ioDispatcher) {
                remoteInsultRepository.insult()
            }
            insultView.showInsult(deferred.await().insult)
        }
    }
}