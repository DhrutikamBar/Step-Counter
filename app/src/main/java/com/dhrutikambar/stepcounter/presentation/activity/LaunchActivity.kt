package com.dhrutikambar.stepcounter.presentation.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dhrutikambar.stepcounter.databinding.ActivityLaunchBinding

class LaunchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaunchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)




        createUI()

    }

    override fun onResume() {
        super.onResume()
        resumeUI()
    }

    private fun resumeUI() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (isPermissionGranted()) {
                goToMainActivity()
            } else {
                when {
                    shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION) -> {
                        showRedirectToSettingsUI()
                    }

                }

            }
        } else {
            goToMainActivity()
        }
    }

    private fun createUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (isPermissionGranted()) {
                goToMainActivity()
            } else {
                when {
                    shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION) -> {
                        showRedirectToSettingsUI()
                    }

                    else -> {
                        showRequestPermissionUI()
                    }
                }

            }
        } else {
            goToMainActivity()
        }
    }

    private fun showRedirectToSettingsUI() {

        binding.tvDescription.text =
            "Looks like you have denied the permission. For that reason we are unable to track your steps. Kindly enable permission in settings."
        binding.tvDescription.visibility = View.VISIBLE
        binding.actionButton.text = "Go to Settings"
        binding.actionButton.visibility = View.VISIBLE


        binding.actionButton.setOnClickListener {
            openPermissionSettings()
        }
    }

    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun showRequestPermissionUI() {
        binding.tvDescription.visibility = View.VISIBLE
        binding.actionButton.visibility = View.VISIBLE
        binding.tvDescription.setText("To track your steps and monitor your activity levels, we need access to your activity data. Please grant this permission to enable step counting.")
        binding.actionButton.setText("Allow")


        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                goToMainActivity()
            }
        }


        binding.actionButton.setOnClickListener {
            launcher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isPermissionGranted(): Boolean {
        var isGranted = false
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isGranted = true
        }
        return isGranted
    }
}