package com.example.pcxui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var tvSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSpeed = findViewById(R.id.tvSpeed)

        // Simulasi pergerakan angka
        val handler = Handler(Looper.getMainLooper())
        val random = Random()

        handler.postDelayed(object : Runnable {
            override fun run() {
                val fakeSpeed = random.nextInt(101) // 0 - 100 km/h
                tvSpeed.text = fakeSpeed.toString()
                handler.postDelayed(this, 1500) 
            }
        }, 1000)
    }
}
