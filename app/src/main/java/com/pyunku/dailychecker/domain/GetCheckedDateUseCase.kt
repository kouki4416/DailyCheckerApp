package com.pyunku.dailychecker.domain

import com.pyunku.dailychecker.calendar.data.OfflineFirstCheckedDateRepository
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckedDateUseCase @Inject constructor(
    private val offlineFirstCheckedDateRepository: OfflineFirstCheckedDateRepository,
) {
    operator fun invoke() : Flow<List<CheckedDate>> {
        return offlineFirstCheckedDateRepository.getCheckedDates()
    }
}