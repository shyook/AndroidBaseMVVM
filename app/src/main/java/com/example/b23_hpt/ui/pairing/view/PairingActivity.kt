package com.example.b23_hpt.ui.pairing.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.b23_hpt.core.utils.NetworkUtils
import com.example.b23_hpt.databinding.ActivityPairingBinding

class PairingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPairingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPairingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() {
        val wifiEnabled = NetworkUtils.isWifiEnabled()
        val wifiConnected = NetworkUtils.isConnectedWifi()
    }

    private fun initViewModel() {

    }
}