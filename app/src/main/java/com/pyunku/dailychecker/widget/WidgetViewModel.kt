package com.pyunku.dailychecker.widget

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.domain.GetCheckedDateUseCase
import com.pyunku.dailychecker.domain.ToggleCheckedDateUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WidgetViewModel @Inject constructor(
    private val toggleCheckedDateUseCase: ToggleCheckedDateUseCase,
    private val getCheckedDateUseCase: GetCheckedDateUseCase
){
    fun loadCheckedDate(): Flow<List<CheckedDate>> = getCheckedDateUseCase()

    suspend fun toggleCheckedDate(checkedDate: CheckedDate){
        toggleCheckedDateUseCase(checkedDate)
    }

}
