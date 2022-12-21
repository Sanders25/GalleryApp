package com.example.galleryapp.util

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
fun ContentResolver.eventFlow(uri: Uri): Flow<Boolean> = callbackFlow {
    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            check(trySend(selfChange).isSuccess)
        }
    }
    registerContentObserver(uri, false, observer)

    awaitClose {
        unregisterContentObserver(observer)
    }
}.buffer(capacity = Channel.UNLIMITED)
    .flowOn(Dispatchers.Main.immediate)