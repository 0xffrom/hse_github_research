@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package hse_github_research.core.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified Data> parseJsonData(fileName: String): Data =
    with(withContext(Dispatchers.IO) { FileInputStream(fileName.formatJsonFileName()) }) {
        Json.decodeFromStream(this)
    }

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified Data> saveJsonData(fileName: String, data: Data) {
    with(withContext(Dispatchers.IO) { FileOutputStream(fileName.formatJsonFileName()) }) {
        Json.encodeToStream(data, this)
    }
}

fun String.formatJsonFileName(): String {
    val formattedString = if (endsWith(".json")) this else "$this.json"

    return if (startsWith("data/")) return formattedString else "data/$formattedString"
}
