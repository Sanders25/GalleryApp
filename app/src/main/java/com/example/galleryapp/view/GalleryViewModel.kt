package com.example.galleryapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.usecase.ObserveImageFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class GalleryViewModel @Inject constructor(
    observeImageFlow: ObserveImageFlow
) : ViewModel() {

    val imageFlow = observeImageFlow().shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5.seconds),
        1
    )
}