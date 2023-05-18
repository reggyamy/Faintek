package com.reggya.faintek.utils

import android.content.Context

class UserPreference(context: Context) {
    companion object{
        private const val KEY_PREFS = "user_pref"
        const val KEY_ID = "id_user"
        private const val ISLOGIN = "false"
    }

    private val preference = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)

    fun removeAll(){
        val editor = preference.edit()
        editor.clear()
        editor.apply()
    }

    fun setLogin(userId: String){
        val editor = preference.edit()
        editor.putString(KEY_ID, userId)
        editor.apply()
    }

    fun getUserId() = preference.getString(KEY_ID, "")

    fun isLogin(state : Boolean){
        val editor = preference.edit()
        editor.putBoolean(ISLOGIN, state)
            .apply()
    }

    fun getIsLogin(): Boolean{
        return preference.getBoolean(ISLOGIN, false)
    }
}
