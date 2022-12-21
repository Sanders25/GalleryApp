package com.example.galleryapp.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface ImageDatasource {
    val imageFlow: Flow<List<Uri>>
}