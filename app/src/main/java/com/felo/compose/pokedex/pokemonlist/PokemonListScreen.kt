package com.felo.compose.pokedex.pokemonlist

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.felo.compose.pokedex.R
import com.felo.compose.pokedex.data.modesl.PokedexListEntry
import com.felo.compose.pokedex.ui.theme.RobotoCondensed
import com.felo.compose.pokedex.utils.hideSoftKeyboard
import com.google.accompanist.drawablepainter.rememberDrawablePainter

private const val TAG = "PokemonListScreen"
@Composable
fun PokemonListScreen(navController: NavController, viewModel: PokemonListViewModel)
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
            ){
                viewModel.searchPokemonList(it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            PokemonList(navController = navController, viewModel)
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
                    isHintDisplayed = (!it.isFocused) || (text.isNotEmpty())
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
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel
)
{
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    val activity = LocalContext.current as ComponentActivity
    LazyColumn(
        modifier = Modifier.clickable {
            activity.hideSoftKeyboard()
        },
        contentPadding = PaddingValues(16.dp)
    ){
        val itemCount = if(pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }
        items(itemCount) {
            if(it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController, viewModel = viewModel)
        }

    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ){
        if(isLoading)
        {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        if(loadError.isNotEmpty())
        {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }
}

@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel
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
            val context = LocalContext.current
//            var pokemonImage by remember { mutableStateOf(ContextCompat.getDrawable(context, R.drawable.ic_international_pok_mon_logo)) }


            Log.d(TAG, "PokedexEntry: entry.dominantColor == null")
            val imageLoader = ImageLoader.Builder(context).build()
            val imageRequest = ImageRequest.Builder(context)
                .data(entry.imageUrl)
                .target { result ->
                    viewModel.calcDominantColor(result) {
                        dominantColor = it
                        entry.dominantColor = it
                    }
                }
                .build()

            imageLoader.enqueue(imageRequest)


            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),
                model = entry.imageUrl,
                contentDescription = null,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                },
            )

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
    navController: NavController,
    viewModel: PokemonListViewModel
)
{
    Column {
        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    viewModel = viewModel
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
)
{
    Column {
        Text(
            text = error,
            color = Color.Red,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.align(CenterHorizontally),
            onClick = onRetry
        ){
            Text(text = "Retry")
        }
    }
}





