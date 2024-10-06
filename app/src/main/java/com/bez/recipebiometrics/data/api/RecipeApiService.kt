package com.bez.recipebiometrics.data.api

import com.bez.recipebiometrics.data.model.Recipe
import retrofit2.http.GET

interface RecipeApiService {
    @GET("android-test/recipes.json")
    suspend fun getRecipes(): List<Recipe>
}
