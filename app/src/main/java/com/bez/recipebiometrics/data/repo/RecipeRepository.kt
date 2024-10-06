package com.bez.recipebiometrics.data.repo

import com.bez.recipebiometrics.data.api.RecipeApiService
import com.bez.recipebiometrics.data.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val apiService: RecipeApiService
) {
    suspend fun getRecipes(): Flow<List<Recipe>> = flow {
        emit(apiService.getRecipes())
    }.flowOn(Dispatchers.IO)
}
