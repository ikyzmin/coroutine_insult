package com.jaka.insult.presenters

import com.jaka.insult.views.InsultView

interface InsultPresenter {
    fun attach(insultView: InsultView)
    fun insult()
}