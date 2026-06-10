package com.example.homeworkout.util

import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.core.os.LocaleListCompat
import com.example.homeworkout.domain.model.AppLanguage
import java.util.Locale

object LocaleHelper {
    /**
     * Persists the locale for the next cold start via AppCompat.
     * Avoid calling this on runtime language changes — it recreates the activity.
     */
    fun syncSystemLocale(language: AppLanguage) {
        val desiredTags = LocaleListCompat.forLanguageTags(language.code).toLanguageTags()
        val currentTags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        if (currentTags != desiredTags) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.code))
        }
    }
}

@Composable
fun ProvideAppLocale(
    language: AppLanguage,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val configuration = remember(language) {
        Configuration(context.resources.configuration).apply {
            setLocale(Locale.forLanguageTag(language.code))
        }
    }
    val localizedContext = remember(configuration, context) {
        val configurationContext = context.createConfigurationContext(configuration)
        object : ContextWrapper(context) {
            override fun getResources(): Resources = configurationContext.resources
            override fun getAssets(): AssetManager = configurationContext.assets
        }
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides configuration,
        LocalResources provides localizedContext.resources,
    ) {
        content()
    }
}
