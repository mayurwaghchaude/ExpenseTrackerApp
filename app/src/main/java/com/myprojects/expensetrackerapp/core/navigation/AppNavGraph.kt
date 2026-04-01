package com.myprojects.expensetrackerapp.core.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.myprojects.expensetrackerapp.presentation.screens.home.HomeScreen

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object AddExpense: Screen("add_expense")
    object EditExpense: Screen("edit_expense/{expenseId}"){
        fun createRoute(expenseId: String) = "edit_expense/$expenseId"
    }
    object Analytics: Screen("analytics")
    object Settings: Screen("settings")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
){
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(Screen.Home.route){
            HomeScreen(navController)
        }

        /*composable(Screen.AddExpense.route){
            AddExpenseScreen(navController)
        }

        composable(Screen.Analytics.route){
            AnalyticsScreen(navController)
        }

        composable(Screen.Settings.route){
            SettingsScreen(navController)
        }
        composable(Screen.EditExpense.route){backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")?.toIntOrNull()
            AddExpenseScreen(navController = navController, expenseId = expenseId)
        }*/
    }
}