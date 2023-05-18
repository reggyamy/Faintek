package com.reggya.faintek.core.presenter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.reggya.faintek.BuildConfig.BASE_URL
import com.reggya.faintek.utils.ProgressDialog
import com.reggya.faintek.utils.UserPreference
import com.reggya.faintek.databinding.ActivityLoginBinding
import com.reggya.faintek.core.data.model.User
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var state = true
    private lateinit var auth : FirebaseAuth
    private lateinit var authLogin : FirebaseAuth
    private var type = "Register"
    private lateinit var progressBar : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            playAnimation()
            state = !state
            type = if (state) "Register" else "Login"
            binding.btnSave.text = type
            binding.question.text = if (state) "Sudah punya akun ?" else "Belum punya akun ?"
            binding.name.visibility = if (state) View.VISIBLE else View.GONE
            binding.title.text = type
            binding.login.text = if (state) "Login" else "Register"
            binding.forgetPassword.visibility = if (!state) View.VISIBLE else View.GONE
        }

        binding.forgetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        binding.btnSave.setOnClickListener {
            setUpLoginRegister()
        }

    }

    private fun setUpLoginRegister() {
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val passwordCfm = binding.passwordCfm.text.toString()

        auth = Firebase.auth
        if (!validation(name, email, password, passwordCfm)){
            progressBar = ProgressDialog(this)
            progressBar.showProgressBar()
            if (type == "Register") setRegister(name, email, password)
            else setLogin(email, password)
        }
    }

    private fun setLogin(email: String, password: String) {
        authLogin= Firebase.auth
        authLogin.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.dismissProgressBar()
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val user = authLogin.currentUser
                    val uid = user?.uid
                    val preferences = UserPreference(this)
                    if (uid != null) {
                        preferences.setLogin(uid)
                    }
                    preferences.isLogin(true)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    progressBar.dismissProgressBar()
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setRegister(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.dismissProgressBar()
                    Log.d(ContentValues.TAG, "registerWithEmail:success")
                    val user = auth.currentUser
                    val uid = user?.uid!!
                    val preferences = UserPreference(this)
                    preferences.setLogin(uid)
                    preferences.isLogin(true)
                    user.sendEmailVerification().addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this, "Link verifikasi terkirim", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val dbRef = FirebaseDatabase.getInstance(BASE_URL)
                    val database = dbRef.getReference("users")
                    database.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val users = mutableListOf<User>()
                            if (dataSnapshot.exists()) {
                                for (snapshot in dataSnapshot.children) {
                                    val user = snapshot.getValue(User::class.java)
                                    Log.e("user", user.toString())
                                    if (user != null) {
                                        users.add(user)
                                    }
                                }
                            }
                            users.add(User(name = name, email =email, uid = uid, verify =false))
                            database.setValue(users).addOnSuccessListener {

                            }

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                           Log.e("ERROR", databaseError.message.toString())
                        }
                    })
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    progressBar.dismissProgressBar()
                    Log.w(ContentValues.TAG, "registerWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validation(name: String, email: String, password: String, passwordCfm: String): Boolean {
        var error = false
        val upperCasePatten = Pattern.compile("[A-Z ]")
        val lowerCasePatten = Pattern.compile("[a-z ]")
        val numberCasePatten = Pattern.compile("[0-9 ]")
        if(type == "Register"){
            if (name.length < 3) {
                binding.name.error = "Nama harus lebih dari 3 karakter"
                error = true
            }
            if (name.length > 50) {
                binding.name.error = "Nama tidak boleh lebih dari 50 karakter"
                error = true
            }
            if (name.isEmpty()){
                binding.name.error = "Nama tidak boleh kosong"
                error = true
            }
        }
        if (password.length < 8) {
            binding.password.error = "Password minimal 8 karakter"
            error = true
        }else if(!upperCasePatten.matcher(password).find()){
            binding.password.error = "Password harus mengandung huruf kapital"
            error = true
        }else if(!lowerCasePatten.matcher(password).find()){
            binding.password.error = "Password harus mengandung huruf kecil"
            error = true
        }else if (!numberCasePatten.matcher(password).find()){
            binding.password.error = "Password harus mengandung angka"
            error = true
        }
        else{
            if (password != passwordCfm){
                binding.passwordCfm.error = "konfirmasi password tidak sama dengan password yang dimasukkan"
                error = true
            }
        }
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Email tidak valid"
        }
        if (email.isEmpty()){
            binding.email.error = "Email tidak boleh kosong"
            error = true
        }
        if (password.isEmpty()){
            binding.password.error = "Password tidak boleh kosong"
            error = true
        }
        if (passwordCfm.isEmpty()){
            binding.password.error = "Password tidak boleh kosong"
            error = true
        }
        return error
    }

    private fun playAnimation() {
        binding.title.alpha = 0f
        binding.email.alpha = 0f
        binding.passwordCfm.alpha = 0f
        binding.name.alpha = 0f
        binding.password.alpha = 0f
        binding.containerLogin.alpha = 0f
        binding.btnSave.alpha = 0f
        binding.forgetPassword.alpha = 0f

        val login = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.name, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(300)
        val password= ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(300)
        val passwordCfm = ObjectAnimator.ofFloat(binding.passwordCfm, View.ALPHA, 1f).setDuration(300)
        val container = ObjectAnimator.ofFloat(binding.containerLogin, View.ALPHA, 1f).setDuration(300)
        val btLogin = ObjectAnimator.ofFloat(binding.btnSave, View.ALPHA, 1f).setDuration(300)
        val forgetPassword = ObjectAnimator.ofFloat(binding.forgetPassword, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(login, name,  email, password, passwordCfm, container, btLogin, forgetPassword)
            startDelay = 300
        }.start()
    }
}