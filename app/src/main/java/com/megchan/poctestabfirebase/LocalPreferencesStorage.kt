package com.megchan.poctestabfirebase

import android.app.Activity
import android.content.Context

class LocalPreferencesStorage(
    private val activity: Activity
) {

    private val preference by lazy {
        activity.getSharedPreferences(
            "DEBIT_CONSENT",
            Context.MODE_PRIVATE
        )
    }

    fun savePreference(isGranted: Boolean) = preference.edit()
        .putBoolean("consent", isGranted)
        .apply()


    fun retrieveConsent(): Boolean = preference.getBoolean("consent", false)

}