package com.farmersmarket.models

interface ProduceStore {
    fun findAll(): List<Farmersmarketmodel>
    fun create(produce: Farmersmarketmodel)
    fun update(produce: Farmersmarketmodel)
    fun delete(produce: Farmersmarketmodel)
}