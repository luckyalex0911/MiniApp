package com.rakuten.tech.mobile.miniapp.api

import com.google.gson.Gson
import com.rakuten.tech.mobile.miniapp.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEndWith
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class AppInfoApiSpec private constructor(
    internal val mockServer: MockWebServer
) : MockWebServerBaseSpec(mockServer) {

    constructor() : this(MockWebServer())

    private lateinit var baseUrl: String
    internal lateinit var retrofit: Retrofit

    @Before
    fun baseSetup() {
        baseUrl = mockServer.url("/").toString()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    internal fun createResponse(
        id: String = TEST_MA_ID,
        displayName: String = TEST_MA_DISPLAY_NAME,
        icon: String = TEST_MA_ICON,
        version: Version = Version(TEST_MA_VERSION_TAG, TEST_MA_VERSION_ID)
    ): MockResponse = MockResponse().setBody(
        "[${Gson().toJson(
            hashMapOf(
                "id" to id,
                "displayName" to displayName,
                "icon" to icon,
                "version" to version
            )
        )}]"
    )
}

class AppInfoApiRequestSpec : AppInfoApiSpec() {

    @Test
    fun `should fetch mini apps using the 'miniapps' endpoint`() {
        executeListingCallByRetrofit()
        val requestUrl = mockServer.takeRequest().requestUrl!!

        requestUrl.encodedPath shouldEndWith "miniapps/"
    }

    private fun executeListingCallByRetrofit() {
        mockServer.enqueue(createResponse())
        retrofit.create(AppInfoApi::class.java)
            .list(hostId = TEST_HA_ID_PROJECT)
            .execute()
    }
}

class AppInfoApiResponseSpec : AppInfoApiSpec() {

    private lateinit var miniAppInfo: MiniAppInfo

    @Before
    fun setup() {
        mockServer.enqueue(createResponse())

        miniAppInfo = retrofit.create(AppInfoApi::class.java)
            .list(hostId = TEST_HA_ID_PROJECT)
            .execute().body()!![0]
    }

    @Test
    fun `should parse the 'id' from response`() {
        miniAppInfo.id shouldBeEqualTo TEST_MA_ID
    }

    @Test
    fun `should parse the 'displayName' from response`() {
        miniAppInfo.displayName shouldBeEqualTo TEST_MA_DISPLAY_NAME
    }

    @Test
    fun `should parse the 'icon' from response`() {
        miniAppInfo.icon shouldBeEqualTo TEST_MA_ICON
    }

    @Test
    fun `should parse the 'versionTag' from response`() {
        miniAppInfo.version.versionTag shouldContain TEST_MA_VERSION_TAG
    }

    @Test
    fun `should parse the 'versionId' from response`() {
        miniAppInfo.version.versionId shouldContain TEST_MA_VERSION_ID
    }
}
