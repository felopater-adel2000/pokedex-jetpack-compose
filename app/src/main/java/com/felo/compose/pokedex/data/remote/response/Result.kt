package com.felo.compose.pokedex.data.remote.response


import com.google.gson.annotations.SerializedName

data class Result(
    val name: String,
    val url: String
)