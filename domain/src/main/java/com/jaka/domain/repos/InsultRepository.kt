package com.jaka.domain.repos

import com.jaka.domain.model.InsultModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow

interface InsultRepository {

    @ExperimentalCoroutinesApi
    suspend fun insult(): Channel<InsultModel>
}