package com.example.homeworkout.data.local

import android.content.Context
import com.example.homeworkout.data.local.dto.WorkoutCatalogDto
import kotlinx.serialization.json.Json

class WorkoutAssetDataSource(
    context: Context,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    val catalog: WorkoutCatalogDto by lazy {
        context.assets.open("workouts.json").bufferedReader().use { reader ->
            json.decodeFromString<WorkoutCatalogDto>(reader.readText())
        }
    }
}
