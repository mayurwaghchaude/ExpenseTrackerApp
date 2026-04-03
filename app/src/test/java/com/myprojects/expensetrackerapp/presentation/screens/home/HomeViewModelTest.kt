package com.myprojects.expensetrackerapp.presentation.screens.home

import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.usecases.*
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var expenseUseCases: ExpenseUseCases
    
    private val getExpenses: GetExpensesUseCase = mock()
    private val getSummary: GetSummaryUseCase = mock()
    private val deleteExpense: DeleteExpenseUseCase = mock()
    private val addExpense: AddExpenseUseCase = mock()
    private val updateExpense: UpdateExpenseUseCase = mock()
    private val getCategoryTotals: GetCategoryTotalsUseCase = mock()
    private val getExpensesByDateRange: GetExpensesByDateRangeUseCase = mock()

    private val testDispatcher = UnconfinedTestDispatcher()

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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        whenever(getExpenses()).thenReturn(flowOf(emptyList()))
        whenever(getSummary.totalIncome()).thenReturn(flowOf(0.0))
        whenever(getSummary.totalExpense()).thenReturn(flowOf(0.0))
        
        viewModel = HomeViewModel(expenseUseCases)
        
        assertEquals(0.0, viewModel.uiState.value.totalIncome, 0.0)
        assertEquals(0.0, viewModel.uiState.value.totalExpense, 0.0)
        assertEquals(0.0, viewModel.uiState.value.balance, 0.0)
        assertEquals(emptyList<Expense>(), viewModel.uiState.value.expenses)
    }

    @Test
    fun `when summary and expenses update, uiState reflects changes`() = runTest {
        val testExpenses = listOf(
            Expense(id = "1", title = "Lunch", amount = 50.0, category = "FOOD", type = ExpenseType.EXPENSE, date = 1000L)
        )
        whenever(getExpenses()).thenReturn(flowOf(testExpenses))
        whenever(getSummary.totalIncome()).thenReturn(flowOf(1000.0))
        whenever(getSummary.totalExpense()).thenReturn(flowOf(50.0))
        
        viewModel = HomeViewModel(expenseUseCases)
        
        assertEquals(1000.0, viewModel.uiState.value.totalIncome, 0.0)
        assertEquals(50.0, viewModel.uiState.value.totalExpense, 0.0)
        assertEquals(950.0, viewModel.uiState.value.balance, 0.0)
        assertEquals(testExpenses, viewModel.uiState.value.expenses)
    }
}
