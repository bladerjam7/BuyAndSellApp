package com.bladerco.buyandsellapp.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bladerco.buyandsellapp.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etVerifyEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etVerifyPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etEmail = findViewById(R.id.et_email_sign_up)
        etVerifyEmail = findViewById(R.id.et_email_sign_up_verify)
        etPassword = findViewById(R.id.et_password_sign_up)
        etVerifyPassword = findViewById(R.id.et_password_sign_up_verify)
        btnSignUp = findViewById(R.id.btn_sign_up)

        btnSignUp.setOnClickListener {
            if (checkInput()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.text.toString().trim(), etPassword.text.toString().trim())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            if(FirebaseAuth.getInstance().currentUser?.isEmailVerified == true){
                                showErrorMessage("is verified")
                                finish()
                            } else {
                                FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                                showErrorMessage("New User Created")
                                finish()
                            }
                        } else{
                            showErrorMessage("Sign up failed. Check your email format")
                        }
                    }
                //finish()
            }

        }

    }


    private fun checkInput(): Boolean {
        when {
            etEmail.text.isEmpty() -> {
                return showErrorMessage("Email cannot be empty")
            }
            etPassword.text.isEmpty() -> {
                return showErrorMessage("Password cannot be empty")
            }
            etEmail.text.toString().trim() != etVerifyEmail.text.toString().trim() -> {
                return showErrorMessage("Emails do not match")
            }
            etPassword.text.toString().trim() != etVerifyPassword.text.toString().trim() -> {
                return showErrorMessage("Passwords do not match")
            }
            etPassword.text.toString().length < 6 -> {
                return showErrorMessage("Password needs to be at least 6 characters or more")
            }
        }
        return true
    }

    private fun showErrorMessage(s: String): Boolean {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        return false
    }
}