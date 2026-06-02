package com.bni.sifipdemo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bni.sifipdemo.R

/**
 * BNI wordmark placeholder. The drawable resource is a generic vector
 * stand-in — replace `res/drawable/bni_logo.xml` by the official BNI Côte
 * d'Ivoire logo (PNG / SVG) received from the bank's communication team
 * without changing any composable code.
 */
@Composable
fun BniLogo(
    modifier: Modifier = Modifier,
    width: Dp = 240.dp,
    height: Dp = 80.dp,
) {
    Box(modifier = modifier.width(width).height(height)) {
        Image(
            painter = painterResource(id = R.drawable.bni_logo),
            contentDescription = "BNI Côte d'Ivoire",
        )
    }
}
