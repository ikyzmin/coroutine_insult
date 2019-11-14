package com.jaka.domain.model

data class InsultModel(val status: InsultStatus = InsultStatus.IN_PROGRESS,val insult: Insult = Insult.EMPTY)