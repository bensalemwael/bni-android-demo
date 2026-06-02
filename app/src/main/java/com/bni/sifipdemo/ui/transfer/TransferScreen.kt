package com.bni.sifipdemo.ui.transfer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bni.sifipdemo.R
import com.bni.sifipdemo.ui.components.BniPrimaryButton
import com.bni.sifipdemo.ui.components.BniSecondaryButton
import com.bni.sifipdemo.ui.components.FraudGauge
import com.bni.sifipdemo.ui.components.FraudReasons
import com.bni.sifipdemo.ui.dashboard.formatXofPublic
import com.bni.sifipdemo.ui.theme.BniBorder
import com.bni.sifipdemo.ui.theme.BniMuted
import com.bni.sifipdemo.ui.theme.BniNavy
import com.bni.sifipdemo.ui.theme.BniRed
import com.bni.sifipdemo.ui.theme.StatusError
import com.bni.sifipdemo.ui.theme.StatusOk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    viewModel: TransferViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

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
                .background(BniNavy),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color.White,
                    )
                }
                Text(
                    text = stringResource(R.string.transfer_title).uppercase(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                )
            }
        }

        when (state.phase) {
            TransferPhase.Form -> TransferForm(
                state = state,
                onRecipientChanged = viewModel::onRecipientChanged,
                onIbanChanged = viewModel::onIbanChanged,
                onAmountChanged = viewModel::onAmountChanged,
                onSubmit = viewModel::submit,
            )
            TransferPhase.Analyzing -> AnalyzingState()
            TransferPhase.Approved -> ResultState(
                title = stringResource(R.string.transfer_approved),
                isSuccess = true,
                score = state.result?.score ?: 0,
                reasons = state.result?.reasons.orEmpty(),
                amount = state.amountText.toLongOrNull() ?: 0,
                onClose = onBack,
                onRetry = viewModel::reset,
            )
            TransferPhase.Rejected -> ResultState(
                title = stringResource(R.string.transfer_blocked),
                isSuccess = false,
                score = state.result?.score ?: 0,
                reasons = state.result?.reasons.orEmpty(),
                amount = state.amountText.toLongOrNull() ?: 0,
                onClose = onBack,
                onRetry = viewModel::reset,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransferForm(
    state: TransferUiState,
    onRecipientChanged: (String) -> Unit,
    onIbanChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 20.dp),
    ) {
        Text(
            text = "COORDONNÉES BÉNÉFICIAIRE",
            color = BniMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        )
        HorizontalDivider(color = BniBorder, thickness = 1.dp)
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            OutlinedTextField(
                value = state.recipient,
                onValueChange = onRecipientChanged,
                label = { Text(stringResource(R.string.transfer_recipient)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BniNavy),
            )
            OutlinedTextField(
                value = state.iban,
                onValueChange = onIbanChanged,
                label = { Text(stringResource(R.string.transfer_iban)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BniNavy),
            )
        }

        Text(
            text = "MONTANT",
            color = BniMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        )
        HorizontalDivider(color = BniBorder, thickness = 1.dp)
        Column(modifier = Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = state.amountText,
                onValueChange = onAmountChanged,
                label = { Text(stringResource(R.string.transfer_amount)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BniNavy),
            )
            Spacer(modifier = Modifier.height(24.dp))
            BniPrimaryButton(
                text = stringResource(R.string.transfer_send),
                onClick = onSubmit,
                enabled = state.amountText.isNotBlank() && state.iban.isNotBlank(),
            )
        }
    }
}

@Composable
private fun AnalyzingState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = BniNavy, strokeWidth = 3.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.transfer_analyzing),
            style = MaterialTheme.typography.titleMedium,
            color = BniNavy,
        )
    }
}

@Composable
private fun ResultState(
    title: String,
    isSuccess: Boolean,
    score: Int,
    reasons: List<String>,
    amount: Long,
    onClose: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        // Bandeau de résultat — couleur de statut
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSuccess) StatusOk.copy(alpha = 0.08f) else StatusError.copy(alpha = 0.08f),
                )
                .border(
                    width = 1.dp,
                    color = if (isSuccess) StatusOk.copy(alpha = 0.3f) else StatusError.copy(alpha = 0.3f),
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isSuccess) Icons.Filled.CheckCircle else Icons.Filled.Block,
                contentDescription = null,
                tint = if (isSuccess) StatusOk else StatusError,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.padding(end = 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSuccess) StatusOk else StatusError,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                )
                Text(
                    text = "Montant : ${formatXofPublic(amount)} FCFA",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BniNavy.copy(alpha = 0.8f),
                )
            }
        }

        FraudGauge(score = score)

        Text(
            text = "MOTIFS ANALYSÉS PAR L'IA SIFIP",
            color = BniMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        )
        HorizontalDivider(color = BniBorder, thickness = 1.dp)
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            FraudReasons(reasons = reasons)
        }
        HorizontalDivider(color = BniBorder, thickness = 1.dp)

        Column(modifier = Modifier.padding(20.dp)) {
            BniPrimaryButton(
                text = stringResource(R.string.transfer_close),
                onClick = onClose,
            )
            Spacer(modifier = Modifier.height(8.dp))
            BniSecondaryButton(
                text = "Nouveau virement",
                onClick = onRetry,
            )
        }
    }
}
