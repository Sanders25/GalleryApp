package com.example.galleryapp.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProvideImageDatasource {

    @Binds
    fun provideImageDatasource(impl: ImageDatasourceImpl): ImageDatasource
}