package com.bladerco.buyandsellapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bladerco.buyandsellapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvSignUp: TextView
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.et_email_sign_in)
        etPassword = findViewById(R.id.et_password_sign_in)
        tvSignUp = findViewById(R.id.tv_sign_up)
        btnSignIn = findViewById(R.id.btn_sign_in)

        // Checks to see if the user is already signed in
        FirebaseAuth.getInstance().currentUser?.let {
            if (it.isEmailVerified) {
                openMainPageActivity()
            } else {
                showErrorMessage("Please verify via email")
            }
        }


        // If user is not currently login in yet then continue
        btnSignIn.setOnClickListener {
            if (checkInput()) {
                val emailInput = etEmail.text.toString()
                val passwordInput = etPassword.text.toString()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener {
                        if (it.isSuccessful && FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                            openMainPageActivity()
                        } else {
                            etPassword.text.clear()
                            showErrorMessage(it.exception?.localizedMessage ?: "Please verify your email before signing in")
                        }
                    }
            }
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    // Simple check to see if the input is empty
    private fun checkInput(): Boolean {
        if (etEmail.text.isEmpty()) {
            return showErrorMessage("Email cannot be empty")
        } else if (etPassword.text.isEmpty()) {
            return showErrorMessage("Password cannot be empty")
        }
        return true
    }

    private fun showErrorMessage(s: String): Boolean {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        return false
    }

    // Starts the main activity with additional flags to prevent the user from going back
    // to the login screen when hitting the back button
    private fun openMainPageActivity() {
        startActivity(Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}