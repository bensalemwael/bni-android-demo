package com.bni.sifipdemo.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bni.sifipdemo.data.model.Transaction
import com.bni.sifipdemo.ui.components.ActionTile
import com.bni.sifipdemo.ui.components.BniBottomNav
import com.bni.sifipdemo.ui.components.BniTab
import com.bni.sifipdemo.ui.components.WaveBottomShape
import com.bni.sifipdemo.ui.theme.BniBorder
import com.bni.sifipdemo.ui.theme.BniGreen
import com.bni.sifipdemo.ui.theme.BniGreenDark
import com.bni.sifipdemo.ui.theme.BniGreenDeep
import com.bni.sifipdemo.ui.theme.BniGreenLight
import com.bni.sifipdemo.ui.theme.BniMuted
import com.bni.sifipdemo.ui.theme.BniText
import com.bni.sifipdemo.ui.theme.StatusError
import com.bni.sifipdemo.ui.theme.StatusOk
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onTransferClicked: () -> Unit,
    onLogout: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val account = state.account
    var selectedTab by remember { mutableStateOf(BniTab.Accueil) }

    Scaffold(
        bottomBar = {
            BniBottomNav(
                selected = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // En-tête vert avec vague
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(WaveBottomShape(waveDepthDp = 28f))
                    .background(BniGreen),
            ) {
                Column(modifier = Modifier.padding(bottom = 36.dp)) {
                    HeaderTopBar(holderName = account.holder, onLogout = onLogout)
                    WalletCard(
                        accountSuffix = account.accountNumberMasked,
                        balanceMga = account.balanceMga,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PillRow(onTransferClicked)
                }
            }

            // Grille d'actions
            Spacer(modifier = Modifier.height(20.dp))
            ActionGrid(onTransferClicked = onTransferClicked)

            // Transactions
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "TRANSACTIONS RÉCENTES",
                color = BniMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
            ) {
                Column {
                    account.transactions.forEachIndexed { i, tx ->
                        TransactionRow(tx)
                        if (i < account.transactions.lastIndex) {
                            HorizontalDivider(
                                color = BniBorder,
                                thickness = 1.dp,
                                modifier = Modifier.padding(start = 60.dp),
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeaderTopBar(holderName: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.PersonOutline,
                contentDescription = null,
                tint = BniGreenDark,
                modifier = Modifier.size(30.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = holderName.uppercase(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Dernière visite : 27/06/2025 11:48:50",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        IconButton(onClick = onLogout) {
            Icon(
                imageVector = Icons.Filled.PowerSettingsNew,
                contentDescription = "Se déconnecter",
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun WalletCard(accountSuffix: String, balanceMga: Long) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.14f),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        tint = BniGreenDark,
                        modifier = Modifier.size(26.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "COMPTE COURANT",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp,
                    )
                    Text(
                        text = accountSuffix,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.25f),
                thickness = 1.dp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Solde disponible :",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${formatMga(balanceMga)} MGA",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun PillRow(onTransferClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PillButton(text = "Mes mouvements", onClick = {}, modifier = Modifier.weight(1f))
        PillButton(text = "Mes bénéficiaires", onClick = {}, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PillButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BniGreenDark,
            contentColor = Color.White,
        ),
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ActionGrid(onTransferClicked: () -> Unit) {
    val tiles = listOf<Triple<ImageVector, String, () -> Unit>>(
        Triple(Icons.Filled.SwapHoriz, "Mes virements", onTransferClicked),
        Triple(Icons.Filled.Payments, "Mise à disposition de fonds", {}),
        Triple(Icons.Filled.PointOfSale, "Retrait sans carte", {}),
        Triple(Icons.Filled.CreditCard, "Mes cartes", {}),
        Triple(Icons.Filled.Receipt, "Paiements de factures", {}),
        Triple(Icons.Filled.CalendarMonth, "Prendre un RDV", {}),
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        tiles.chunked(3).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { (icon, label, onClick) ->
                    ActionTile(
                        icon = icon,
                        label = label,
                        onClick = onClick,
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(tx: Transaction) {
    val credit = tx.amountMga >= 0
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (credit) BniGreenLight else StatusError.copy(alpha = 0.10f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (credit) Icons.Filled.SouthWest else Icons.Filled.NorthEast,
                contentDescription = null,
                tint = if (credit) StatusOk else StatusError,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = BniText,
            )
            Text(
                text = tx.date,
                style = MaterialTheme.typography.bodyMedium,
                color = BniMuted,
            )
        }
        Text(
            text = "${if (credit) "+" else "-"}${formatMga(kotlin.math.abs(tx.amountMga))} MGA",
            color = if (credit) StatusOk else StatusError,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun formatMga(amount: Long): String {
    val nf = NumberFormat.getInstance(Locale.FRANCE)
    return nf.format(amount)
}

internal fun formatMgaPublic(amount: Long): String = formatMga(amount)
