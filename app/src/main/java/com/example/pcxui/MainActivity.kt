import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var speed = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedView = findViewById<PcxSpeedometerView>(R.id.pcxSpeedometer)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnMinus = findViewById<Button>(R.id.btnMinus)

        btnPlus.setOnClickListener {
            if (speed < 10f) speed += 1f
            speedView.currentSpeed = speed
        }

        btnMinus.setOnClickListener {
            if (speed > 0f) speed -= 1f
            speedView.currentSpeed = speed
        }
    }
}
