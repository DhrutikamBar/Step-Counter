package com.dhrutikambar.stepcounter.presentation.activity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dhrutikambar.stepcounter.R
import com.dhrutikambar.stepcounter.databinding.ActivityMainBinding
import com.dhrutikambar.stepcounter.presentation.viewModel.MainViewModel

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var steps: Int? = 0
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        //  binding.tvStepsCount.text = "$steps"

        observeStepsCount()
    }

    private fun observeStepsCount() {
        viewModel.stepsCount.observe(this) {
            binding.tvStepsCount.setText(it)
        }
    }

    override fun onResume() {
        super.onResume()
        stepCounterSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == stepCounterSensor) {
            steps = event.values[0].toInt()
            viewModel.updateStepCount(steps.toString())
            Toast.makeText(this, "Called", Toast.LENGTH_LONG).show()
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, accuracy: Int) {


        when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                // High accuracy, sensor data is reliable
                Toast.makeText(this, "SENSOR_STATUS_ACCURACY_HIGH", Toast.LENGTH_LONG).show()
            }
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                // Medium accuracy, might want to handle with caution
                Toast.makeText(this, "SENSOR_STATUS_ACCURACY_MEDIUM", Toast.LENGTH_LONG).show()

            }
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                // Low accuracy, data may not be very reliable
                Toast.makeText(this, "SENSOR_STATUS_ACCURACY_LOW", Toast.LENGTH_LONG).show()

            }
            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                // Unreliable, should not use the data
                Toast.makeText(this, "SENSOR_STATUS_UNRELIABLE", Toast.LENGTH_LONG).show()

            }
        }
    }


}