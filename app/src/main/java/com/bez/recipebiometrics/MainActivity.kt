package com.bez.recipebiometrics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.bez.recipebiometrics.ui.navigation.RecipeApp
import com.bez.recipebiometrics.ui.theme.RecipeBiometricsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeBiometricsTheme {
                RecipeApp()
            }
        }
    }
}
