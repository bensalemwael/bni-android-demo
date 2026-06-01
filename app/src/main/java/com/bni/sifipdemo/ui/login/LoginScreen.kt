package com.bni.sifipdemo.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bni.sifipdemo.R
import com.bni.sifipdemo.data.mock.MockScenario
import com.bni.sifipdemo.ui.components.BniLogo
import com.bni.sifipdemo.ui.components.BniPrimaryButton
import com.bni.sifipdemo.ui.components.BniSecondaryButton
import com.bni.sifipdemo.ui.components.CheckStepRow
import com.bni.sifipdemo.ui.theme.BniNavy
import com.bni.sifipdemo.ui.theme.BniNavyDark
import com.bni.sifipdemo.ui.theme.StatusError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onAuthenticated: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Branded header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Brush.verticalGradient(listOf(BniNavy, BniNavyDark))),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                ScenarioPicker(
                    current = state.scenario,
                    onSelected = viewModel::setScenario,
                )
                Spacer(modifier = Modifier.height(8.dp))
                BniLogo(width = 180.dp, height = 64.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.login_welcome),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.login_subtitle),
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        // Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 200.dp)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = viewModel::onPhoneChanged,
                    label = { Text(stringResource(R.string.login_phone_label)) },
                    placeholder = { Text(stringResource(R.string.login_phone_hint)) },
                    enabled = state.phase != LoginPhase.Running,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BniNavy,
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                CheckStepRow(check = state.numberVerify)
                CheckStepRow(check = state.simSwap)
                CheckStepRow(check = state.deviceSwap)
                CheckStepRow(check = state.authorization)

                Spacer(modifier = Modifier.height(4.dp))

                when (state.phase) {
                    LoginPhase.Failure -> {
                        Text(
                            text = "Connexion bloquée par le contrôle SIFIP.",
                            color = StatusError,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        BniSecondaryButton(
                            text = stringResource(R.string.login_retry),
                            onClick = viewModel::reset,
                        )
                    }
                    else -> {
                        BniPrimaryButton(
                            text = stringResource(R.string.login_button),
                            onClick = { viewModel.login(onAuthenticated) },
                            loading = state.phase == LoginPhase.Running,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScenarioPicker(
    current: MockScenario,
    onSelected: (MockScenario) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Surface(
            color = Color.White.copy(alpha = 0.12f),
            shape = RoundedCornerShape(20.dp),
        ) {
            Row(
                modifier = Modifier.padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Démo : ${current.label}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription = "Changer de scénario",
                        tint = Color.White,
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
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
