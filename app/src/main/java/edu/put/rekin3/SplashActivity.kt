package edu.put.rekin3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Duration of wait
        val splashDuration = 2000L // 2000 milliseconds or 2 seconds

        Handler().postDelayed({
            // Start your app main activity
            startActivity(Intent(this, MainActivity::class.java))

            // Close this activity
            finish()
        }, splashDuration)
    }
}