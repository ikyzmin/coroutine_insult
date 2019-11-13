package com.jaka.remote.feature

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.accept
import io.ktor.client.response.HttpResponseContainer
import io.ktor.client.response.HttpResponsePipeline
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.readRemaining

class JsonTextFeature constructor(
    val serializer: JsonSerializer,
    @KtorExperimentalAPI val acceptContentTypes: List<ContentType>
) {
    @Deprecated("Install feature properly instead of direct instantiation.", level = DeprecationLevel.ERROR)
    constructor(serializer: JsonSerializer) : this(
        serializer,
        listOf(ContentType.Application.Json)
    )

    /**
     * [JsonFeature] configuration that is used during installation
     */
    class Config {
        /**
         * Serializer that will be used for serializing requests and deserializing response bodies.
         *
         * Default value for [serializer] is [defaultSerializer].
         */
        var serializer: JsonSerializer? = null

        /**
         * List of content types that are handled by this feature.
         * It also affects `Accept` request header value.
         * Please note that wildcard content types are supported but no quality specification provided.
         */
        @KtorExperimentalAPI
        var acceptContentTypes: List<ContentType> = listOf(ContentType.Application.Json,ContentType("text", "json"))
            set(newList) {
                require(newList.isNotEmpty()) { "At least one content type should be provided to acceptContentTypes" }
                field = newList
            }
    }

    /**
     * Companion object for feature installation
     */
    companion object Feature : HttpClientFeature<Config, JsonTextFeature> {
        override val key: AttributeKey<JsonTextFeature> = AttributeKey("Json")

        override fun prepare(block: Config.() -> Unit): JsonTextFeature {
            val config = Config().apply(block)
            val serializer = config.serializer ?: defaultSerializer()
            val allowedContentTypes = config.acceptContentTypes.toList()

            return JsonTextFeature(serializer, allowedContentTypes)
        }

        override fun install(feature: JsonTextFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Transform) { payload ->
                feature.acceptContentTypes.forEach { context.accept(it) }

                val contentType = context.contentType() ?: return@intercept
                if (feature.acceptContentTypes.none { contentType.match(it) })
                    return@intercept

                context.headers.remove(HttpHeaders.ContentType)

                val serializedContent = when (payload) {
                    is EmptyContent -> feature.serializer.write(Unit, contentType)
                    else -> feature.serializer.write(payload, contentType)
                }

                proceedWith(serializedContent)
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) return@intercept

                if (feature.acceptContentTypes.none { context.response.contentType()?.match(it) == true })
                    return@intercept
                try {
                    proceedWith(HttpResponseContainer(info, feature.serializer.read(info, body.readRemaining())))
                } finally {
                    context.close()
                }
            }
        }
    }
}

/**
 * Install [JsonFeature].
 */
fun HttpClientConfig<*>.Json(block: JsonFeature.Config.() -> Unit) {
    install(JsonFeature, block)
}