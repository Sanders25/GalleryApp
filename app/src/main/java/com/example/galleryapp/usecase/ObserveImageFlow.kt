package com.example.galleryapp.usecase

import android.net.Uri
import com.example.galleryapp.data.ImageDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun interface ObserveImageFlow : () -> Flow<List<Uri>>

class ObserveImageFlowImpl @Inject constructor(
    private val datasource: ImageDatasource
) : ObserveImageFlow {
    override fun invoke(): Flow<List<Uri>> {
        return datasource.imageFlow
    }
}