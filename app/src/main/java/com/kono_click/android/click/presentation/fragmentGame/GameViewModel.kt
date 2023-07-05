package com.kono_click.android.click.presentation.fragmentGame

import androidx.lifecycle.ViewModel
import com.kono_click.android.click.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(val repository: BaseRepository) : ViewModel() {
}