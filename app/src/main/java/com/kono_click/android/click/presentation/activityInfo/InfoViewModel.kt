package com.kono_click.android.click.presentation.activityInfo

import androidx.lifecycle.ViewModel
import com.kono_click.android.click.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(val repository: BaseRepository) : ViewModel() {
}