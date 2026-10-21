import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.anastr.speedviewlib.SpeedView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvSpeed: TextView
    private lateinit var tvOdometer: TextView
    private lateinit var speedView: SpeedView

    private var totalDistance = 0.0 // dalam km
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSpeed = findViewById(R.id.tvSpeed)
        tvOdometer = findViewById(R.id.tvOdometer)
        speedView = findViewById(R.id.speedView)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Minta izin lokasi
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            com.google.android.gms.location.LocationRequest.create()
                .setInterval(1000) // update tiap 1 detik
                .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY),
            locationCallback,
            mainLooper
        )
    }

    private val locationCallback = object : com.google.android.gms.location.LocationCallback() {
        override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
            val location = locationResult.lastLocation ?: return

            // Kecepatan dari GPS (m/s → km/h)
            val speedKmh = (location.speed * 3.6f).toInt()
            tvSpeed.text = speedKmh.toString()
            speedView.speedTo(speedKmh.toFloat())

            // Hitung odometer (jarak)
            lastLocation?.let {
                val distance = it.distanceTo(location) / 1000.0 // meter → km
                totalDistance += distance
                tvOdometer.text = "TOTAL %.1f km".format(totalDistance)
            }
            lastLocation = location
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }
}
