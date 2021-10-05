package com.phantom.automath

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.phantom.automath.ui.Screen.AlgebraCreator

sealed class Screen(val route: String){
	object MainScreen: Screen("main-screen")
	object AlgebraCreator: Screen("algebra-creator")
	object AlgebraSelector: Screen("algebra-selector")

	fun withArgs(vararg args: String): String {
		return buildString{
			append(route)
			args.forEach{
				arg -> append("/$arg")
			}
		}
	}
}

@ExperimentalMaterialApi
@Composable
fun Navigation() {
	var navController = rememberNavController()
	NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
		composable(route = Screen.MainScreen.route) {
			ScaffoldMainScreen(navigation = navController)
		}
		composable(
			route = Screen.AlgebraCreator.route + "/{expression}",
			arguments = listOf(
				navArgument("expression"){
					type = NavType.StringType
					defaultValue = ""
					nullable = true
				}
			)
		){
			AlgebraCreator(input_value = it.arguments?.getString("expression"))
		}

		composable(route = Screen.AlgebraCreator.route){
			AlgebraCreator(null)
		}

	}
}