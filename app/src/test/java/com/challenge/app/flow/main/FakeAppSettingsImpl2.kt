package com.challenge.app.flow.main

import com.challenge.app.repository.local.AppSettings

class FakeAppSettingsImpl2 : AppSettings {
    override fun putBoolean(key: String, value: Boolean) {}

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return false
    }

    override fun clear() {}
}