package com.myprojects.expensetrackerapp.presentation.screens.transactions

import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModelTest {

    private lateinit var viewModel: TransactionsViewModel
    private lateinit var expenseUseCases: ExpenseUseCases

    private val getExpenses: GetExpensesUseCase = mock()
    private val addExpense: AddExpenseUseCase = mock()
    private val deleteExpense: DeleteExpenseUseCase = mock()
    private val updateExpense: UpdateExpenseUseCase = mock()
    private val getCategoryTotals: GetCategoryTotalsUseCase = mock()
    private val getSummary: GetSummaryUseCase = mock()
    private val getExpensesByDateRange: GetExpensesByDateRangeUseCase = mock()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testExpenses = listOf(
        Expense(id = "1", title = "Lunch", amount = 50.0, category = "FOOD", type = ExpenseType.EXPENSE, date = 1000L),
        Expense(id = "2", title = "Salary", amount = 5000.0, category = "SALARY", type = ExpenseType.INCOME, date = 2000L),
        Expense(id = "3", title = "Coffee", amount = 10.0, category = "FOOD", type = ExpenseType.EXPENSE, date = 3000L)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        expenseUseCases = ExpenseUseCases(
            getExpenses = getExpenses,
            addExpense = addExpense,
            deleteExpense = deleteExpense,
            updateExpense = updateExpense,
            getCategoryTotals = getCategoryTotals,
            getSummary = getSummary,
            getExpensesByDateRange = getExpensesByDateRange
        )
        
        whenever(getExpenses()).thenReturn(flowOf(testExpenses))
        viewModel = TransactionsViewModel(expenseUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads all expenses`() {
        assertEquals(testExpenses, viewModel.uiState.value.allExpenses)
        assertEquals(testExpenses, viewModel.uiState.value.filteredExpenses)
    }

    @Test
    fun `search query filters expenses by title`() {
        viewModel.onSearchQueryChange("Lunch")
        
        assertEquals(1, viewModel.uiState.value.filteredExpenses.size)
        assertEquals("Lunch", viewModel.uiState.value.filteredExpenses[0].title)
    }

    @Test
    fun `search query filters expenses by category`() {
        viewModel.onSearchQueryChange("FOOD")
        
        assertEquals(2, viewModel.uiState.value.filteredExpenses.size)
        assertTrue(viewModel.uiState.value.filteredExpenses.all { it.category == "FOOD" })
    }

    @Test
    fun `income filter returns only income transactions`() {
        viewModel.onFilterChange(TransactionFilter.INCOME)
        
        assertEquals(1, viewModel.uiState.value.filteredExpenses.size)
        assertEquals(ExpenseType.INCOME, viewModel.uiState.value.filteredExpenses[0].type)
    }

    @Test
    fun `expense filter returns only expense transactions`() {
        viewModel.onFilterChange(TransactionFilter.EXPENSE)
        
        assertEquals(2, viewModel.uiState.value.filteredExpenses.size)
        assertTrue(viewModel.uiState.value.filteredExpenses.all { it.type == ExpenseType.EXPENSE })
    }

    @Test
    fun `combined search and filter works correctly`() {
        viewModel.onFilterChange(TransactionFilter.EXPENSE)
        viewModel.onSearchQueryChange("Lunch")
        
        assertEquals(1, viewModel.uiState.value.filteredExpenses.size)
        assertEquals("Lunch", viewModel.uiState.value.filteredExpenses[0].title)
        
        viewModel.onSearchQueryChange("Salary") // Salary is INCOME
        assertEquals(0, viewModel.uiState.value.filteredExpenses.size)
    }

    @Test
    fun `deleteExpense calls delete use case`() = runTest {
        val expenseToDelete = testExpenses[0]
        viewModel.deleteExpense(expenseToDelete)
        
        verify(deleteExpense).invoke(expenseToDelete)
    }
    
    private fun assertTrue(condition: Boolean) {
        assert(condition)
    }
}
