package com.felo.compose.pokedex.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.felo.compose.pokedex.data.modesl.PokedexListEntry
import com.felo.compose.pokedex.repository.PokemonRepository
import com.felo.compose.pokedex.utils.Constants.PAGE_SIZE
import com.felo.compose.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor( private val repository: PokemonRepository) : ViewModel()
{
    private val TAG = "PokemonListViewModel"
    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())

    var loadError = mutableStateOf("")

    var isLoading = mutableStateOf(false)

    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated()
    {
        viewModelScope.launch {
            Log.d(TAG, "loadPokemonPaginated: ")
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result)
            {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data?.count ?: 0

                    val pokedexEntries = result.data?.results!!.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                            else {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }

                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }

                is Resource.Error -> {
                    loadError.value = result.message ?: "Error"
                    isLoading.value = false
                }

                else -> {}
            }
        }
    }


    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit)
    {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let {colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}