@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.galleryapp.data

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import com.example.galleryapp.util.eventFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ImageDatasourceImpl @Inject constructor(
    private val application: Application
) : ImageDatasource {

    private val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    override val imageFlow: Flow<List<Uri>> = application.contentResolver.eventFlow(contentUri)
        .transform { emit(Unit) }
        .onStart { emit(Unit) }
        .map {
            val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA
            )

            val imageList = mutableListOf<Uri>()

            val contentResolver = application.contentResolver
            contentResolver.query(
                contentUri,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val imageUriIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
                    do {
                        imageList.add(Uri.parse(cursor.getString(imageUriIndex)))
                    } while (cursor.moveToNext())
                }
            }
            imageList.asReversed()
        }.buffer(Channel.UNLIMITED).flowOn(Dispatchers.IO)
}