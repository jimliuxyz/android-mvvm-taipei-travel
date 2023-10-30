package com.example.taipeitravel.utilities

import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.taipeitravel.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Utils {
    companion object {

        fun getLangForWebapi(): String {
            val locales = AppCompatDelegate.getApplicationLocales()
            val lang = locales.get(0)?.toString()?.lowercase() ?: "en"
            return when (lang) {
                "rtw" -> "zh-tw"
                "zh" -> "zh-cn"
                else -> lang
            }
        }

        fun getLangForDisplay(lang: String): String {
            return when (lang) {
                "zh-tw" -> "繁體中文"
                "zh-cn" -> "简体中文"
                "en" -> "English"
                "ja" -> "日本語"
                "ko" -> "한국어"
                "es" -> "Español"
                "id" -> "Indonesia"
                "th" -> "แบบไทย"
                "vi" -> "Tiếng Việt"
                else -> lang
            }
        }

        fun setAppLocales(lang: String) {
            var tag = lang
            tag = if (tag == "zh-tw") "zh-rTW" else tag
            tag = if (tag == "zh-cn") "zh" else tag

            val appLocale = LocaleListCompat.forLanguageTags(tag)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        suspend fun toast(message: String) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    App.instance, message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}