package com.bez.recipebiometrics.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    var isDecrypted by remember { mutableStateOf(false) }

    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is UiState.Success -> {
            val recipe = (uiState as UiState.Success<List<Recipe>>).data.firstOrNull { it.id == id }

            if (recipe != null) {
                // Make the content scrollable using verticalScroll
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    // Conditionally show the decrypt button if the content is not decrypted yet
                    if (!isDecrypted) {
                        Button(onClick = {
                            BiometricHelper.authenticate(
                                context = context,
                                onSuccess = { isDecrypted = true },
                                onFailure = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            )
                        }) {
                            Text("Decrypt")
                        }
                    }

                    // Show the recipe details, passing the isDecrypted state
                    RecipeDetailsContent(recipe, isDecrypted)
                }
            } else {
                Text(text = "Recipe not found", fontSize = 24.sp)
            }
        }
        is UiState.Error -> {
            Text(
                text = (uiState as UiState.Error).message,
                color = Color.Red,
                fontSize = 24.sp,
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
