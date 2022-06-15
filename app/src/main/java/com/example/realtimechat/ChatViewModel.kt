package com.example.realtimechat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatViewModel: ViewModel(), FirebaseAuth.AuthStateListener {

    private val auth: FirebaseAuth = Firebase.auth

    val chatListLiveData = MutableLiveData<List<Chat>>()

    val isLoggedInLiveData = MutableLiveData<Boolean>()

    private val chatNodeReference: DatabaseReference =  Firebase.database.reference.child("chats")

    init {
        chatNodeReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val chatList = mutableListOf<Chat>()
                snapshot.children.forEach {
                    val message = it.child("message").getValue<String>() ?: return@forEach
                    val sender = it.child("sender").getValue<String>() ?: return@forEach
                    val chatId = it.key ?: return@forEach
                    val chat = Chat(
                        chatId = chatId,
                        message = message,
                        userName = sender
                    )
                    chatList.add(chat)
                }
                chatListLiveData.postValue(chatList)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        })
        auth.addAuthStateListener(this)

    }
    fun sendMessage(message: String) {
        chatNodeReference.push().apply {
            child("sender").setValue(auth.currentUser?.email?:"Test User")
            child("message").setValue(message)
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        val isLoggedIn = firebaseAuth.currentUser != null
        isLoggedInLiveData.postValue(isLoggedIn)
    }

    fun logOut() {
        auth.signOut()
    }

}

