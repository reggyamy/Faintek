package com.reggya.faintek.core.data.model

import com.google.gson.annotations.SerializedName

data class UsersResponse(

	@field:SerializedName("UsersResponse")
	val usersResponse: List<User?>? = null
)

