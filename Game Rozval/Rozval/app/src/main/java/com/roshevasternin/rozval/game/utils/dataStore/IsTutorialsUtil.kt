package com.roshevasternin.rozval.game.utils.dataStore

import com.roshevasternin.rozval.game.manager.GameDataStoreManager
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IsTutorialsUtil(val coroutine: CoroutineScope) {

    var isTutorials = false
        private set

    init {
        coroutine.launch {
            isTutorials = GameDataStoreManager.IsTutorials.get() ?: true
            log("Store isTutorials = $isTutorials")
        }
    }

    fun update(flag: Boolean) {
        coroutine.launch {
            isTutorials = flag
            log("Store isTutorials update = $isTutorials")
            GameDataStoreManager.IsTutorials.update { isTutorials }
        }
    }

}