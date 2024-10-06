package com.bez.recipebiometrics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bez.recipebiometrics.ui.screens.RecipeDetailsScreen
import com.bez.recipebiometrics.ui.screens.RecipeListScreen

sealed class Screen(val route: String) {
    object RecipeList : Screen("recipe_list")
    object RecipeDetails : Screen("recipe_details/{id}") {
        fun createRoute(id: String) = "recipe_details/$id"
    }
}

@Composable
fun RecipeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.RecipeList.route) {
        composable(Screen.RecipeList.route) {
            RecipeListScreen(navController)
        }
        composable(
            Screen.RecipeDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            RecipeDetailsScreen(id = id, navController = navController)
        }
    }
}
