package com.jaka.domain.repos

import com.jaka.domain.model.InsultModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface InsultRepository {

    @ExperimentalCoroutinesApi
    fun insult(): Flow<InsultModel>
}