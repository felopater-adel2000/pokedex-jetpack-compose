package com.felo.compose.pokedex.di

import com.felo.compose.pokedex.data.remote.PokeApi
import com.felo.compose.pokedex.repository.PokemonRepository
import com.felo.compose.pokedex.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Provides
    @Singleton
    fun providePokemonRepository(api: PokeApi) = PokemonRepository(api)


    fun providePokeApi(): PokeApi
    {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }

}
