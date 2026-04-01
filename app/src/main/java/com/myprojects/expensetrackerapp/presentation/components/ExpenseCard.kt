package com.myprojects.expensetrackerapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.expensetrackerapp.domain.enums.ExpenseCategory
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import com.myprojects.expensetrackerapp.domain.model.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val dateFormatter = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val category = ExpenseCategory.entries.find {
        it.name == expense.category
    } ?: ExpenseCategory.OTHER

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete expense") },
            text = { Text("Are you sure you want to delete \"${expense.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Category icon
        Surface(
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = Color(0xFF1D9E75),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Description + date
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = expense.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = dateFormatter.format(Date(expense.date)),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        // Amount
        Text(
            text = "${if (expense.type == ExpenseType.EXPENSE) "-" else "+"}${
                formatter.format(
                    expense.amount
                )
            }",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (expense.type == ExpenseType.EXPENSE) Color(0xFFE24B4A) else Color(0xFF1D9E75)
        )

        // Delete button
        IconButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp)
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}