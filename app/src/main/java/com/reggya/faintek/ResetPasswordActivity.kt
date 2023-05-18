package com.reggya.faintek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.reggya.faintek.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnSave.setOnClickListener {
            val email = binding.email.text.toString()

            if (email.isEmpty()) binding.email.error = "email tidak boleh kosong"
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) binding.email.error = "email tidak valid"
            else {

                mAuth!!.sendPasswordResetEmail(email) .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Link terkirim", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

    }
}