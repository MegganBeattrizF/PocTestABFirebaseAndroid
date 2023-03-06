package com.megchan.poctestabfirebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.megchan.poctestabfirebase.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private val preferences by lazy { LocalPreferencesStorage(this) }
    private val analytics by lazy { Firebase.analytics }
    private val remoteConfig by lazy { Firebase.remoteConfig }
    private val binding by lazy {
        ActivityTestBinding.inflate(layoutInflater).apply { setContentView(root) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRemoteConfig()
        startTestAB()
        setupListeners()
    }

    private fun setupRemoteConfig() {
        remoteConfig.apply {
            val settings = remoteConfigSettings {
                fetchTimeoutInSeconds = 5
                minimumFetchIntervalInSeconds = 3600
            }
            setDefaultsAsync(R.xml.remote_config_defalts)
            setConfigSettingsAsync(settings)
        }
    }

    private fun setupListeners() {
        binding.buttonActivateModal.setOnClickListener { registerActivateEvent() }
        binding.buttonNotActivateModal.setOnClickListener { registerNotActivateEvent() }
        binding.buttonActivateBanner.setOnClickListener { registerActivateEvent() }
        binding.buttonNotActivateBanner.setOnClickListener { registerNotActivateEvent() }
    }

    private fun startTestAB() {
        if (preferences.retrieveAutomaticDebitEnabled().not()) {
            remoteConfig.run {
                fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful.not()) {
                        Log.d("testAB", "erro ao pegar do remoteConfig")
                        return@addOnCompleteListener
                    }

                    val variableFlow = this[CONFIG_KEY_INCENTIVE_VARIABLES].asString()
                    Log.d("testAB", "sucesso ao pegar do remoteConfig a $variableFlow")
                    if (variableFlow == CONFIG_VALUE_BANNER_ENABLED) {
                        binding.banner.visibility = View.VISIBLE
                    } else if (variableFlow == CONFIG_VALUE_MODAL_ENABLED) {
                        binding.bottomSheet.visibility = View.VISIBLE
                    }
                    registerStartEvent()
                }
            }
        }
    }

    private fun registerActivateEvent() {
        analytics.logEvent(ACTIVATED_TEST_AB) {
            Log.d("testAB", "evento enviado: ativado")
        }
    }

    private fun registerNotActivateEvent() {
        analytics.logEvent(NOT_ACTIVATED_TEST_AB) {
            Log.d("testAB", "evento enviado: nao ativado")
        }
    }

    private fun registerStartEvent() {
        analytics.logEvent(START_TEST_AB) {
            Log.d("testAB", "inicio do teste")
        }
    }

    companion object {
        private const val START_TEST_AB = "start_test_ab"
        private const val ACTIVATED_TEST_AB = "debit_activated"
        private const val NOT_ACTIVATED_TEST_AB = "feature_not_activated"
        private const val CONFIG_VALUE_BANNER_ENABLED = "banner_enabled"
        private const val CONFIG_VALUE_MODAL_ENABLED = "modal_enabled"
        private const val CONFIG_KEY_INCENTIVE_VARIABLES = "automatic_debit_incentive_views"
        fun getStartIntent(context: Context) = Intent(context, TestActivity::class.java)
    }
}