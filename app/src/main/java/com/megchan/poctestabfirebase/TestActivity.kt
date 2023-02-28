package com.megchan.poctestabfirebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.megchan.poctestabfirebase.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private val preferences by lazy { LocalPreferencesStorage(this) }
    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater).apply { setContentView(root) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startTestAB()
    }

    private fun startTestAB() {
        if (preferences.retrieveConsent()) binding.banner.visibility = View.VISIBLE
        else binding.bottomSheet.visibility = View.VISIBLE
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, TestActivity::class.java)
    }
}