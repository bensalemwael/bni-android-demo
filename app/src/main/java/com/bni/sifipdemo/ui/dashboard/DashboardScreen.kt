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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bni.sifipdemo.R
import com.bni.sifipdemo.data.model.Transaction
import com.bni.sifipdemo.ui.components.BniLogo
import com.bni.sifipdemo.ui.theme.BniBorder
import com.bni.sifipdemo.ui.theme.BniMuted
import com.bni.sifipdemo.ui.theme.BniNavy
import com.bni.sifipdemo.ui.theme.BniRed
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        // Trait rouge + bandeau navy
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(BniRed),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BniNavy)
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BniLogo(width = 150.dp, height = 48.dp)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(R.string.dashboard_logout),
                        tint = Color.White,
                    )
                }
            }
        }

        // Section solde — flat avec bordure du bas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
        ) {
            Text(
                text = stringResource(R.string.dashboard_hello, account.holder).uppercase(),
                color = BniMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.dashboard_account_label, account.accountNumberMasked),
                color = BniNavy,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.dashboard_balance_label),
                color = BniMuted,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${formatXof(account.balanceXof)} FCFA",
                color = BniNavy,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        HorizontalDivider(color = BniBorder, thickness = 1.dp)

        // Actions rapides — 3 icônes + libellés
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            QuickAction(
                icon = Icons.Filled.SwapHoriz,
                label = "Virement",
                accent = true,
                onClick = onTransferClicked,
            )
            QuickAction(
                icon = Icons.Filled.PhoneAndroid,
                label = "Recharge",
                onClick = {},
            )
            QuickAction(
                icon = Icons.Filled.Receipt,
                label = "Factures",
                onClick = {},
            )
        }
        HorizontalDivider(color = BniBorder, thickness = 1.dp)

        // Section transactions — liste plate
        Text(
            text = stringResource(R.string.dashboard_recent).uppercase(),
            color = BniMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        )

        Column(modifier = Modifier.background(Color.White)) {
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
        HorizontalDivider(color = BniBorder, thickness = 1.dp)
    }
}

@Composable
private fun QuickAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    accent: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(96.dp)
            .padding(vertical = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(
                    if (accent) BniNavy else Color.White,
                    shape = RoundedCornerShape(4.dp),
                )
                .border(
                    width = 1.dp,
                    color = if (accent) BniNavy else BniBorder,
                    shape = RoundedCornerShape(4.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (accent) Color.White else BniNavy,
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = BniNavy,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun TransactionRow(tx: Transaction) {
    val credit = tx.amountXof >= 0
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (credit) StatusOk.copy(alpha = 0.10f) else StatusError.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(4.dp),
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
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = tx.date,
                style = MaterialTheme.typography.bodyMedium,
                color = BniMuted,
            )
        }
        Text(
            text = "${if (credit) "+" else "-"}${formatXof(kotlin.math.abs(tx.amountXof))} FCFA",
            color = if (credit) StatusOk else StatusError,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun formatXof(amount: Long): String {
    val nf = NumberFormat.getInstance(Locale.FRANCE)
    return nf.format(amount)
}

internal fun formatXofPublic(amount: Long): String = formatXof(amount)
