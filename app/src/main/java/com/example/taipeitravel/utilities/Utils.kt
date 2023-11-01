package com.example.taipeitravel.utilities

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.taipeitravel.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

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

        fun toast(message: String) {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    App.instance, message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // for memory leak test
        fun gcTest(obj: Any, nameId: String) {
            val weakRef = WeakReference<Any>(obj)

            GlobalScope.launch {
                for (idx in 0 until 100) {
                    Runtime.getRuntime().gc();
                    if (weakRef?.get() == null) {
                        Log.d("gctest", "${nameId} is released!")
                        break
                    }
                    Log.d("gctest", "${nameId} " + idx + ":" + weakRef?.get())
                    delay(1000L)
                }
            }
        }
    }
}