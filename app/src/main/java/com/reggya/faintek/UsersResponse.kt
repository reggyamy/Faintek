package com.reggya.faintek

import com.google.gson.annotations.SerializedName

data class UsersResponse(

	@field:SerializedName("UsersResponse")
	val usersResponse: List<User?>? = null
)

