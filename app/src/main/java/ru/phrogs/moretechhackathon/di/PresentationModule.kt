package ru.phrogs.moretechhackathon.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.phrogs.moretechhackathon.presentation.viewmodel.MapViewModel

fun providePresentationModule() = module {

    viewModel {
        MapViewModel(get())
    }

}