package com.example.galleryapp.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface BindUseCases {

    @Binds
    fun bindObserveImageFlow(impl: ObserveImageFlowImpl): ObserveImageFlow
}