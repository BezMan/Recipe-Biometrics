package com.bez.recipebiometrics.domain.repo

import com.bez.recipebiometrics.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {
    suspend fun getRecipes(): Flow<List<Recipe>>
}
