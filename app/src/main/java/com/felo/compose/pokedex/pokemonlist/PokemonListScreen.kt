package com.felo.compose.pokedex.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.request.ImageRequest
import com.felo.compose.pokedex.R
import com.felo.compose.pokedex.data.modesl.PokedexListEntry
import com.felo.compose.pokedex.ui.theme.RobotoCondensed
import com.google.accompanist.coil.CoilImage

@Composable
fun PokemonListScreen(navController: NavController)
{
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
            )
            
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                hint = "search..."
            ){}
        }

    }
}



@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit
)
{
    var text by remember{ mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = (!it.isFocused)
                },
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
        )

        if(isHintDisplayed)
        {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                text = hint,
                color = Color.LightGray
            )
        }
    }
}


@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
)
{
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
            .clickable {
                navController.navigate("pokemon_details_screen/${dominantColor.toArgb()}/${entry.pokemonName}")
            },
    ){
        Column {
            CoilImage(
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),
                request = ImageRequest.Builder(LocalContext.current).data(entry.imageUrl)
                    .target { viewModel.calcDominantColor(it) { color -> dominantColor = color } }
                    .build(),
                contentDescription = entry.pokemonName,
                fadeIn = true
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.scale(0.5f)
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController
)
{
    Column {
        Row {
            PokedexEntry(
                modifier = Modifier.weight(1f),
                entry = entries[rowIndex],
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if(entries.size >= rowIndex * 2 + 2) {
            PokedexEntry(
                entry = entries[rowIndex * 2 + 1],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}





