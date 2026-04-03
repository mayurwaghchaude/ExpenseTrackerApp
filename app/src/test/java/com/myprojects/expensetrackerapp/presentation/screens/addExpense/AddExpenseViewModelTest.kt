package com.myprojects.expensetrackerapp.presentation.screens.addExpense

import com.myprojects.expensetrackerapp.domain.enums.ExpenseCategory
import com.myprojects.expensetrackerapp.domain.enums.ExpenseType
import com.myprojects.expensetrackerapp.domain.model.Expense
import com.myprojects.expensetrackerapp.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddExpenseViewModelTest {

    private lateinit var viewModel: AddExpenseViewModel
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
        viewModel = AddExpenseViewModel(expenseUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when description changes, uiState is updated`() {
        val newDescription = "New Title"
        viewModel.onDescriptionChange(newDescription)
        assertEquals(newDescription, viewModel.uiState.value.title)
    }

    @Test
    fun `when amount changes, uiState is updated`() {
        val newAmount = "100.50"
        viewModel.onAmountChange(newAmount)
        assertEquals(newAmount, viewModel.uiState.value.amount)
    }

    @Test
    fun `loadExpense updates state with existing expense data`() = runTest {
        val expenseId = "1"
        val expense = Expense(
            id = expenseId,
            title = "Old Title",
            amount = 50.0,
            category = "FOOD",
            type = ExpenseType.EXPENSE,
            date = 1000L,
            note = "Old Note"
        )
        whenever(getExpenses()).thenReturn(flowOf(listOf(expense)))

        viewModel.loadExpense(expenseId)

        val state = viewModel.uiState.value
        assertEquals("Old Title", state.title)
        assertEquals("50.0", state.amount)
        assertEquals(ExpenseCategory.FOOD, state.selectedCategory)
        assertEquals(ExpenseType.EXPENSE, state.selectedType)
        assertEquals(1000L, state.date)
        assertEquals("Old Note", state.note)
    }

    @Test
    fun `saveExpense with empty title sets error`() {
        viewModel.onDescriptionChange("")
        viewModel.onAmountChange("100")
        
        viewModel.saveExpense()
        
        assertEquals("Description cannot be empty", viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isSaved)
    }

    @Test
    fun `saveExpense with invalid amount sets error`() {
        viewModel.onDescriptionChange("Title")
        viewModel.onAmountChange("abc")
        
        viewModel.saveExpense()
        
        assertEquals("Enter a valid amount", viewModel.uiState.value.error)
        
        viewModel.onAmountChange("-10")
        viewModel.saveExpense()
        assertEquals("Enter a valid amount", viewModel.uiState.value.error)
    }

    @Test
    fun `saveExpense successfully adds new expense`() = runTest {
        viewModel.onDescriptionChange("New Expense")
        viewModel.onAmountChange("150.0")
        viewModel.onCategoryChange(ExpenseCategory.SHOPPING)
        viewModel.onTypeChange(ExpenseType.EXPENSE)
        
        viewModel.saveExpense()
        
        verify(addExpense).invoke(any())
        assertTrue(viewModel.uiState.value.isSaved)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `saveExpense successfully updates existing expense`() = runTest {
        val expenseId = "existing_id"
        viewModel.onDescriptionChange("Updated Expense")
        viewModel.onAmountChange("200.0")
        
        viewModel.saveExpense(expenseId)
        
        verify(updateExpense).invoke(argThat { id == expenseId && title == "Updated Expense" && amount == 200.0 })
        assertTrue(viewModel.uiState.value.isSaved)
    }
}
