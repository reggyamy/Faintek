package com.reggya.faintek

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("users")
    val users : List<User>
)