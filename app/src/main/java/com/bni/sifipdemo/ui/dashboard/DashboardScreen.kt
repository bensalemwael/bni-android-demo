package com.bni.sifipdemo.ui.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.PersonOutline
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bni.sifipdemo.data.model.Transaction
import com.bni.sifipdemo.ui.components.BniBarAction
import com.bni.sifipdemo.ui.components.BniBottomActionBar
import com.bni.sifipdemo.ui.theme.BniMuted
import com.bni.sifipdemo.ui.theme.BniTeal
import com.bni.sifipdemo.ui.theme.BniTealDark
import com.bni.sifipdemo.ui.theme.BniTealDeep
import com.bni.sifipdemo.ui.theme.BniTealLight
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
    var selectedAction by remember { mutableStateOf(BniBarAction.Accueil) }

    Scaffold(
        bottomBar = {
            BniBottomActionBar(
                selected = selectedAction,
                onActionSelected = { action ->
                    selectedAction = action
                    if (action == BniBarAction.Virements) onTransferClicked()
                },
            )
        },
        containerColor = BniTealDeep,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BniTeal, BniTealDark, BniTealDeep),
                    ),
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                TopBar(holderName = account.holder, onLogout = onLogout)
                Spacer(modifier = Modifier.height(16.dp))
                QuickActionsRow(onTransferClicked = onTransferClicked)
                Spacer(modifier = Modifier.height(20.dp))
                BalanceCard(
                    accountLabel = "Compte courant",
                    balanceMga = account.balanceMga,
                    onTransferClicked = onTransferClicked,
                )
                Spacer(modifier = Modifier.height(28.dp))
                SectionTitle("Mes opérations bancaires")
                Spacer(modifier = Modifier.height(12.dp))
                OperationsRow(account.transactions)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TopBar(holderName: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Accueil",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Bonjour, ${holderName.split(" ").firstOrNull() ?: holderName}",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp,
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.NotificationsNone,
                contentDescription = "Notifications",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = "Se déconnecter",
                    tint = BniTeal,
                )
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onTransferClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        QuickAction(icon = Icons.Filled.SwapHoriz, label = "Virements", onClick = onTransferClicked)
        QuickAction(icon = Icons.Filled.Savings, label = "Épargne", onClick = {})
        QuickAction(icon = Icons.Filled.CreditCard, label = "Cartes", onClick = {})
        QuickAction(icon = Icons.Filled.Receipt, label = "Factures", onClick = {})
    }
}

@Composable
private fun QuickAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(74.dp)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onClick, modifier = Modifier.size(56.dp)) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = BniTeal,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun BalanceCard(
    accountLabel: String,
    balanceMga: Long,
    onTransferClicked: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BniTealLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Wallet,
                        contentDescription = null,
                        tint = BniTeal,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = accountLabel,
                        color = BniMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    )
                    Text(
                        text = "Solde disponible",
                        color = BniText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "MGA ${formatMga(balanceMga)}",
                color = BniTeal,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Chip(icon = Icons.Filled.Send, label = "Envoyer", onClick = onTransferClicked)
                Chip(icon = Icons.Filled.QrCodeScanner, label = "Scan", onClick = {})
                Chip(icon = Icons.Filled.CreditCard, label = "Carte", onClick = {})
                Chip(icon = Icons.Filled.PowerSettingsNew, label = "Plus", onClick = {})
            }
        }
    }
}

@Composable
private fun Chip(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BniTealLight),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = BniTeal,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = BniText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
private fun OperationsRow(transactions: List<Transaction>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        transactions.forEachIndexed { i, tx ->
            OperationCard(tx)
            if (i < transactions.lastIndex) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun OperationCard(tx: Transaction) {
    val credit = tx.amountMga >= 0
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (credit) BniTealLight else StatusError.copy(alpha = 0.10f)),
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
                    color = BniText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = tx.date,
                    color = BniMuted,
                    fontSize = 12.sp,
                )
            }
            Text(
                text = "MGA ${formatMga(kotlin.math.abs(tx.amountMga))}",
                color = if (credit) StatusOk else StatusError,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun formatMga(amount: Long): String {
    val nf = NumberFormat.getInstance(Locale.FRANCE)
    return nf.format(amount)
}

internal fun formatMgaPublic(amount: Long): String = formatMga(amount)
