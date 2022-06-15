package com.example.realtimechat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity: AppCompatActivity() {

    private val editTextEmail by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
    }

    private val editTextPassword by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextPassword)
    }

    private val editTextPasswordAgain by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextPasswordAgain)
    }

    private val buttonRegister by lazy {
        findViewById<MaterialButton>(R.id.btn_Register)
    }

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.registerViewEventLiveData.observe(this, { viewEvent ->
            when (viewEvent) {
                RegisterViewModel.RegisterViewEvent.Success -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                is RegisterViewModel.RegisterViewEvent.Error -> {
                    Toast.makeText(baseContext,viewEvent.message,Toast.LENGTH_LONG).show()
                }
            }
        })

        buttonRegister.setOnClickListener {
            if (!validateEmail() || !validatePassword() || !validatePasswordAgain()){
            return@setOnClickListener
        }
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val passwordAgain = editTextPasswordAgain.text.toString()

            viewModel.register(email, password, passwordAgain)

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
        val _editTextPassword : String = editTextPassword.text.toString().trim()
        val checkPassword = Regex("[\\d{10}]")

        return if ( _editTextPassword.isEmpty()) {
            editTextPassword.setError("Field cannot be empty")
            false
        } else if (!_editTextPassword.matches(checkPassword)) {
            editTextPassword.setError("Password should be numbers!")
            false
        } else {
            editTextPassword.setError(null)
            true
        }
    }
    private fun validatePasswordAgain(): Boolean {
        val _editTextPassword : String = editTextPassword.text.toString().trim()
        val _editTextPasswordAgain : String = editTextPasswordAgain.text.toString().trim()

        return if ( _editTextPasswordAgain.isEmpty()) {
            editTextPasswordAgain.setError("Field cannot be empty")
            false
        } else if (_editTextPasswordAgain != _editTextPassword) {
            editTextPasswordAgain.setError("Password should be same!")
            false
        } else {
            editTextPasswordAgain.setError(null)
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == androidx.appcompat.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}