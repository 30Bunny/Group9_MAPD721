package com.mapd.group9_mapd721.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bansidholakiya_mapd721_test.datastore.DataStoreManager
import com.mapd.group9_mapd721.MainActivity
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfilePage() {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    logout(context)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 16.dp
            ),
            modifier = Modifier.fillMaxWidth()
                .height(56.dp) ,
        ) {
            Text(text = "Logout")
        }
    }
}

suspend fun logout(context: Context){
    val dataStore = DataStoreManager(context)
    dataStore.clearDS()

    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}