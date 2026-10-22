package com.example.pcxui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class PcxSpeedometerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var currentSpeed = 0f
        set(value) {
            field = value.coerceIn(0f, 10f)
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        
        // Posisi tengah layar (untuk teks dan indikator)
        val centerX = w / 2
        val centerY = h / 2
        
        // Radius busur atas (dibuat lebih lebar agar memenuhi layar)
        val radius = w.coerceAtMost(h) * 0.42f
        val arcCenterY = centerY - 50f // Pusat busur digeser ke atas

        canvas.drawColor(Color.parseColor("#1A1A1A")) // Background gelap

        // ==========================================
        // 1. GAMBAR BUSUR (ARC) ABU-ABU
        // ==========================================
        val rectF = RectF(centerX - radius, arcCenterY - radius, centerX + radius, arcCenterY + radius)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 35f
        paint.color = Color.parseColor("#333333")
        
        // Mulai dari 150 derajat, sapu 240 derajat
        canvas.drawArc(rectF, 150f, 240f, false, paint)

        // ==========================================
        // 2. TICK MARKS & ANGKA (0-10)
        // ==========================================
        textPaint.color = Color.WHITE
        textPaint.textSize = 55f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        
        for (i in 0..10) {
            val angle = 150f + (i * 24f) 
            val rad = Math.toRadians(angle.toDouble())
            
            // Garis Tick
            val tickStartX = centerX + (radius - 20) * cos(rad).toFloat()
            val tickStartY = arcCenterY + (radius - 20) * sin(rad).toFloat()
            val tickEndX = centerX + (radius + 20) * cos(rad).toFloat()
            val tickEndY = arcCenterY + (radius + 20) * sin(rad).toFloat()

            // Warna merah untuk 8, 9, 10
            paint.color = if (i >= 8) Color.RED else Color.WHITE
            paint.strokeWidth = 6f
            canvas.drawLine(tickStartX, tickStartY, tickEndX, tickEndY, paint)

            // Posisi Angka
            val textX = centerX + (radius - 80) * cos(rad).toFloat()
            val textY = arcCenterY + (radius - 80) * sin(rad).toFloat()
            
            textPaint.color = if (i >= 8) Color.RED else Color.WHITE
            canvas.drawText(i.toString(), textX, textY + 15, textPaint)
        }

        // ==========================================
        // 3. GAMBAR JARUM MERAH
        // ==========================================
        val speedAngle = 150f + (currentSpeed * 24f)
        val speedRad = Math.toRadians(speedAngle.toDouble())
        
        paint.color = Color.RED
        paint.strokeWidth = 12f
        paint.strokeCap = Paint.Cap.ROUND
        
        // Jarum dimulai dari pusat busur (arcCenterY)
        canvas.drawLine(
            centerX, arcCenterY,
            centerX + (radius - 30) * cos(speedRad).toFloat(),
            arcCenterY + (radius - 30) * sin(speedRad).toFloat(),
            paint
        )
        
        // Titik pusat jarum
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, arcCenterY, 15f, paint)

        // ==========================================
        // 4. TEKS KECEPATAN BESAR (Tengah)
        // ==========================================
        // Teks diletakkan di tengah layar, bukan di pusat busur
        textPaint.color = Color.WHITE
        textPaint.textSize = 240f // Sangat besar
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(currentSpeed.toInt().toString(), centerX, centerY + 70, textPaint)

        // Teks "km/h"
        textPaint.textSize = 45f
        textPaint.typeface = Typeface.DEFAULT
        textPaint.color = Color.GRAY
        canvas.drawText("km/h", centerX, centerY + 130, textPaint)

        // ==========================================
        // 5. INDIKATOR BENSIN (Kiri)
        // ==========================================
        textPaint.color = Color.WHITE
        textPaint.textSize = 35f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        
        // Huruf F dan E
        canvas.drawText("F", 100f, arcCenterY - 100f, textPaint)
        canvas.drawText("E", 100f, arcCenterY + 150f, textPaint)
        
        // Kotak Bensin
        val fuelRect = RectF(85f, arcCenterY - 60f, 115f, arcCenterY + 100f)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.GRAY
        canvas.drawRoundRect(fuelRect, 5f, 5f, paint)
        
        // Isi Bensin
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#4CAF50")
        canvas.drawRect(88f, arcCenterY - 50f, 112f, arcCenterY + 20f, paint)

        // ==========================================
        // 6. ODOMETER (Bawah Tengah)
        // ==========================================
        textPaint.textSize = 55f
        textPaint.color = Color.WHITE
        canvas.drawText("TOTAL 160 km", centerX, h - 80f, textPaint)
    }
}
