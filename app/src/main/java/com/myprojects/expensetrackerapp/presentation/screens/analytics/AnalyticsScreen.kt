package com.myprojects.expensetrackerapp.presentation.screens.analytics

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myprojects.expensetrackerapp.R
import com.myprojects.expensetrackerapp.data.entity.CategoryTotal
import com.myprojects.expensetrackerapp.domain.enums.ExpenseCategory
import com.myprojects.expensetrackerapp.presentation.components.BottomNavBar
import com.myprojects.expensetrackerapp.ui.theme.ExpenseRed
import com.myprojects.expensetrackerapp.ui.theme.IncomeGreen
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Summary cards
            item {
                Text(
                    text = stringResource(R.string.analytics_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        label = stringResource(R.string.total_spent_label),
                        amount = formatter.format(uiState.totalExpense),
                        amountColor = ExpenseRed,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        label = stringResource(R.string.total_income_label),
                        amount = formatter.format(uiState.totalIncome),
                        amountColor = IncomeGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Pie Chart Section
            if (uiState.categoryTotals.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.spending_distribution),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PieChart(
                        data = uiState.categoryTotals,
                        totalExpense = uiState.totalExpense,
                        formatter = formatter
                    )
                }
            }

            // Category breakdown
            item {
                Text(
                    text = stringResource(R.string.by_category),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (uiState.categoryTotals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_data_yet),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                items(uiState.categoryTotals) { categoryTotal ->
                    CategoryBar(
                        categoryTotal = categoryTotal,
                        maxAmount = uiState.categoryTotals.first().total,
                        totalExpense = uiState.totalExpense,
                        formatter = formatter
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(
    data: List<CategoryTotal>,
    totalExpense: Double,
    formatter: NumberFormat,
    radiusOuter: Dp = 80.dp,
    chartBarWidth: Dp = 16.dp,
    animDuration: Int = 1000
) {

    var animationPlayed by remember { mutableStateOf(false) }

    val animateFloat by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = "pie_chart_anim"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier.size(radiusOuter * 2f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var lastValue = -90f
                data.forEachIndexed { index, categoryTotal ->
                    val category = ExpenseCategory.entries.find {
                        it.name == categoryTotal.category
                    } ?: ExpenseCategory.OTHER
                    val sweepAngle = (categoryTotal.total / totalExpense * 360).toFloat()
                    drawArc(
                        color = category.iconTint,
                        startAngle = lastValue,
                        sweepAngle = sweepAngle * animateFloat,
                        useCenter = false,
                        style = Stroke(width = chartBarWidth.toPx(), cap = StrokeCap.Round)
                    )
                    lastValue += sweepAngle
                }
            }

            // Text in center
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.total),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = formatter.format(totalExpense).substringBefore("."),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            data.take(5).forEachIndexed { index, categoryTotal ->
                val category = ExpenseCategory.entries.find {
                    it.name == categoryTotal.category
                } ?: ExpenseCategory.OTHER

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(category.iconTint)
                    )
                    Text(
                        text = stringResource(category.labelRes),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    label: String,
    amount: String,
    amountColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

@Composable
fun CategoryBar(
    categoryTotal: CategoryTotal,
    maxAmount: Double,
    totalExpense: Double,
    formatter: NumberFormat
) {
    val category = ExpenseCategory.entries.find {
        it.name == categoryTotal.category
    } ?: ExpenseCategory.OTHER

    val percentage = if (totalExpense > 0) {
        (categoryTotal.total / totalExpense * 100).toInt()
    } else 0

    val progress = if (totalExpense > 0) {
        (categoryTotal.total / totalExpense).toFloat()
    } else 0f

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = category.iconTint
                )
                Text(text = stringResource(category.labelRes), fontSize = 13.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = formatter.format(categoryTotal.total),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$percentage%",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = category.iconTint,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        )
    }
}
