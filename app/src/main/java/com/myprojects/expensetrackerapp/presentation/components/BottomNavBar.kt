package com.myprojects.expensetrackerapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.myprojects.expensetrackerapp.R
import com.myprojects.expensetrackerapp.core.navigation.Screen
import com.myprojects.expensetrackerapp.ui.theme.NavBarColor

data class BottomNavItem(
    val labelRes: Int,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavController) {

    val leftItems = listOf(
        BottomNavItem(R.string.nav_home, Icons.Rounded.Home, Screen.Home.route),
        BottomNavItem(R.string.nav_transactions, Icons.Rounded.ListAlt, Screen.Transactions.route)
    )

    val rightItems = listOf(
        BottomNavItem(R.string.nav_analytics, Icons.Rounded.Analytics, Screen.Analytics.route),
        BottomNavItem(R.string.nav_settings, Icons.Rounded.Settings, Screen.Settings.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(contentAlignment = Alignment.TopCenter) {

        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {

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
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.labelRes)
                        )
                    },
                    label = { Text(stringResource(item.labelRes)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = Color.White.copy(alpha = 0.5f),
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color.White.copy(alpha = 0.5f)
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
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.labelRes)
                        )
                    },
                    label = { Text(stringResource(item.labelRes)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = Color.White.copy(alpha = 0.5f),
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color.White.copy(alpha = 0.5f)
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
            containerColor = NavBarColor,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 4.dp
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.add_transaction),
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
