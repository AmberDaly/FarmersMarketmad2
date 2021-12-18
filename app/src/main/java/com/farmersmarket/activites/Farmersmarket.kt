package com.farmersmarket.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.farmersmarket.R
import com.farmersmarket.databinding.FarmersMarketBinding
import com.farmersmarket.helpers.showImagePicker
import com.farmersmarket.main.MainApp
import com.farmersmarket.models.Farmersmarketmodel
import com.farmersmarket.models.Location
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

import timber.log.Timber
import timber.log.Timber.i

class farmersmarket : AppCompatActivity() {

    private lateinit var binding: FarmersMarketBinding
    var farmersmarket= Farmersmarketmodel()

    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    //var location = Location(52.245696, -7.139102, 15f)
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = FarmersMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        app = application as MainApp

        i("farmers market app started...")

        if (intent.hasExtra("farmersmarket_edit")) {
            edit = true
            farmersmarket = intent.extras?.getParcelable("farmersmarket_edit")!!
            binding.farmersmarket.setText(farmersmarket.title)
            binding.description.setText(farmersmarket.description)
            binding.btnAdd.setText(R.string.save_farmersmarket)
            Picasso.get()
                .load(farmersmarket.image)
                .into(binding.farmersmarketImage)
            if (farmersmarket.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_farmersmarket_image)
            }

        }

        binding.btnAdd.setOnClickListener() {
            farmersmarket.title = binding.farmersmarket.text.toString()
            farmersmarket.description = binding.description.text.toString()
            if (farmersmarket.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_farmersmarket_title, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if (edit) {
                    app.farmersmarkets.update(farmersmarket.copy())
                } else {
                    app.farmersmarkets.create(farmersmarket.copy())
                }
            }

            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)

        }


        binding.farmersmarketLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (farmersmarket.zoom != 0f) {
                location.lat =  farmersmarket.lat
                location.lng = farmersmarket.lng
                location.zoom = farmersmarket.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_produce, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.farmersmarkets.delete(farmersmarket)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            farmersmarket.image = result.data!!.data!!
                            Picasso.get()
                                .load(farmersmarket.image)
                                .into(binding.farmersmarketImage)
                            binding.chooseImage.setText(R.string.change_farmersmarket_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            farmersmarket.lat = location.lat
                            farmersmarket.lng = location.lng
                            farmersmarket.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}