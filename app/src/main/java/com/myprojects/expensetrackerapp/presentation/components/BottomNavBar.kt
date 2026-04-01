package com.myprojects.expensetrackerapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.myprojects.expensetrackerapp.core.navigation.Screen

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavController) {

    val leftItems = listOf(
        BottomNavItem("Home", Icons.Rounded.Home, Screen.Home.route),
        BottomNavItem("Transactions", Icons.Rounded.Payments, Screen.AddExpense.route)
    )

    val rightItems = listOf(
        BottomNavItem("Analytics", Icons.Rounded.Analytics, Screen.Analytics.route),
        BottomNavItem("Settings", Icons.Rounded.Settings, Screen.Settings.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(contentAlignment = Alignment.TopCenter) {

        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {

            // Left items
            leftItems.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.label)
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1D9E75),
                        selectedTextColor = Color(0xFF1D9E75),
                        indicatorColor = Color(0xFF1D9E75).copy(alpha = 0.1f)
                    )
                )
            }

            // Center spacer for FAB
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = { Box(modifier = Modifier.size(56.dp)) }, // empty space for FAB
                enabled = false,
                label = { Text("") }
            )

            // Right items
            rightItems.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.label)
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1D9E75),
                        selectedTextColor = Color(0xFF1D9E75),
                        indicatorColor = Color(0xFF1D9E75).copy(alpha = 0.1f)
                    )
                )
            }
        }

        // FAB centered on top of the nav bar
        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddExpense.route) },
            modifier = Modifier
                .size(56.dp)
                .offset(y = (-16).dp),  // lifts FAB above the nav bar
            containerColor = Color(0xFF1D9E75),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 4.dp
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add expense",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}