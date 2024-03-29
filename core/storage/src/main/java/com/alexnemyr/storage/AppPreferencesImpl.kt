package com.alexnemyr.storage

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesImpl(context: Context) : AppPreferences {

    init {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    override fun putName(name: String) {
        preferences.edit {
            it.putString(NAME_KEY, name)
            it.commit()
        }
    }

    override fun putDate(name: String) {
        preferences.edit {
            it.putString(DATE_KEY, name)
            it.commit()
        }
    }

    override fun putUri(name: String) {
        preferences.edit {
            it.putString(URI_KEY, name)
            it.commit()
        }
    }

    override val name: String?
        get() = preferences.getString(NAME_KEY, "")
    override val date: String?
        get() = preferences.getString(DATE_KEY, "")
    override val uri: String?
        get() = preferences.getString(URI_KEY, "")


    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    companion object {
        private const val NAME = "app_preferences"
        private const val MODE = Context.MODE_PRIVATE
        private lateinit var preferences: SharedPreferences

        private const val NAME_KEY = "USER_NAME"
        private const val DATE_KEY = "USER_DATE"
        private const val URI_KEY = "USER_URI"
    }
}