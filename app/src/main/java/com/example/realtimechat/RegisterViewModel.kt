package com.example.realtimechat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterViewModel: ViewModel() {
    sealed class RegisterViewEvent{
        object Success : RegisterViewEvent()

        data class Error(val message: String) : RegisterViewEvent()

    }
    val registerViewEventLiveData = SingleLiveEvent<RegisterViewEvent>()

    private val auth: FirebaseAuth = Firebase.auth

    fun register(email:String, password:String, passwordAgain:String){
        if (password != passwordAgain){
            registerViewEventLiveData.postValue(RegisterViewEvent.Error("Password does not match"))
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    registerViewEventLiveData.postValue(RegisterViewEvent.Success)
                }else{
                    Log.w("LoginActivity","createUserWithEmail:failure",task.exception)
                    registerViewEventLiveData.postValue(RegisterViewEvent.Error("Authentication failed!"))
                }
            }
    }
}