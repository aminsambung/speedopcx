package com.example.pcxui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class RpmArcView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        
        // Busur dibuat agak lebar memenuhi layar
        val rectF = RectF(-w * 0.2f, h * 0.2f, w * 1.2f, h * 1.8f)
        
        // 1. Gambar Arc
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 40f
        paint.color = Color.parseColor("#E0E0E0") // Abu-abu terang
        canvas.drawArc(rectF, 200f, 140f, false, paint)

        // 2. Gambar Garis & Angka RPM
        textPaint.color = Color.BLACK
        textPaint.textSize = 35f
        textPaint.textAlign = Paint.Align.CENTER
        
        val radius = rectF.width() / 2
        val centerX = rectF.centerX()
        val centerY = rectF.centerY()

        for (i in 0..10) {
            val angle = 200f + (i * 14f) // 140 derajat dibagi 10
            val rad = Math.toRadians(angle.toDouble())
            
            // Garis Tick
            val tickStartX = centerX + (radius - 20) * cos(rad).toFloat()
            val tickStartY = centerY + (radius - 20) * sin(rad).toFloat()
            val tickEndX = centerX + (radius + 20) * cos(rad).toFloat()
            val tickEndY = centerY + (radius + 20) * sin(rad).toFloat()

            // Warna merah untuk RPM tinggi (8-10)
            paint.color = if (i >= 8) Color.RED else Color.BLACK
            paint.strokeWidth = 5f
            canvas.drawLine(tickStartX, tickStartY, tickEndX, tickEndY, paint)

            // Angka RPM
            val textX = centerX + (radius - 60) * cos(rad).toFloat()
            val textY = centerY + (radius - 60) * sin(rad).toFloat()
            canvas.drawText(i.toString(), textX, textY + 10, textPaint)
        }
    }
}
