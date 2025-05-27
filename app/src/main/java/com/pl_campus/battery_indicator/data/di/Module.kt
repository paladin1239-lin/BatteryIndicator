package com.pl_campus.battery_indicator.data.di

import com.pl_campus.battery_indicator.data.utils.HelpersImp
import com.pl_campus.battery_indicator.domain.utils.Helpers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {

    @Binds
    abstract fun bindHelpers(
        helpersImp: HelpersImp
    ) :Helpers
}