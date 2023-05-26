package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


import androidx.databinding.DataBindingUtil
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            signUp(email, password)
        }

        authListener = AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser
            if (user != null) {
                // Đăng ký thành công, thực hiện các hành động cần thiết ở đây
            }
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {

                                // Gửi email xác thực thành công, thực hiện các hành động cần thiết ở đây
                                Toast.makeText(
                                    this@SignUp,
                                    "Sign up success",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                } else {
                    // Đăng ký thất bại, hiển thị thông báo lỗi
                    Toast.makeText(
                        this@SignUp,
                        "Sign up failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth.removeAuthStateListener(authListener)
        }
    }
}
