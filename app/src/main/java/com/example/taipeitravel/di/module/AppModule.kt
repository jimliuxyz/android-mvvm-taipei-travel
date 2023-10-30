package com.example.taipeitravel.di.module

import com.example.taipeitravel.repository.ITravelRepository
import com.example.taipeitravel.repository.TravelRepository
import com.example.taipeitravel.webapi.TravelService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(FragmentComponent::class, ViewModelComponent::class)
abstract class AppModule {
    companion object {
        @Provides
        fun providesTravelService(): TravelService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.travel.taipei/open-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TravelService::class.java)
        }
    }

    @Binds
    abstract fun bindITravelRepository(
        repo: TravelRepository
    ): ITravelRepository

}
