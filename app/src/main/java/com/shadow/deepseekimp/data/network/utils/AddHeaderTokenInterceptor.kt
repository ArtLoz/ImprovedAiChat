package com.shadow.deepseekimp.data.network.utils

import android.util.Log
import com.shadow.deepseekimp.BuildConfig
import com.shadow.deepseekimp.data.datastore.DataStoreHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AddHeaderTokenInterceptor @Inject constructor(
    private val dataStoreHelper: DataStoreHelper
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestUrl = chain.request().url.host
        Log.d("AddHeaderTokenInterceptor", "requestUrl: $requestUrl")
        val token = runBlocking {
            if (BuildConfig.BASE_URL_DEEP.contains(requestUrl)) {
                dataStoreHelper.getTokenForDeepSeek()
            } else {
                dataStoreHelper.getTokenForQwen()
            }
        }
        Log.d("AddHeaderTokenInterceptor", "token: $token")
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}