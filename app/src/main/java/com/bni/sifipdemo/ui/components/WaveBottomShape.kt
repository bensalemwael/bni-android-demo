package com.bni.sifipdemo.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Forme avec une vague concave en bas — utilisée pour les bandeaux verts
 * institutionnels en haut des écrans (Splash, Login, Dashboard, Transfer).
 *
 * Le centre du bas redescend de [waveDepth] dp en-dessous des coins.
 */
class WaveBottomShape(private val waveDepthDp: Float = 28f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val depth = with(density) { waveDepthDp.dp.toPx() }
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - depth)
            quadraticBezierTo(
                size.width / 2f, size.height + depth,
                0f, size.height - depth,
            )
            close()
        }
        return Outline.Generic(path)
    }
}
