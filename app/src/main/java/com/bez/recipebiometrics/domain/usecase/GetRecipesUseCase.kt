package com.bez.recipebiometrics.domain.usecase

import com.bez.recipebiometrics.data.model.Recipe
import com.bez.recipebiometrics.data.repo.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(): Flow<List<Recipe>> {
        return repository.getRecipes()
    }
}
