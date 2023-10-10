package com.felo.compose.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.felo.compose.pokedex.pokemondetails.PokemonDetailsScreen
import com.felo.compose.pokedex.pokemondetails.PokemonDetailsViewModel
import com.felo.compose.pokedex.pokemonlist.PokemonListScreen
import com.felo.compose.pokedex.pokemonlist.PokemonListViewModel
import com.felo.compose.pokedex.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val pokemonListViewModel: PokemonListViewModel by viewModels()
    private val pokemonDetailsViewModel: PokemonDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
               val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen"
                ){
                    composable(route = "pokemon_list_screen"){
                        PokemonListScreen(navController = navController, viewModel = pokemonListViewModel)
                    }

                    composable(
                        route = "pokemon_details_screen/{dominantColor}/{pokemonName}",
                        arguments = listOf(
                            navArgument("dominantColor"){ type = NavType.IntType },
                            navArgument("pokemonName"){ type = NavType.StringType }
                        )
                    ){
                        val dominantColor: Color = remember {
                            /**val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White**/
                            Color(it.arguments?.getInt("dominantColor") ?: (0xFFFFFFFF).toInt())
                        }

                        val pokemonName: String = remember{
                            it.arguments?.getString("pokemonName") ?: ""
                        }
                        PokemonDetailsScreen(
                            domaintColor = dominantColor,
                            pokemonName = pokemonName.lowercase(Locale.ROOT),
                            navController = navController,
                            viewModel = pokemonDetailsViewModel
                        )
                    }
                }
            }
        }
    }
}
