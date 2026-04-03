package com.myprojects.expensetrackerapp.presentation.screens.analytics

import com.myprojects.expensetrackerapp.data.entity.CategoryTotal
import com.myprojects.expensetrackerapp.domain.usecases.*
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
class AnalyticsViewModelTest {

    private lateinit var viewModel: AnalyticsViewModel
    private lateinit var expenseUseCases: ExpenseUseCases

    private val getExpenses: GetExpensesUseCase = mock()
    private val addExpense: AddExpenseUseCase = mock()
    private val deleteExpense: DeleteExpenseUseCase = mock()
    private val updateExpense: UpdateExpenseUseCase = mock()
    private val getCategoryTotals: GetCategoryTotalsUseCase = mock()
    private val getSummary: GetSummaryUseCase = mock()
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
    fun `initial state is correct and data is observed`() = runTest {
        val testCategoryTotals = listOf(
            CategoryTotal("FOOD", 500.0),
            CategoryTotal("SHOPPING", 200.0)
        )
        whenever(getCategoryTotals()).thenReturn(flowOf(testCategoryTotals))
        whenever(getSummary.totalIncome()).thenReturn(flowOf(2000.0))
        whenever(getSummary.totalExpense()).thenReturn(flowOf(700.0))

        viewModel = AnalyticsViewModel(expenseUseCases)

        val state = viewModel.uiState.value
        assertEquals(2000.0, state.totalIncome, 0.0)
        assertEquals(700.0, state.totalExpense, 0.0)
        assertEquals(1300.0, state.balance, 0.0)
        assertEquals(testCategoryTotals, state.categoryTotals)
    }
}
