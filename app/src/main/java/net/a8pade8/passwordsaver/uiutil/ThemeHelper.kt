package net.a8pade8.passwordsaver.uiutil

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

object ThemeHelper {

    private const val THEME_PREF_KEY = "theme_mode"
    private const val THEME_SYSTEM = "system"
    private const val THEME_LIGHT = "light"
    private const val THEME_DARK = "dark"

    /**
     * Применяет сохранённую тему к активности.
     * Должен вызываться перед setContentView() в onCreate().
     */
    fun applyTheme(activity: AppCompatActivity) {
        val themeMode = getThemeMode(activity)
        setAppTheme(themeMode)
    }

    /**
     * Применяет тему к контексту приложения (для Service, BroadcastReceiver и т.д.)
     */
    fun applyTheme(context: Context) {
        val themeMode = getThemeMode(context)
        setAppTheme(themeMode)
    }

    /**
     * Возвращает текущий режим темы из SharedPreferences.
     */
    fun getThemeMode(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(THEME_PREF_KEY, THEME_SYSTEM) ?: THEME_SYSTEM
    }

    /**
     * Устанавливает тему приложения в зависимости от строкового значения.
     */
    fun setAppTheme(themeMode: String) {
        when (themeMode) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    /**
     * Сохраняет выбранную тему в настройки.
     */
    fun saveThemeMode(context: Context, themeMode: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(THEME_PREF_KEY, themeMode).apply()
        setAppTheme(themeMode)
    }
}