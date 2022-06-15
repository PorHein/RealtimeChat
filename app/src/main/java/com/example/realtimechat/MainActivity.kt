package com.example.realtimechat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val rvChat by lazy {
        findViewById<RecyclerView>(R.id.rvChat)
    }
    private val editTextMessage by lazy {
        findViewById<EditText>(R.id.editTextMessage)
    }
    private val buttonSend by lazy {
        findViewById<Button>(R.id.btn_Send)
    }
    private val chatRecyclerViewAdapter = ChatRecyclerViewAdapter()

    private lateinit var auth: FirebaseAuth

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_main)
        viewModel.chatListLiveData.observe(this) { chatList ->
            chatRecyclerViewAdapter.submitList(chatList)
        }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvChat.layoutManager = linearLayoutManager

        rvChat.apply {
            adapter = chatRecyclerViewAdapter
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }

        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            viewModel.sendMessage(message)
            editTextMessage.text.clear()
        }

        viewModel.isLoggedInLiveData.observe(this, { isLoggedIn ->
            if (isLoggedIn) {
                return@observe
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logOut) {
            viewModel.logOut()
        }
        return super.onOptionsItemSelected(item)
    }


    //SHA1: 28:E0:D1:29:6E:2F:14:F9:69:D1:A7:3E:91:F7:76:71:58:B8:6D:90
    //SHA256: 0D:45:6A:D7:09:FC:B6:5D:77:D0:BE:80:11:FA:84:4B:39:21:13:A9:24:50:41:61:CE:ED:84:C2:69:A3:5E:E7

}