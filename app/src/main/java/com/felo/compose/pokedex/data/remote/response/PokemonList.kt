package com.plcoding.jetpackcomposepokedex.data.remote.responses


import com.felo.compose.pokedex.data.remote.response.Result

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)