package com.batodev.arrows.engine

import androidx.compose.ui.geometry.Offset
import com.batodev.arrows.GameConstants
import com.batodev.arrows.GameConstants.DEFAULT_TOLERANCE
import com.batodev.arrows.GameConstants.TAP_AREA_OFFSET_FACTOR
import kotlin.math.min

class InputHandler {
    
    fun transformTapToGrid(params: TapTransformationParams): Offset {
        // Inverse graphicsLayer transformation
        val centerX = params.containerWidth / 2
        val centerY = params.containerHeight / 2
        val transformedX = (params.tapOffset.x - params.offsetX - centerX) / params.scale + centerX
        val transformedY = (params.tapOffset.y - params.offsetY - centerY) / params.scale + centerY

        // Centered board bounds
        val cellSize = min(params.containerWidth / params.level.width, params.containerHeight / params.level.height)
        val boardWidth = cellSize * params.level.width
        val boardHeight = cellSize * params.level.height
        val leftOffset = (params.containerWidth - boardWidth) / 2
        val topOffset = (params.containerHeight - boardHeight) / 2

        // Grid coordinates
        val cellX = (transformedX - leftOffset) / cellSize
        val cellY = (transformedY - topOffset) / cellSize

        return Offset(cellX, cellY)
    }

    fun findTappedSnake(cellX: Float, cellY: Float, snakes: List<Snake>, isObstructed: (Snake) -> Boolean): Snake? {
        return snakes
            .map { snake ->
                // Compute minimum squared distance from tap to any body segment (prefer head offset)
                var minDistSq = Float.POSITIVE_INFINITY

                snake.body.forEachIndexed { index, point ->
                    val baseCenterX = point.x + GameConstants.CELL_CENTER
                    val baseCenterY = point.y + GameConstants.CELL_CENTER

                    // For the head, bias the tap area slightly in the head direction (as before)
                    val centerX = if (index == 0) baseCenterX + snake.headDirection.dx * TAP_AREA_OFFSET_FACTOR else baseCenterX
                    val centerY = if (index == 0) baseCenterY + snake.headDirection.dy * TAP_AREA_OFFSET_FACTOR else baseCenterY

                    val dx = centerX - cellX
                    val dy = centerY - cellY
                    val distSq = dx * dx + dy * dy

                    if (distSq < minDistSq) minDistSq = distSq
                }

                Triple(snake, minDistSq, isObstructed(snake))
            }
            .filter { it.second <= DEFAULT_TOLERANCE * DEFAULT_TOLERANCE }
            .minWithOrNull(compareBy({ it.third }, { it.second }))
            ?.first
    }
}
