package com.roshevasternin.rozval.game.utils.dataStore

import com.roshevasternin.rozval.game.manager.GameDataStoreManager
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StarUtil(val coroutine: CoroutineScope) {

    var starFlow = MutableStateFlow(0)
        private set

    init {
        coroutine.launch {
            starFlow.value = GameDataStoreManager.Star.get() ?: 0
            log("Store star = ${starFlow.value}")
        }
    }

    fun update(block: (Int) -> Int) {
        coroutine.launch {
            starFlow.value = block(starFlow.value)

            log("Store star update = ${starFlow.value}")
            GameDataStoreManager.Star.update { starFlow.value }
        }
    }

}