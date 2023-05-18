package com.reggya.faintek

import android.R
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.reggya.faintek.ApiResponseType.*
import com.reggya.faintek.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private var type: String = "Status"
    private val status = arrayOf("Semua", "Verify", "Not Verified")
    private var usersResponse = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preference = UserPreference(this)
        val uid = preference.getUserId()
        auth = Firebase.auth
        val user = auth.currentUser
        val isVerify = user?.isEmailVerified

        if (isVerify == true) {
            val dbRef = FirebaseDatabase.getInstance("https://faintek-510bd-default-rtdb.firebaseio.com/")
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
                    val user = users.find { it.uid == uid }
                    user.let {
                        val updateVerify = it?.copy(verify = true)
                        if (updateVerify != null) {
                            users[users.indexOf(it)] = updateVerify
                        }
                    }
                    database.setValue(users)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("ERROR", databaseError.message.toString())
                }
            })
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]



        val userAdapter = UserAdapter()
        viewModel.getListUser().observe(this){
            when(it.type){
                ERROR -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                SUCCESS -> {
                    userAdapter.setNewDAta(it.data)
                    usersResponse.addAll(it.data!!)
                }
                EMPTY -> Toast.makeText(this, "Data null", Toast.LENGTH_SHORT).show()
            }
        }
        val adapter_ = ArrayAdapter(this, R.layout.simple_spinner_item, status)
        adapter_.setDropDownViewResource(R.layout.simple_spinner_item)
        binding.status.apply {
            adapter = adapter_
            prompt = type
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                    (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    type = parent.getItemAtPosition(position).toString()
                    when (type ) {
                        status[0] -> userAdapter.setNewDAta(usersResponse)
                        status[1] -> {
                            val newData = usersResponse.filter { it.verify == true}
                            userAdapter.setNewDAta(newData)
                        }
                        status[2] -> {
                            val newData = usersResponse.filter { it.verify == false }
                            userAdapter.setNewDAta(newData)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userAdapter
        binding.recyclerView.setHasFixedSize(true)
    }
}