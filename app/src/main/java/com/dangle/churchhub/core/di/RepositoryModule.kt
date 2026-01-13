package com.dangle.churchhub.core.di

import com.dangle.churchhub.data.repository.*
import com.dangle.churchhub.domain.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton abstract fun bindChurchInfoRepository(impl: ChurchInfoRepositoryImpl): ChurchInfoRepository
    @Binds @Singleton abstract fun bindAnnouncementsRepository(impl: AnnouncementsRepositoryImpl): AnnouncementsRepository
    @Binds @Singleton abstract fun bindReadingPlanRepository(impl: ReadingPlanRepositoryImpl): ReadingPlanRepository
    @Binds @Singleton abstract fun bindSermonsRepository(impl: SermonsRepositoryImpl): SermonsRepository
}
