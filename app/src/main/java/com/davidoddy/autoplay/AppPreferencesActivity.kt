package com.davidoddy.autoplay

import android.app.Activity
import android.os.Bundle

class AppPreferencesActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, AppPreferencesFragment())
                .commit()    }
}
