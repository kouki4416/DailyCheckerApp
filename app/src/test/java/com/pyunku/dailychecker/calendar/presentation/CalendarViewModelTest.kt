package com.pyunku.dailychecker.calendar.presentation

import com.pyunku.dailychecker.calendar.data.OfflineFirstCheckedDateRepository
import com.pyunku.dailychecker.common.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

@ExperimentalCoroutinesApi
class CalendarViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    private lateinit var viewModel: CalendarViewModel

    @Mock
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUpViewModel() {
        val offlineFirstCheckedDateRepository = OfflineFirstCheckedDateRepository(
            FakeCheckedDateDataSource()
        )
        viewModel = CalendarViewModel(
            offlineFirstCheckedDateRepository,
            userPreferencesRepository
        )
    }

    @Test
    fun initialState_CalendarScreenState_Default() {
        val initialState = viewModel.state.value
        assert(
            initialState == CalendarScreenState(
                checkedDates = listOf(),
                isLoading = true
            )
        )
    }
}