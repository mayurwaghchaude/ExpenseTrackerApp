package com.myprojects.expensetrackerapp.domain.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.myprojects.expensetrackerapp.R

enum class ExpenseCategory(
    @get:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    FOOD(R.string.category_food, Icons.Rounded.Restaurant),
    TRANSPORT(R.string.category_transport, Icons.Rounded.DirectionsBus),
    BILLS(R.string.category_bills, Icons.Rounded.ElectricBolt),
    ENTERTAINMENT(R.string.category_entertainment, Icons.Rounded.Movie),
    HEALTH(R.string.category_health, Icons.Rounded.HealthAndSafety),
    SHOPPING(R.string.category_shopping, Icons.Rounded.ShoppingBag),
    SALARY(R.string.category_salary, Icons.Rounded.Work),
    OTHER(R.string.category_other, Icons.Rounded.Category)
}