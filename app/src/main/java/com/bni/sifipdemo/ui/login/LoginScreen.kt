package com.bni.sifipdemo.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bni.sifipdemo.R
import com.bni.sifipdemo.data.mock.MockScenario
import com.bni.sifipdemo.ui.components.BniPrimaryButton
import com.bni.sifipdemo.ui.components.BniSecondaryButton
import com.bni.sifipdemo.ui.components.CheckStepRow
import com.bni.sifipdemo.ui.components.WaveBottomShape
import com.bni.sifipdemo.ui.theme.BniBorder
import com.bni.sifipdemo.ui.theme.BniTeal
import com.bni.sifipdemo.ui.theme.BniTealDeep
import com.bni.sifipdemo.ui.theme.BniMuted
import com.bni.sifipdemo.ui.theme.StatusError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onAuthenticated: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        // Header vert avec vague concave en bas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(WaveBottomShape(waveDepthDp = 24f))
                .background(BniTeal),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(44.dp))
                Text(
                    text = "Authentification",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                ScenarioPicker(current = state.scenario, onSelected = viewModel::setScenario)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Entrez votre identifiant",
            style = MaterialTheme.typography.headlineMedium,
            color = BniTealDeep,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            textAlign = TextAlign.Center,
        )

        OutlinedTextField(
            value = state.phoneNumber,
            onValueChange = viewModel::onPhoneChanged,
            label = { Text(stringResource(R.string.login_phone_label)) },
            placeholder = { Text(stringResource(R.string.login_phone_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.PersonOutline,
                    contentDescription = null,
                    tint = BniTeal,
                )
            },
            enabled = state.phase != LoginPhase.Running,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BniTeal,
            ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "CONTRÔLES DE SÉCURITÉ SIFIP",
            color = BniMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        )
        HorizontalDivider(color = BniBorder, thickness = 1.dp)

        CheckStepRow(check = state.numberVerify)
        CheckStepRow(check = state.simSwap)
        CheckStepRow(check = state.deviceSwap)
        CheckStepRow(check = state.authorization, showDivider = false)

        Spacer(modifier = Modifier.height(28.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            when (state.phase) {
                LoginPhase.Failure -> {
                    Text(
                        text = "Connexion bloquée par le contrôle SIFIP.",
                        color = StatusError,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    BniSecondaryButton(
                        text = stringResource(R.string.login_retry),
                        onClick = viewModel::reset,
                    )
                }
                else -> {
                    BniPrimaryButton(
                        text = "Suivant",
                        onClick = { viewModel.login(onAuthenticated) },
                        loading = state.phase == LoginPhase.Running,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ScenarioPicker(
    current: MockScenario,
    onSelected: (MockScenario) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.ExpandMore,
                contentDescription = "Scénario : ${current.label}",
                tint = Color.White,
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            MockScenario.values().forEach { scenario ->
                DropdownMenuItem(
                    text = { Text(scenario.label) },
                    onClick = {
                        onSelected(scenario)
                        expanded = false
                    },
                )
            }
        }
    }
}
