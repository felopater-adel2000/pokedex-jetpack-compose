package com.felo.compose.pokedex.repository

import com.felo.compose.pokedex.data.remote.PokeApi
import com.felo.compose.pokedex.utils.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.plcoding.jetpackcomposepokedex.data.remote.responses.PokemonList
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(private val api: PokeApi)
{
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList>
    {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch(e: Exception)
        {
            return Resource.Error(" Unknown Error")
        }
        return Resource.Success(data = response)
    }

    suspend fun getPokemonInfo(name: String): Resource<Pokemon>
    {
        val response = try {
            api.getPokemonInfo(name)
        } catch(e: Exception)
        {
            return Resource.Error(" Unknown Error")
        }
        return Resource.Success(data = response)
    }
}