package com.jaka.insult.presenters

import com.jaka.domain.model.InsultModel
import com.jaka.domain.model.InsultStatus
import com.jaka.insult.views.InsultView
import com.jaka.remote.RemoteChannelInsultRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class AppInsultPresenter(
    private val remoteInsultRepository: RemoteChannelInsultRepository,
    private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InsultPresenter, CoroutineScope {
    private val coroutineJob = Job()



    private val handler =
        CoroutineExceptionHandler { _, _ -> insultView.showInsult(InsultModel(InsultStatus.ERROR)) }

    override val coroutineContext: CoroutineContext =
        uiDispatcher + handler + coroutineJob

    private lateinit var insultView: InsultView

    @FlowPreview
    @ExperimentalCoroutinesApi
    private val flow = remoteInsultRepository.insult()
        .flowOn(ioDispatcher)

    override fun attach(insultView: InsultView) {
        this.insultView = insultView
    }


    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun insult() {
        launch(coroutineContext) {
            flow.collect {
                insultView.showInsult(it)
            }
        }
    }
}