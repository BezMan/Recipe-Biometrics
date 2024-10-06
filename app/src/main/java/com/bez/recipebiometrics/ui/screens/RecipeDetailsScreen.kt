package com.bez.recipebiometrics.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bez.recipebiometrics.data.model.Recipe
import com.bez.recipebiometrics.ui.viewmodel.RecipeViewModel
import com.bez.recipebiometrics.utils.BiometricHelper

@Composable
fun RecipeDetailsScreen(
    id: String?,
    navController: NavHostController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }
    val recipe = viewModel.recipes.collectAsState().value.firstOrNull { it.id == id }

    // Trigger biometric authentication when the screen is loaded
    LaunchedEffect(Unit) {
        BiometricHelper.authenticate(
            context = context,
            onSuccess = { isAuthenticated = true },
            onFailure = { /* handle failure */ }
        )
    }

    if (isAuthenticated && recipe != null) {
        RecipeDetailsContent(recipe) {
            BiometricHelper.authenticate(
                context = context,
                onSuccess = { isAuthenticated = true },
                onFailure = { /* handle failure */ }
            )
        }
    } else {
        Text(
            text = if (isAuthenticated) "Recipe not found" else "Authenticating...",
            fontSize = 18.sp,
        )
    }
}

@Composable
fun RecipeDetailsContent(recipe: Recipe, onReAuthenticate: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = recipe.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        AsyncImage(
            model = recipe.image, contentDescription = recipe.name,
            modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Fats: ${recipe.fats}")
        Text(text = "Calories: ${recipe.calories}")
        Text(text = "Carbohydrates: ${recipe.carbos}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = recipe.description)
        Button(onClick = onReAuthenticate, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Re-authenticate to View")
        }
    }
}
