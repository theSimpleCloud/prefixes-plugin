package app.simplecloud.plugin.prefixes.shared

import app.simplecloud.plugin.prefixes.api.PrefixesConfig
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.FileReader

class PrefixesConfigParser<C : PrefixesConfig>(private val configFile: File) {
    fun parse(type: Class<C>, default: C): C {
        if (!configFile.exists()) return default
        val gson = Gson()
        val reader = JsonReader(FileReader(configFile))
        return gson.fromJson(reader, type)
    }
}