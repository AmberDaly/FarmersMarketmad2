package com.farmersmarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmersmarket.databinding.CardFarmersmarketBinding
import com.farmersmarket.models.Farmersmarketmodel
import com.squareup.picasso.Picasso

interface FarmersmarketListener {
    fun onFarmersmarketClick(farmersmarket: Farmersmarketmodel)
}

class farmersmarketAdapter constructor(
    private var farmersmarkets: List<Farmersmarketmodel>,
    private var listener: FarmersmarketListener
    ) :
    RecyclerView.Adapter<farmersmarketAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFarmersmarketBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val farmersmarket = farmersmarkets[holder.adapterPosition]
        holder.bind(farmersmarket, listener)
    }

    override fun getItemCount(): Int = farmersmarkets.size

    class MainHolder(private val binding : CardFarmersmarketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(farmersmarket: Farmersmarketmodel, listener: FarmersmarketListener) {
            binding.farmersmarketTitle.text = farmersmarket.title
            binding.description.text = farmersmarket.description
            Picasso.get().load(farmersmarket.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener{listener.onFarmersmarketClick(farmersmarket)}
        }
    }
}