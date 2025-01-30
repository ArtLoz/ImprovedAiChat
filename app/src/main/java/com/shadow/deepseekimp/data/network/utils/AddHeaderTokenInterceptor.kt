package com.shadow.deepseekimp.data.network.utils

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
        val token = runBlocking {
            if (requestUrl.contains(BuildConfig.BASE_URL_DEEP)) {
                dataStoreHelper.getTokenForDeepSeek()
            } else {
                dataStoreHelper.getTokenForQwen()
            }
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}