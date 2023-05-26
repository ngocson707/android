package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val REQUEST_CODE_SIGN_UP = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)

        val savedUsername = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("pass", null)

        if (savedUsername != null && savedPassword != null) {
            // Tự động đăng nhập nếu đã có thông tin đăng nhập được lưu trữ
            startMainActivity2()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity2::class.java)

                        // Lưu thông tin đăng nhập vào Shared Preferences
                        val editor = sharedPreferences.edit()
                        editor.putString("email", email)
                        editor.putString("pass", pass)
                        editor.apply()

                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignIn.setOnClickListener {
            startMainActivitySignUp()
        }

        binding.tvForgetPass.setOnClickListener {
            val email = binding.edtEmail.text.toString()

            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Reset password email sent to $email",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidUserException) {
                                Toast.makeText(
                                    this,
                                    "No account with the provided email address",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (exception is FirebaseAuthActionCodeException) {
                                Toast.makeText(
                                    this,
                                    "Invalid action code. Please try again later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (exception is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    this,
                                    "Invalid email address",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Password reset failed. Please try again later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivity2() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMainActivitySignUp() {
        val intent = Intent(this, SignUp::class.java)
        startActivityForResult(intent, REQUEST_CODE_SIGN_UP)
    }
}
