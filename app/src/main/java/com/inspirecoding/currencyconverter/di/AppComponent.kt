package com.inspirecoding.currencyconverter.di

import android.app.Application
import com.inspirecoding.currencyconverter.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        RetrofitModule::class,
        ViewModelFactoryModule::class,
        ViewModelsModule::class,
        CurrencyRepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application (application: Application) : Builder
        fun build() : AppComponent
    }

}