package com.jaka.remote

import com.jaka.domain.model.Insult
import com.jaka.domain.model.InsultModel
import com.jaka.domain.model.InsultStatus
import com.jaka.domain.repos.InsultRepository
import com.jaka.remote.data.GSONInsult
import com.jaka.remote.feature.JsonTextFeature
import com.sun.management.jmx.Trace.send
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class RemoteChannelInsultRepository(private val endPoint: String = "https://insult.mattbas.org/api/insult.json") :
    InsultRepository {

    @ExperimentalCoroutinesApi
    private val insultChannel = ConflatedBroadcastChannel<InsultModel>()

    @KtorExperimentalAPI
    private val client = HttpClient(CIO) {
        install(JsonTextFeature) {
            serializer = GsonSerializer()
        }
        expectSuccess = true
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @KtorExperimentalAPI
    override suspend fun insult(): Channel<InsultModel> {
        return Channel<InsultModel>(2).apply {
            send(InsultModel())
            send(InsultModel(InsultStatus.COMPLETED, client.get<GSONInsult>(endPoint)))
        }
    }
}