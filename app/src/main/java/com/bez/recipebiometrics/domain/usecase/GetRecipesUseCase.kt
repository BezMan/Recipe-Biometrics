package com.bez.recipebiometrics.domain.usecase

import com.bez.recipebiometrics.data.model.Recipe
import com.bez.recipebiometrics.domain.repo.IRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: IRecipeRepository
) {
    suspend operator fun invoke(): Flow<List<Recipe>> {
        return repository.getRecipes()
    }
}
