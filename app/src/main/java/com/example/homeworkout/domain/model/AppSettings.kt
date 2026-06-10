package com.example.homeworkout.domain.model

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    HINDI("hi");

    companion object {
        fun fromCode(code: String): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
    }
}

enum class AppTheme {
    DARK,
    LIGHT;

    val isDark: Boolean get() = this == DARK

    companion object {
        fun fromRaw(value: String): AppTheme =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: LIGHT
    }
}

data class AppSettings(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
)
