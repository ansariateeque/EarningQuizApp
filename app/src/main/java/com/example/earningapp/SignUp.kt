package com.example.earningapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.earningapp.databinding.ActivitySignUpBinding
import com.example.earningapp.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding.signupbtn.setOnClickListener(View.OnClickListener {
            if (binding.name.text.toString().isEmpty()) {
                binding.name.setError("Enter your name")
                return@OnClickListener
            }

            if (binding.age.text.toString().isEmpty()) {
                binding.age.setError("Enter your age")
                return@OnClickListener
            }else{
                val isNumeric: Boolean =
                    binding.age.text.toString().chars().allMatch(Character::isDigit)
                if (!isNumeric || Integer.parseInt(binding.age.text.toString()) <1 ) {
                    binding.age.setError("please enter valid Age")
                    return@OnClickListener
                }
            }
            if (binding.email.text.toString().isEmpty()) {
                binding.email.setError("Enter your email")
                return@OnClickListener
            }
            if (binding.password.text.toString().isEmpty()) {
                binding.password.setError("Enter your password")
                return@OnClickListener
            }
            auth.createUserWithEmailAndPassword(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        var userIdvalue = task.getResult().user?.uid
                        var user: Users = Users(
                            binding.password.text.toString(),
                            binding.name.text.toString(),
                            binding.age.text.toString(),
                            binding.email.text.toString(),

                            )
                        if (userIdvalue != null) {
                            user.userid = userIdvalue
                            database.getReference().child("Users").child(userIdvalue).setValue(user)
                            database.getReference().child("PlayChance").child(Firebase.auth.currentUser!!.uid).child("Value").setValue(1)
                            database.getReference().child("PlayWinAmount").child(Firebase.auth.currentUser!!.uid).child("Value").setValue(100)
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        Toast.makeText(this, "successfull Signup", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

        }
        )

        binding.login1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        )
    }



    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    }