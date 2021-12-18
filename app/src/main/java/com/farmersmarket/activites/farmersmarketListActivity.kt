package com.farmersmarket.activites


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmersmarket.R
import com.farmersmarket.adapters.FarmersmarketListener
import com.farmersmarket.adapters.farmersmarketAdapter
import com.farmersmarket.databinding.ActivityFarmersmarketListBinding
import com.farmersmarket.databinding.CardFarmersmarketBinding
import com.farmersmarket.main.MainApp
import com.farmersmarket.models.Farmersmarketmodel

class farmersmarketListActivity : AppCompatActivity(), FarmersmarketListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityFarmersmarketListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmersmarketListBinding.inflate(layoutInflater)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadfarmersmarket()

        registerRefreshCallback()

        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, farmersmarket::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, farmersmarketMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onFarmersmarketClick(farmersmarket: Farmersmarketmodel) {
        val launcherIntent = Intent(this, com.farmersmarket.activites.farmersmarket::class.java)
     launcherIntent.putExtra("farmersmarket_edit", farmersmarket)
        refreshIntentLauncher.launch(launcherIntent)
    }


    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadfarmersmarket() }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun loadfarmersmarket() {
        showFarmersmarket(app.farmersmarkets.findAll())
    }

    fun showFarmersmarket (placemarks: List<Farmersmarketmodel>) {
        binding.recyclerView.adapter = farmersmarketAdapter(placemarks, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}

