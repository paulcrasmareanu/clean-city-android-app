package com.upt.cleancity.service.common

import android.content.Context
import com.upt.cleancity.R
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object CustomHttpClient {

    fun getCustomHttpClientBuilder(context: Context): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val certificateInput = context.resources.openRawResource(R.raw.my_certificate)
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val ca = certificateFactory.generateCertificate(certificateInput)

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        val trustManager = MyManager()

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        return OkHttpClient.Builder()
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(logging)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
    }

}