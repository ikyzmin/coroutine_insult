package com.jaka.remote

import com.jaka.domain.model.Insult
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

class RemoteInsultRepository(private val endPoint: String = "https://insult.mattbas.org/api/insult.json") :
    InsultRepository {


    @KtorExperimentalAPI
    private val client = HttpClient(CIO) {
        install(JsonTextFeature) {
            serializer = GsonSerializer()
        }
        expectSuccess = true
    }

    @KtorExperimentalAPI
    override suspend fun insult(): Insult {
        return client.get<GSONInsult>(endPoint){
            body = MultiPartFormDataContent(formData {
                append("lang", "ru")
            })
        }
    }
}