package com.felo.compose.pokedex.pokemondetails

import androidx.lifecycle.ViewModel
import com.felo.compose.pokedex.repository.PokemonRepository
import com.felo.compose.pokedex.utils.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel()
{
    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon>
    {
        return repository.getPokemonInfo(pokemonName)
    }
}