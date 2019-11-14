package com.jaka.domain.model

interface Insult {
    val insult: String
    val error: Boolean
    val message: String

    companion object EMPTY : Insult {
        override val insult: String = ""
        override val error: Boolean = false
        override val message: String = ""
    }
}