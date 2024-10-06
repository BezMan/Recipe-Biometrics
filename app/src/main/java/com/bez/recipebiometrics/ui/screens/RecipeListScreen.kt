package com.bez.recipebiometrics.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bez.recipebiometrics.data.model.Recipe
import com.bez.recipebiometrics.ui.navigation.Screen
import com.bez.recipebiometrics.ui.viewmodel.RecipeViewModel
import com.bez.recipebiometrics.utils.BiometricHelper

@Composable
fun RecipeListScreen(navController: NavHostController, viewModel: RecipeViewModel = hiltViewModel()) {
    val recipes by viewModel.recipes.collectAsState()

    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe = recipe) { context ->
                BiometricHelper.authenticate(
                    context = context,
                    onSuccess = {
                        navController.navigate(Screen.RecipeDetails.createRoute(recipe.id))
                    },
                    onFailure = {
                        // Handle failure case
                    }
                )
            }
        }
    }
}


@Composable
fun RecipeItem(recipe: Recipe, onClick: (Context) -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .clickable { onClick(context) }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = recipe.thumb,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = recipe.name, fontWeight = FontWeight.Bold)
            Text(text = "Fats: ${recipe.fats}, Calories: ${recipe.calories}, Carbs: ${recipe.carbos}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeItem() {
    RecipeItem(
        recipe = Recipe("1", "Sample Recipe", "", "10g", "200 kcal", "20g", "A delicious recipe", "")
    ) {}
}
