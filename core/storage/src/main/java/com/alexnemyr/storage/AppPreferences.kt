package com.alexnemyr.storage

interface AppPreferences {
    val test: String
    fun putName(name: String)
    fun putDate(name: String)
    fun putUri(name: String)
    val name: String?
    val date: String?
    val uri: String?
}