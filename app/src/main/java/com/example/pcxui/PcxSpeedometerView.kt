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
            field = value.coerceIn(0f, 10f) // Pastikan nilai tidak lebih dari 10
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        
        // Posisi tengah disesuaikan agar mirip layar TFT PCX
        val centerX = w / 2
        val centerY = h / 2 + 50f 
        val radius = w.coerceAtMost(h) * 0.35f // Radius lingkaran

        canvas.drawColor(Color.parseColor("#1A1A1A"))

        // 1. Gambar Arc Speedometer (Hanya bagian atas)
        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 35f
        paint.color = Color.parseColor("#333333")
        
        // Mulai dari 150 derajat, sapu 240 derajat (Hanya melengkung di atas)
        canvas.drawArc(rectF, 150f, 240f, false, paint)

        // 2. Gambar Tick Marks & Angka
        textPaint.color = Color.WHITE
        textPaint.textSize = 55f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        
        for (i in 0..10) {
            val angle = 150f + (i * 24f) // 24 derajat per angka
            val rad = Math.toRadians(angle.toDouble())
            
            // Garis Tick (Lebih tebal dan jelas)
            val tickStartX = centerX + (radius - 25) * cos(rad).toFloat()
            val tickStartY = centerY + (radius - 25) * sin(rad).toFloat()
            val tickEndX = centerX + (radius + 25) * cos(rad).toFloat()
            val tickEndY = centerY + (radius + 25) * sin(rad).toFloat()

            // Warna merah untuk angka 8, 9, 10
            paint.color = if (i >= 8) Color.RED else Color.WHITE
            paint.strokeWidth = 6f
            canvas.drawLine(tickStartX, tickStartY, tickEndX, tickEndY, paint)

            // Posisi Angka
            val textX = centerX + (radius - 75) * cos(rad).toFloat()
            val textY = centerY + (radius - 75) * sin(rad).toFloat()
            
            // Reset warna teks jadi putih (kecuali angka merah)
            textPaint.color = if (i >= 8) Color.RED else Color.WHITE
            canvas.drawText(i.toString(), textX, textY + 15, textPaint)
        }

        // 3. Gambar Jarum Merah
        val speedAngle = 150f + (currentSpeed * 24f)
        val speedRad = Math.toRadians(speedAngle.toDouble())
        
        paint.color = Color.RED
        paint.strokeWidth = 12f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(
            centerX, centerY,
            centerX + (radius - 40) * cos(speedRad).toFloat(),
            centerY + (radius - 40) * sin(speedRad).toFloat(),
            paint
        )
        
        // Titik pusat jarum
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, 15f, paint)

        // 4. Gambar Angka Kecepatan Besar di Tengah (Sangat Besar)
        textPaint.color = Color.WHITE
        textPaint.textSize = 220f // Ukuran diperbesar
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(currentSpeed.toInt().toString(), centerX, centerY + 70, textPaint)

        // Teks "km/h" di bawah angka
        textPaint.textSize = 50f
        textPaint.typeface = Typeface.DEFAULT
        textPaint.color = Color.GRAY
        canvas.drawText("km/h", centerX, centerY + 130, textPaint)

        // 5. Gambar Indikator Bensin (Kotak di sebelah Kiri)
        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        // Huruf F dan E
        canvas.drawText("F", centerX - radius - 60, centerY - radius + 60, textPaint)
        canvas.drawText("E", centerX - radius - 60, centerY + radius - 60, textPaint)
        
        // Kotak Bensin
        val fuelRect = RectF(centerX - radius - 50, centerY - 70, centerX - radius + 10, centerY + 70)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.color = Color.GRAY
        canvas.drawRoundRect(fuelRect, 10f, 10f, paint)
        
        // Isi Bensin (Hijau)
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#4CAF50")
        canvas.drawRect(centerX - radius - 45, centerY - 65, centerX - radius + 5, centerY, paint)

        // 6. Odometer (TOTAL) di Bawah
        textPaint.textSize = 60f
        textPaint.color = Color.WHITE
        canvas.drawText("TOTAL 160 km", centerX, centerY + radius + 110, textPaint)
    }
}
