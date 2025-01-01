package com.aghajari.inappbillingbazaar.ui.content

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf

abstract class BasePoolakeyActivity : ComponentActivity() {

    private val status = mutableStateOf("Not Connected")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(
                status = status.value,
                onClickSKU10KIRR = ::onClickSKU10KIRR,
                onClickSKU20KIRR = ::onClickSKU20KIRR,
            )
        }
    }

    fun updateStatus(status: String) {
        this.status.value = status
        Log.d("In-App-Billing-Bazaar", "New Status: $status")
    }

    abstract fun onClickSKU10KIRR()

    abstract fun onClickSKU20KIRR()
}
