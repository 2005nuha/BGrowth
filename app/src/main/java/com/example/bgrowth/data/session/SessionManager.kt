package com.example.bgrowth.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun saveAccessToken(accessToken: String) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(
            KEY_ACCESS_TOKEN,
            null
        )
    }

    fun getRefreshToken(): String? {
        return prefs.getString(
            KEY_REFRESH_TOKEN,
            null
        )
    }

    fun hasSession(): Boolean {
        return !getAccessToken().isNullOrBlank() &&
                !getRefreshToken().isNullOrBlank()
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    /*
     * مؤقتًا للحفاظ على توافق الكود القديم.
     * لاحقًا ممكن نحذفها بعد تحديث كل الاستخدامات.
     */
    fun getToken(): String? = getAccessToken()

    fun clearAll() = clearSession()

    fun isLoggedIn(): Boolean = hasSession()

    private companion object {

        const val PREFS_NAME =
            "bgrowth_session"

        const val KEY_ACCESS_TOKEN =
            "auth_access_token"

        const val KEY_REFRESH_TOKEN =
            "auth_refresh_token"
    }
}