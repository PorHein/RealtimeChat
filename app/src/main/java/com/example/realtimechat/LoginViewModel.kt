package com.example.realtimechat

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel() {

    sealed class LoginViewEvent{
        object Success : LoginViewEvent()

        data class Error(val message: String) : LoginViewEvent()

    }
    val loginViewEventLiveData = SingleLiveEvent<LoginViewEvent>()

    private val auth: FirebaseAuth = Firebase.auth

    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    loginViewEventLiveData.postValue(LoginViewEvent.Success)
                }else{
                    Log.w("LoginActivity","createUserWithEmail:failure",task.exception)
                    loginViewEventLiveData.postValue(LoginViewEvent.Error("Authentication failed!"))
                }
            }
    }
}