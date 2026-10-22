package com.example.pcxui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Panggil Custom View milikmu
        val speedView = findViewById<PcxSpeedometerView>(R.id.pcxSpeedometer)

        // 2. Buat simulasi pergerakan jarum
        val handler = Handler(Looper.getMainLooper())
        val random = Random()

        handler.postDelayed(object : Runnable {
            override fun run() {
                // Buat angka acak dari 0.0 sampai 10.0
                val fakeSpeed = random.nextFloat() * 10f
                
                // Kirim angka ke Custom View
                speedView.currentSpeed = fakeSpeed
                
                // Ulangi setiap 1.5 detik
                handler.postDelayed(this, 1500)
            }
        }, 1000)
    }
}
