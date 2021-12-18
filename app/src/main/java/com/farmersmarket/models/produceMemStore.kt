package com.farmersmarket.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class produceMemStore : ProduceStore{

    val farmersmarkets = ArrayList<Farmersmarketmodel>()

    override fun findAll(): List<Farmersmarketmodel> {
        return farmersmarkets
    }

    override fun create(farmersmarket: Farmersmarketmodel) {
        farmersmarket.id = getId()
        farmersmarkets.add(farmersmarket)
        logAll()
    }

    override fun update(farmersmarket: Farmersmarketmodel) {
        var foundFarmersmarket: Farmersmarketmodel? =
            farmersmarkets.find { p -> p.id == farmersmarket.id }
        if (foundFarmersmarket != null) {
            foundFarmersmarket.title = farmersmarket.title
            foundFarmersmarket.description = farmersmarket.description
            foundFarmersmarket.image = farmersmarket.image
            foundFarmersmarket.lat = farmersmarket.lat
            foundFarmersmarket.lng = farmersmarket.lng
            foundFarmersmarket.zoom = farmersmarket.zoom
            logAll()
        }
    }

    override fun delete(farmersmarket: Farmersmarketmodel) {
        farmersmarkets.remove(farmersmarket)
    }

   private fun logAll() {
        farmersmarkets.forEach{ i("${it}") }
    }
}