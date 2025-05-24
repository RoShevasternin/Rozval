package com.roshevasternin.rozval.game.utils.dataStore

import com.roshevasternin.rozval.game.manager.GameDataStoreManager
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class KeyUtil(val coroutine: CoroutineScope) {

    var keyFlow = MutableStateFlow(0)
        private set

    init {
        coroutine.launch {
            keyFlow.value = GameDataStoreManager.Key.get() ?: 0
            log("Store key = ${keyFlow.value}")
        }
    }

    fun update(block: (Int) -> Int) {
        coroutine.launch {
            keyFlow.value = block(keyFlow.value)

            log("Store key update = ${keyFlow.value}")
            GameDataStoreManager.Key.update { keyFlow.value }
        }
    }

}