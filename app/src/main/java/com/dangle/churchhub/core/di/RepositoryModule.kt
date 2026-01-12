package com.dangle.churchhub.core.di

import com.dangle.churchhub.data.repository.AnnouncementRepositoryImpl
import com.dangle.churchhub.domain.repo.AnnouncementRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindAnnouncementRepository(impl: AnnouncementRepositoryImpl): AnnouncementRepository
}