package com.farmersmarket.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.farmersmarket.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "farmersmarket.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<Farmersmarketmodel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class farmersmarketJSONStore (private val context: Context) : ProduceStore {

    var farmersmarkets = mutableListOf<Farmersmarketmodel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<Farmersmarketmodel> {
        logAll()
        return farmersmarkets
    }

    override fun create(farmersmarket: Farmersmarketmodel) {
        farmersmarket.id = generateRandomId()
        farmersmarkets.add(farmersmarket)
        serialize()
    }


    override fun update(farmersmarket: Farmersmarketmodel) {
        val FarmersmarketsList = findAll() as ArrayList<Farmersmarketmodel>
        var foundFarmersmarket: Farmersmarketmodel? =FarmersmarketsList.find { p -> p.id == farmersmarket.id }
        if (foundFarmersmarket != null) {
            foundFarmersmarket.title = farmersmarket.title
            foundFarmersmarket.description = farmersmarket.description
            foundFarmersmarket.image = farmersmarket.image
            foundFarmersmarket.lat = farmersmarket.lat
            foundFarmersmarket.lng = farmersmarket.lng
            foundFarmersmarket.zoom = farmersmarket.zoom
        }
        serialize()
    }
    override fun delete(farmersmarket: Farmersmarketmodel) {
        farmersmarkets.remove(farmersmarket)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(farmersmarkets, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        farmersmarkets = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        farmersmarkets.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}