package com.felo.compose.pokedex.pokemondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.felo.compose.pokedex.utils.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon


@Composable
fun PokemonDetailsScreen(
    domaintColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailsViewModel
)
{
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue =  Resource.Loading()){
        value = viewModel.getPokemonInfo(pokemonName)
    }.value


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(domaintColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonDetailsTopSection(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter),
            navController = navController
        )

        PokemonDetailStateWrapper(
            modifier = Modifier.fillMaxSize()
                .padding(top = topPadding + pokemonImageSize / 2f, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter),
            pokemonInfo = pokemonInfo,
            loadingModifier = Modifier.size(100.dp)
                .align(Alignment.Center)
        )


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            if(pokemonInfo is Resource.Success)
            {
                pokemonInfo.data?.sprites?.let {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.frontDefault)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemonInfo.data.name,
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailsTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
)
{
    Box(
        modifier = modifier.background(Brush.verticalGradient(listOf(Color.Black, Color.Transparent))),
        contentAlignment = Alignment.TopStart
    )
    {
        Icon(
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable { navController.popBackStack() },
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
        )
    }
}


@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
)
{
    when(pokemonInfo)
    {
        is Resource.Success -> {}

        is Resource.Error -> {
            Text(
                modifier = modifier,
                text = pokemonInfo.message ?: "",
                color = Color.Red
            )
        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = loadingModifier,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}