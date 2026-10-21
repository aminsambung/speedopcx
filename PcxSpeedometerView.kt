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
    
    // Variabel untuk simulasi data (nanti bisa diubah dari luar)
    var currentSpeed = 0f
        set(value) {
            field = value
            invalidate() // Redraw view saat nilai berubah
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val centerX = w / 2
        val centerY = h / 2
        val radius = w.coerceAtMost(h) * 0.4f

        // 1. Gambar Background Gelap
        canvas.drawColor(Color.parseColor("#1A1A1A"))

        // 2. Gambar Arc Speedometer (Abu-abu)
        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 30f
        paint.color = Color.parseColor("#333333")
        // Mulai dari 150 derajat, sapu 240 derajat (0 - 10)
        canvas.drawArc(rectF, 150f, 240f, false, paint)

        // 3. Gambar Tick Marks & Angka
        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
        
        for (i in 0..10) {
            val angle = 150f + (i * 24f) // 240 derajat / 10 interval = 24 derajat
            val rad = Math.toRadians(angle.toDouble())
            
            // Posisi tick
            val tickStartX = centerX + (radius - 20) * cos(rad).toFloat()
            val tickStartY = centerY + (radius - 20) * sin(rad).toFloat()
            val tickEndX = centerX + (radius + 20) * cos(rad).toFloat()
            val tickEndY = centerY + (radius + 20) * sin(rad).toFloat()

            paint.color = if (i >= 8) Color.RED else Color.WHITE
            paint.strokeWidth = 5f
            canvas.drawLine(tickStartX, tickStartY, tickEndX, tickEndY, paint)

            // Posisi Angka
            val textX = centerX + (radius - 60) * cos(rad).toFloat()
            val textY = centerY + (radius - 60) * sin(rad).toFloat()
            canvas.drawText(i.toString(), textX, textY + 15, textPaint)
        }

        // 4. Gambar Jarum Merah
        val speedAngle = 150f + (currentSpeed / 10f * 24f)
        val speedRad = Math.toRadians(speedAngle.toDouble())
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(
            centerX, centerY,
            centerX + (radius - 30) * cos(speedRad).toFloat(),
            centerY + (radius - 30) * sin(speedRad).toFloat(),
            paint
        )

        // 5. Gambar Angka Kecepatan Besar di Tengah
        textPaint.color = Color.WHITE
        textPaint.textSize = 150f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(currentSpeed.toInt().toString(), centerX, centerY + 40, textPaint)

        // 6. Gambar Teks "km/h"
        textPaint.textSize = 40f
        textPaint.typeface = Typeface.DEFAULT
        textPaint.color = Color.GRAY
        canvas.drawText("km/h", centerX, centerY + 100, textPaint)

        // 7. Gambar Indikator Bensin (Kiri)
        textPaint.color = Color.WHITE
        textPaint.textSize = 30f
        canvas.drawText("F", centerX - radius - 40, centerY - radius + 50, textPaint)
        canvas.drawText("E", centerX - radius - 40, centerY + radius - 50, textPaint)
        
        // Gambar kotak baterai/bensin sederhana
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#444444")
        canvas.drawRect(centerX - radius - 30, centerY - 60, centerX - radius + 10, centerY + 60, paint)
        paint.color = Color.GREEN // Simulasi bensin penuh
        canvas.drawRect(centerX - radius - 30, centerY - 60, centerX - radius + 10, centerY, paint)

        // 8. Odometer (Bawah)
        textPaint.textSize = 45f
        textPaint.color = Color.WHITE
        canvas.drawText("TOTAL 160 km", centerX, centerY + radius + 80, textPaint)
    }
}
