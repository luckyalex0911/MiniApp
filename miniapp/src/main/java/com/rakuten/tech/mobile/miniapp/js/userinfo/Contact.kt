package com.rakuten.tech.mobile.miniapp.js.userinfo

import androidx.annotation.Keep

/** Contact object for miniapp. */
@Keep
data class Contact(
    val id: String,
    var name: String?,
    var email: String?,
    var allEmailList: List<String>?
)
