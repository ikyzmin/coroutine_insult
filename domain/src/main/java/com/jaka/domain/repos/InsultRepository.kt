package com.jaka.domain.repos

import com.jaka.domain.model.Insult
import com.jaka.domain.model.InsultStatus

interface InsultRepository {

    suspend fun insult(): Insult
}