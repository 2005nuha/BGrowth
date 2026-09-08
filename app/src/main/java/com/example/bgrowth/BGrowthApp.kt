package com.example.bgrowth

import android.app.Application
import com.example.bgrowth.data.session.SessionManager

class BGrowthApp : Application() {

    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        sessionManager = SessionManager(this)
    }

    companion object {
        // Accessed by RetrofitClient and AuthRepository to reach SessionManager
        // without requiring a DI framework.
        lateinit var instance: BGrowthApp
            private set
    }
}
