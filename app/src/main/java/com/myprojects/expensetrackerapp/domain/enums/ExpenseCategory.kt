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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.myprojects.expensetrackerapp.R
import com.myprojects.expensetrackerapp.ui.theme.BillsTint
import com.myprojects.expensetrackerapp.ui.theme.EntertainmentTint
import com.myprojects.expensetrackerapp.ui.theme.FoodTint
import com.myprojects.expensetrackerapp.ui.theme.HealthTint
import com.myprojects.expensetrackerapp.ui.theme.TransportTint
import com.myprojects.expensetrackerapp.ui.theme.ShoppingTint
import com.myprojects.expensetrackerapp.ui.theme.SalaryTint
import com.myprojects.expensetrackerapp.ui.theme.OtherTint

enum class ExpenseCategory(
    @get:StringRes val labelRes: Int,
    val icon: ImageVector,
    val iconTint: Color
) {
    FOOD(R.string.category_food, Icons.Rounded.Restaurant, FoodTint),
    TRANSPORT(R.string.category_transport, Icons.Rounded.DirectionsBus, TransportTint),
    BILLS(R.string.category_bills, Icons.Rounded.ElectricBolt, BillsTint),
    ENTERTAINMENT(R.string.category_entertainment, Icons.Rounded.Movie, EntertainmentTint),
    HEALTH(R.string.category_health, Icons.Rounded.HealthAndSafety, HealthTint),
    SHOPPING(R.string.category_shopping, Icons.Rounded.ShoppingBag, ShoppingTint),
    SALARY(R.string.category_salary, Icons.Rounded.Work, SalaryTint),
    OTHER(R.string.category_other, Icons.Rounded.Category, OtherTint)
}