package com.bez.recipebiometrics.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.bez.recipebiometrics.data.model.Recipe
import com.bez.recipebiometrics.domain.common.UiState
import com.bez.recipebiometrics.ui.viewmodel.RecipeViewModel
import com.bez.recipebiometrics.utils.BiometricHelper
import com.bez.recipebiometrics.utils.EncryptionHelper

@Composable
fun RecipeDetailsScreen(
    id: String?,
    navController: NavHostController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }
    var isDecrypted by remember { mutableStateOf(false) }  // Tracks if text is decrypted

    // Collect the recipe data from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Handle biometric authentication on entering the screen
    if (!isAuthenticated) {
        BiometricHelper.authenticate(
            context = context,
            onSuccess = { isAuthenticated = true },
            onFailure = { /* handle failure (e.g. navigate back) */ }
        )
    }

    when (uiState) {
        is UiState.Loading -> {
            // Show loading while fetching the recipe
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is UiState.Success -> {
            val recipe = (uiState as UiState.Success<List<Recipe>>).data.firstOrNull { it.id == id }

            if (isAuthenticated && recipe != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Decrypt button at the top
                    Button(onClick = {
                        // Re-authenticate and decrypt the text if successful
                        BiometricHelper.authenticate(
                            context = context,
                            onSuccess = { isDecrypted = true },
                            onFailure = { /* handle failure (e.g. show error) */ }
                        )
                    }) {
                        Text("Decrypt")
                    }

                    // Show recipe details (encrypted/decrypted based on isDecrypted state)
                    RecipeDetailsContent(recipe, isDecrypted)
                }
            } else if (!isAuthenticated) {
                Text(text = "Authenticating...", fontSize = 18.sp)
            } else {
                Text(text = "Recipe not found", fontSize = 18.sp)
            }
        }

        is UiState.Error -> {
            // Show error if API call fails
            Text(
                text = (uiState as UiState.Error).message,
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun RecipeDetailsContent(recipe: Recipe, isDecrypted: Boolean) {
    val encryptedName = remember { EncryptionHelper.encrypt(recipe.name) }
    val encryptedCalories = remember { EncryptionHelper.encrypt(recipe.calories) }
    val encryptedCarbos = remember { EncryptionHelper.encrypt(recipe.carbos) }
    val encryptedFats = remember { EncryptionHelper.encrypt(recipe.fats) }
    val encryptedDescription = remember { EncryptionHelper.encrypt(recipe.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(recipe.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Decrypt or show encrypted text based on isDecrypted state
        Text(text = if (isDecrypted) recipe.name else encryptedName, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = if (isDecrypted) "Calories: ${recipe.calories}" else "Calories: $encryptedCalories")
        Text(text = if (isDecrypted) "Carbos: ${recipe.carbos}" else "Carbos: $encryptedCarbos")
        Text(text = if (isDecrypted) "Fats: ${recipe.fats}" else "Fats: $encryptedFats")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = if (isDecrypted) recipe.description else encryptedDescription)
    }
}
