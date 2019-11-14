package com.jaka.insult.views

import com.jaka.domain.model.InsultModel

interface InsultView {
    fun showInsult(insultModel:InsultModel)
}