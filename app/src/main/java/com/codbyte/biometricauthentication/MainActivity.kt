package com.codbyte.biometricauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import com.codbyte.biometricauthentication.utils.BiometricUtil
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAuthenticate = findViewById<Button>(R.id.buttonAuthenticate)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        buttonAuthenticate.setOnClickListener(this)

        tvStatus.text = if (BiometricUtil.isHardwareAvailable(this)) {
            if (BiometricUtil.hasBiometricEnrolled(this)) {
                getString(R.string.biometric_available)
            } else {
                getString(R.string.no_biometric_enrolled)
            }
        } else {
            getString(R.string.biometric_not_available)
        }

    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.buttonAuthenticate)
            startAuthenticate()
    }

    private fun startAuthenticate() {
        val executor = Executors.newSingleThreadExecutor()

        val biometricPrompt = BiometricPrompt(this, executor, biometricCallback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication Title")
            .setSubtitle("Sub title")
            .setDescription("Description: Identify yourself by Biometrics")
            .setNegativeButtonText("Cancel Authentication")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private val biometricCallback = object: BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, errString, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, "Successfully Authenticated", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
