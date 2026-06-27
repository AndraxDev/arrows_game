package com.batodev.arrows.ui.game

import com.batodev.arrows.WinCelebrationResources
import kotlin.random.Random

data class CelebrationContent(val labelResId: Int)

object CelebrationContentSelector {

    fun selectContent(random: Random = Random): CelebrationContent {
        val label = WinCelebrationResources.CONGRATULATION_LABELS.random(random)
        return CelebrationContent(labelResId = label)
    }
}
