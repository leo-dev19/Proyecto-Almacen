package com.example.dorothy.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.dorothy.R
import com.example.dorothy.producto.ProductosActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class MenuActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // CORREGIDO

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    Log.d("NAV", "Home presionado")
                    startActivity(Intent(this, ProductosActivity::class.java))
                    true
                }
                R.id.bottom_search -> {
                    Log.d("NAV", "Search presionado")
                    // Acción para Search
                    true
                }
                R.id.bottom_profile -> {
                    Log.d("NAV", "Profile presionado")
                    // Acción para Profile
                    true
                }
                else -> false
            }
        }
    }
    fun setActivityLayout(layoutResId: Int) {
        val contentFrame = findViewById<FrameLayout>(R.id.frame_container)
        layoutInflater.inflate(layoutResId, contentFrame, true)
    }
}
