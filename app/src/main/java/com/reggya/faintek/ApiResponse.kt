package com.reggya.faintek

data class ApiResponse<out T>(val type: ApiResponseType, val data: T?, val message: String?){
    companion object{
        fun empty() = ApiResponse(ApiResponseType.EMPTY, null, null)
        fun error(message: String?) = ApiResponse(ApiResponseType.ERROR, null, message)
        fun <T> success(value: T) = ApiResponse(ApiResponseType.SUCCESS, value, null)
    }
}
