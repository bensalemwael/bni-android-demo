package com.bni.sifipdemo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bni.sifipdemo.ui.login.CheckState
import com.bni.sifipdemo.ui.login.CheckStatus
import com.bni.sifipdemo.ui.theme.BniBorder
import com.bni.sifipdemo.ui.theme.BniMuted
import com.bni.sifipdemo.ui.theme.StatusError
import com.bni.sifipdemo.ui.theme.StatusOk

/**
 * Flat row with a thin bottom divider — registre liste bancaire classique.
 * Pas d'ombres, pas de cartes, pas de coins arrondis : ressemble à une
 * ligne de tableau institutionnel.
 */
@Composable
fun CheckStepRow(
    check: CheckState,
    showDivider: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            StatusIndicator(check.status)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = check.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = check.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BniMuted,
                )
                AnimatedVisibility(
                    visible = check.resultMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Text(
                        text = check.resultMessage.orEmpty(),
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = when (check.status) {
                            CheckStatus.Ok -> StatusOk
                            CheckStatus.Failed -> StatusError
                            else -> BniMuted
                        },
                    )
                }
            }
        }
        if (showDivider) {
            HorizontalDivider(color = BniBorder, thickness = 1.dp)
        }
    }
}

@Composable
private fun StatusIndicator(status: CheckStatus) {
    val size = 22.dp
    when (status) {
        CheckStatus.Idle -> {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(BniBorder),
            )
        }
        CheckStatus.Running -> {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                strokeWidth = 2.5.dp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        CheckStatus.Ok -> {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(StatusOk),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "OK",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
        CheckStatus.Failed -> {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(StatusError),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Échec",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(0.dp))
}
