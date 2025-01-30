package com.shadow.deepseekimp.data.service

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

    fun fetchAndActivate(onDone: (deep:String?, qwen:String?) -> Unit) {
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val tokenDeep = remoteConfig.getString(DEEP_SEEKER_CONFIG)
            val tokenQwen = remoteConfig.getString(QWEN_CONFIG)
            onDone.invoke(tokenDeep, tokenQwen)
        }.addOnFailureListener {
            onDone.invoke(null, null)
        }
    }

    private companion object {
        const val DEEP_SEEKER_CONFIG = "DEEP_KEY"
        const val QWEN_CONFIG = "QWEN_KEY"
    }
}