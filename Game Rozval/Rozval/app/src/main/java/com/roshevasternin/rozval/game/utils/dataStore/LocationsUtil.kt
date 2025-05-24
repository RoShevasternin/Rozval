package com.roshevasternin.rozval.game.utils.dataStore

import com.roshevasternin.rozval.game.data.Level
import com.roshevasternin.rozval.game.data.Location
import com.roshevasternin.rozval.game.manager.GameDataStoreManager
import com.roshevasternin.rozval.game.utils.manager.LocationManager
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocationsUtil(val coroutine: CoroutineScope) {

    private val defaultLocationList = List<Location>(LocationManager.LocationType.entries.size) {
        Location(false, List<Level>(LocationManager.LocationType.entries[it].listLevelNecessaryStars.size) { Level(0, false) })
    }

    var locationsFlow = MutableStateFlow(listOf<Location>())
        private set

    init {
        coroutine.launch {
            val levelStr = GameDataStoreManager.Level.get()
            locationsFlow.value = if (levelStr == null) defaultLocationList else Json.decodeFromString<List<Location>>(levelStr)

            log("Store locations:")
            locationsFlow.value.onEachIndexed { index, location ->
                log("$index. ${LocationManager.LocationType.entries[index].name} = $location")
            }
        }
    }

    fun update(block: (List<Location>) -> List<Location>) {
        coroutine.launch {
            locationsFlow.value = block(locationsFlow.value)

            log("Store locations update:")
            locationsFlow.value.onEachIndexed { index, location ->
                log("$index. ${LocationManager.LocationType.entries[index].name} = $location")
            }
            GameDataStoreManager.Level.update { Json.encodeToString<List<Location>>(locationsFlow.value) }
        }
    }

}