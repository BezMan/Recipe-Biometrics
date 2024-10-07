package com.bez.recipebiometrics.di

import android.content.Context
import com.bez.recipebiometrics.BuildConfig
import com.bez.recipebiometrics.data.api.RecipeApiService
import com.bez.recipebiometrics.data.repo.MockRecipeRepository
import com.bez.recipebiometrics.data.repo.RecipeRepositoryImpl
import com.bez.recipebiometrics.domain.repo.IRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideRecipeApiService(): RecipeApiService {
        return Retrofit.Builder()
            .baseUrl("https://hf-android-app.s3-eu-west-1.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApiService::class.java)
    }


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RealRepository

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MockRepository

    @RealRepository
    @Provides
    @Singleton
    fun provideRealRecipeRepository(
        apiService: RecipeApiService
    ): IRecipeRepository {
        return RecipeRepositoryImpl(apiService)
    }

    @MockRepository
    @Provides
    @Singleton
    fun provideMockRecipeRepository(
        @ApplicationContext context: Context
    ): IRecipeRepository {
        return MockRecipeRepository(context)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        @RealRepository realRepository: IRecipeRepository,
        @MockRepository mockRepository: IRecipeRepository
    ): IRecipeRepository {
        // Change this to toggle between real or mock data
        return if (BuildConfig.DEBUG) {
            mockRepository
        } else {
            realRepository
        }
    }
}
