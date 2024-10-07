package com.bez.recipebiometrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bez.recipebiometrics.data.model.Recipe
import com.bez.recipebiometrics.domain.common.UiState
import com.bez.recipebiometrics.domain.usecase.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Recipe>>> = _uiState

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            // Collect the flow from the repository
            getRecipesUseCase()
                .catch { e ->
                    // Handle error by updating UiState
                    _uiState.value = UiState.Error(e.localizedMessage ?: "An error occurred")
                }
                .collect { recipes ->
                    // Update the UiState with success and the collected data
                    _uiState.value = UiState.Success(recipes)
                }
        }
    }
}
