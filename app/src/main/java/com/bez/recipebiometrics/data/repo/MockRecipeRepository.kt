package com.bez.recipebiometrics.data.repo

import com.bez.recipebiometrics.data.model.Recipe

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import android.content.Context
import com.bez.recipebiometrics.domain.repo.IRecipeRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers

class MockRecipeRepository @Inject constructor(
    private val context: Context
) : IRecipeRepository {

    override suspend fun getRecipes(): Flow<List<Recipe>> = flow {
        val json = context.assets.open("mock_recipes.json").bufferedReader().use { it.readText() }
        val recipes = parseJsonToRecipeList(json)
        emit(recipes)
    }.flowOn(Dispatchers.IO)

    private fun parseJsonToRecipeList(json: String): List<Recipe> {
        val type = object : TypeToken<List<Recipe>>() {}.type
        return Gson().fromJson(json, type)
    }
}
