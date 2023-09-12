package com.felo.compose.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.felo.compose.pokedex.pokemonlist.PokemonListScreen
import com.felo.compose.pokedex.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                        PokemonListScreen(navController = navController)
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
                    }
                }
            }
        }
    }
}
