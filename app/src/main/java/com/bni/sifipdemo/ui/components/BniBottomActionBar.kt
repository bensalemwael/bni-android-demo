package com.bni.sifipdemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bni.sifipdemo.ui.theme.BniTeal
import com.bni.sifipdemo.ui.theme.BniTealDeep

enum class BniBarAction { Accueil, Comptes, Center, Virements, Cartes }

/**
 * Barre d'actions bas style BNI : 4 onglets + un bouton central blanc
 * carré légèrement surélevé. Fond teal foncé.
 */
@Composable
fun BniBottomActionBar(
    selected: BniBarAction,
    onActionSelected: (BniBarAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = BniTealDeep,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ActionItem(
                icon = Icons.Filled.Home,
                label = "Accueil",
                isSelected = selected == BniBarAction.Accueil,
                onClick = { onActionSelected(BniBarAction.Accueil) },
            )
            ActionItem(
                icon = Icons.Filled.AccountBalance,
                label = "Comptes",
                isSelected = selected == BniBarAction.Comptes,
                onClick = { onActionSelected(BniBarAction.Comptes) },
            )
            CenterSquare(onClick = { onActionSelected(BniBarAction.Center) })
            ActionItem(
                icon = Icons.Filled.SwapHoriz,
                label = "Virements",
                isSelected = selected == BniBarAction.Virements,
                onClick = { onActionSelected(BniBarAction.Virements) },
            )
            ActionItem(
                icon = Icons.Filled.CreditCard,
                label = "Cartes",
                isSelected = selected == BniBarAction.Cartes,
                onClick = { onActionSelected(BniBarAction.Cartes) },
            )
        }
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.55f)
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CenterSquare(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .offset(y = (-14).dp)
            .size(56.dp)
            .background(Color.White, shape = RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BniTeal, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.QrCodeScanner,
                contentDescription = "QR Code",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
