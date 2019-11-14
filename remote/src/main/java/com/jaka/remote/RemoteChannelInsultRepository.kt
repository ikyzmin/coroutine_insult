package com.jaka.remote

import com.jaka.domain.model.Insult
import com.jaka.domain.model.InsultModel
import com.jaka.domain.model.InsultStatus
import com.jaka.domain.repos.InsultRepository
import com.jaka.remote.data.GSONInsult
import com.jaka.remote.feature.JsonTextFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
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
    override fun insult(): Flow<InsultModel> {
        return flow {
            emit(InsultModel())
            emit(InsultModel(InsultStatus.COMPLETED, client.get<GSONInsult>(endPoint)))
        }
    }
}