package com.reggya.faintek

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.reggya.faintek.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appName = arrayOf("F","A","N","I","N","T","E","K"," ","T","E","S","T")

        for (i in 0..12){
            Handler(Looper.getMainLooper()).postDelayed({ binding.appName.append(appName[i]) },
                (300+(i*200)).toLong()
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val preference = UserPreference(this)
            if (preference.getIsLogin()){
                setIntent(MainActivity::class.java)
            }else setIntent(LoginActivity::class.java)
        }, 2500)


    }

    private fun setIntent(activity: Class<out Activity>){
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }
}