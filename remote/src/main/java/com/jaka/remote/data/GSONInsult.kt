package com.jaka.remote.data

import com.google.gson.annotations.SerializedName
import com.jaka.domain.model.Insult

data class GSONInsult(
    @SerializedName("insult") override val insult: String,
    @SerializedName("error") override val error: Boolean,
    @SerializedName("error_message") override val message: String,
    @SerializedName("args") val args: InsultArguments
) : Insult