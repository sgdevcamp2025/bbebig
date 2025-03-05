package com.smilegate.bbebig.data.datasource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.smilegate.bbebig.data.model.LoginInfoDataModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocalDataSource {
    private val sharedPreferences: SharedPreferences by lazy {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    /**
     * Int
     */
    @Synchronized
    private fun putInt(key: String, value: Int) {
        Log.d(TAG, "putInt: key: $key, value: $value")
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    @Synchronized
    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue).also {
            Log.d(TAG, "getInt: key: $key, value: $it")
        }
    }

    /**
     * Long
     */
    @Synchronized
    private fun putLong(key: String, value: Long) {
        Log.d(TAG, "putLong: key: $key, value: $value")
        sharedPreferences.edit {
            putLong(key, value)
        }
    }

    @Synchronized
    private fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue).also {
            Log.d(TAG, "getLong: key: $key, value: $it")
        }
    }

    /**
     * String
     */
    @Synchronized
    private fun putString(key: String, value: String?) {
        Log.d(TAG, "putString: key: $key, value: $value")
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return getStringOrNull(key, defaultValue) ?: defaultValue
    }

    @Synchronized
    private fun getStringOrNull(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue).also {
            Log.d(TAG, "getStringOrNull: key: $key, value: $it")
        }
    }

    /**
     * Boolean
     */
    @Synchronized
    private fun putBoolean(key: String, value: Boolean) {
        Log.d(TAG, "putBoolean: key: $key, value: $value")
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    @Synchronized
    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue).also {
            Log.d(TAG, "getBoolean: key: $key, value: $it")
        }
    }

    private fun remove(key: String) {
        return sharedPreferences.edit {
            remove(key)
        }
    }

    override fun removeAll() {
        sharedPreferences.edit {
            clear()
        }
    }

    override fun saveToken(accessToken: String, refreshToken: String) {
        putString(KEY_ACCESS_TOKEN, accessToken)
        putString(KEY_REFRESH_TOKEN, refreshToken)
    }

    override fun removeToken() {
        remove(KEY_ACCESS_TOKEN)
        remove(KEY_REFRESH_TOKEN)
    }

    override fun getAccessToken(): String? {
        return getStringOrNull(KEY_ACCESS_TOKEN)
    }

    override fun getRefreshToken(): String? {
        return getStringOrNull(KEY_REFRESH_TOKEN)
    }

    override fun saveLoginInfo(userInfo: LoginInfoDataModel) {
        putString(KEY_USER_EMAIL, userInfo.email)
        putString(KEY_USER_PASSWORD, userInfo.password)
    }

    override fun loadLoginInfo(): LoginInfoDataModel {
        return LoginInfoDataModel(
            email = getString(KEY_USER_EMAIL, ""),
            password = getString(KEY_USER_PASSWORD, ""),
        )
    }

    companion object {
        private const val TAG = "preferences"
        private const val PREF_NAME = "bbebig_preferences"
        private const val KEY_ACCESS_TOKEN = "key_access_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
        private const val KEY_USER_EMAIL = "key_user_name"
        private const val KEY_USER_PASSWORD = "key_user_password"
    }
}
