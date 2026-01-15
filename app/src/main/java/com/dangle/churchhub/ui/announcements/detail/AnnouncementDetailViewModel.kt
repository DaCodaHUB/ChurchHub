package com.dangle.churchhub.ui.announcements.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangle.churchhub.data.local.dao.AnnouncementDao
import com.dangle.churchhub.data.local.entity.AnnouncementEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementDetailViewModel @Inject constructor(
    private val dao: AnnouncementDao
) : ViewModel() {

    private val _announcement = MutableStateFlow<AnnouncementEntity?>(null)
    val announcement: StateFlow<AnnouncementEntity?> = _announcement

    fun load(id: String) {
        viewModelScope.launch {
            dao.observeById(id).collect { _announcement.value = it }
        }
    }
}
