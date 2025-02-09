package com.shadow.deepseekimp.data

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoveConfigureController @Inject constructor() {

    private val remoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        setConfigSettingsAsync(configSettings)
    }

    fun fetchAndActivate(onDone: () -> Unit) {
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            Log.i("RemoveConfigureController", "Remote config fetched and activated")
            Log.i(
                "RemoveConfigureController",
                "Deep seeker config: ${remoteConfig.getString(DEEP_SEEKER_CONFIG)}"
            )
            onDone.invoke()
        }
    }

    private companion object {
        const val DEEP_SEEKER_CONFIG = "DEEP_KEY"
    }
}