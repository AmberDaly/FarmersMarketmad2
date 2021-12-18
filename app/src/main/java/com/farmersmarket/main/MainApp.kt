package com.farmersmarket.main

import android.app.Application
import com.farmersmarket.models.Farmersmarketmodel
import com.farmersmarket.models.farmersmarketJSONStore
import com.farmersmarket.models.ProduceStore
import com.farmersmarket.models.produceMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var farmersmarkets: ProduceStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
       farmersmarkets = farmersmarketJSONStore(applicationContext)
        i("farmers market started")
    }
}