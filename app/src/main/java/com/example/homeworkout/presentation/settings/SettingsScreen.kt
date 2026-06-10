package com.example.homeworkout.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homeworkout.R
import com.example.homeworkout.domain.model.AppLanguage
import com.example.homeworkout.domain.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SettingsSectionCard(
                icon = Icons.Default.Language,
                title = stringResource(R.string.settings_language),
                subtitle = stringResource(R.string.settings_language_desc),
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = state.language == AppLanguage.ENGLISH,
                        onClick = { viewModel.onIntent(SettingsIntent.LanguageSelected(AppLanguage.ENGLISH)) },
                        label = { Text(stringResource(R.string.language_english)) },
                    )
                    FilterChip(
                        selected = state.language == AppLanguage.HINDI,
                        onClick = { viewModel.onIntent(SettingsIntent.LanguageSelected(AppLanguage.HINDI)) },
                        label = { Text(stringResource(R.string.language_hindi)) },
                    )
                }
            }

            SettingsSectionCard(
                icon = Icons.Default.DarkMode,
                title = stringResource(R.string.settings_theme),
                subtitle = stringResource(R.string.settings_theme_desc),
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = state.theme == AppTheme.DARK,
                        onClick = { viewModel.onIntent(SettingsIntent.ThemeSelected(AppTheme.DARK)) },
                        label = { Text(stringResource(R.string.theme_dark)) },
                        leadingIcon = {
                            Icon(Icons.Default.DarkMode, contentDescription = null,
                                modifier = Modifier.padding(start = 4.dp))
                        },
                    )
                    FilterChip(
                        selected = state.theme == AppTheme.LIGHT,
                        onClick = { viewModel.onIntent(SettingsIntent.ThemeSelected(AppTheme.LIGHT)) },
                        label = { Text(stringResource(R.string.theme_light)) },
                        leadingIcon = {
                            Icon(Icons.Default.LightMode, contentDescription = null,
                                modifier = Modifier.padding(start = 4.dp))
                        },
                    )
                }
            }

            PlaceholderSettingCard(
                icon = Icons.Default.MusicNote,
                title = stringResource(R.string.settings_sound),
                subtitle = stringResource(R.string.settings_sound_desc),
            )
            PlaceholderSettingCard(
                icon = Icons.Default.Timer,
                title = stringResource(R.string.settings_rest_timer),
                subtitle = stringResource(R.string.settings_rest_timer_desc),
            )
            PlaceholderSettingCard(
                icon = Icons.Default.Tune,
                title = stringResource(R.string.settings_units),
                subtitle = stringResource(R.string.settings_units_desc),
            )
        }
    }
}

@Composable
private fun SettingsSectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            content()
        }
    }
}

@Composable
private fun PlaceholderSettingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
