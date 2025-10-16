package com.flowpulse.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flowpulse_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val LAST_ERROR_EXECUTION_ID = longPreferencesKey("last_error_execution_id")
        val THEME = stringPreferencesKey("theme")
        val LANGUAGE = stringPreferencesKey("language")
        val POLLING_INTERVAL_MIN = intPreferencesKey("poll_interval_min")
    }

    fun lastErrorExecutionId(instanceId: Long): Flow<Long> = context.dataStore.data.map {
        it[longPreferencesKey("last_error_execution_id_$instanceId")] ?: 0L
    }

    suspend fun setLastErrorExecutionId(instanceId: Long, id: Long) {
        context.dataStore.edit { prefs ->
            prefs[longPreferencesKey("last_error_execution_id_$instanceId")] = id
        }
    }

    fun theme(): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.THEME] ?: "system"
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs -> prefs[Keys.THEME] = theme }
    }

    fun language(): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.LANGUAGE] ?: "es"
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { prefs -> prefs[Keys.LANGUAGE] = language }
    }

    fun pollingInterval(): Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.POLLING_INTERVAL_MIN] ?: com.flowpulse.app.BuildConfig.POLL_INTERVAL_MINUTES
    }

    suspend fun setPollingInterval(minutes: Int) {
        context.dataStore.edit { prefs -> prefs[Keys.POLLING_INTERVAL_MIN] = minutes }
    }
}
