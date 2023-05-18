package com.reggya.faintek.core.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("uid")
    val uid: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("verify")
    val verify: Boolean? = null,

    @field:SerializedName("email")
    val email: String? = null
)
