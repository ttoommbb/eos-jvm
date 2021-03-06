package com.memtrip.eos.chain.actions.query

import com.memtrip.eos.chain.actions.query.producer.GetBlockProducers
import com.memtrip.eos.http.rpc.Api
import junit.framework.TestCase.assertFalse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(JUnitPlatform::class)
class GetBlockProducersTest : Spek({

    given("an Api") {

        val okHttpClient by memoized {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
        }

        val chainApi by memoized { Api("http://api.eosnewyork.io/", okHttpClient).chain }

        on("v1/chain/get_producers") {

            val response = GetBlockProducers(chainApi).getProducers(50).blockingGet()

            it("should return the transaction") {
                response.map { blockProducer ->
                    assertFalse(blockProducer.apiEndpoint.isEmpty())
                }
            }
        }
    }
})