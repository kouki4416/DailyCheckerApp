package com.pyunku.dailychecker.domain

import com.pyunku.dailychecker.calendar.data.OfflineFirstCheckedDateRepository
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import javax.inject.Inject

class ToggleCheckedDateUseCase @Inject constructor(
    private val offlineFirstCheckedDateRepository: OfflineFirstCheckedDateRepository,
) {
    suspend operator fun invoke(checkedDate: CheckedDate) {
        if (offlineFirstCheckedDateRepository.getCheckedState(checkedDate)){
            offlineFirstCheckedDateRepository.deleteCheckedDate(checkedDate)
        } else {
            offlineFirstCheckedDateRepository.addCheckedDate(checkedDate)
        }
    }
}