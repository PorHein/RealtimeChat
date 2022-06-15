package com.example.realtimechat

import android.R.attr.phoneNumber
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class LoginActivity: AppCompatActivity() {

    private val editTextEmail by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
    }
    private val editTextPassword by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextPassword)
    }
    private val buttonLogin by lazy {
        findViewById<MaterialButton>(R.id.btn_Login)
    }
    private val buttonRegister by lazy {
        findViewById<MaterialButton>(R.id.btn_Register)
    }
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewModel.loginViewEventLiveData.observe(this, { event ->
            when (event) {
                LoginViewModel.LoginViewEvent.Success -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                is LoginViewModel.LoginViewEvent.Error -> {
                    Toast.makeText(baseContext,event.message,Toast.LENGTH_LONG).show()
                }
            }
        })

        buttonLogin.setOnClickListener {
            if (!validateEmail() || !validatePassword()){
                return@setOnClickListener
            }
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            viewModel.login(email, password)
        }
    }

    private fun validateEmail(): Boolean {
        val _editTextEmail  = editTextEmail.text.toString().trim()
        val checkEmail = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return if (_editTextEmail.isEmpty()) {
            editTextEmail.setError("Field cannot be empty")
            false
        } else if (!_editTextEmail.matches(checkEmail)) {
            editTextEmail.setError("Invalid Email!")
            false
        } else {
            editTextEmail.setError(null)
            true
        }
    }

    private fun validatePassword(): Boolean {
        val _editTextPassword = editTextPassword.text.toString().trim()
        val checkPassword = Regex("\\d{10}")

        return if ( _editTextPassword.isEmpty()) {
            editTextPassword.setError("Field cannot be empty")
            false
        } else if (!_editTextPassword.matches(checkPassword)) {
            editTextPassword.setError("No white spaces are allowed!")
            false
        } else {
            editTextPassword.setError(null)
            true
        }
    }
}