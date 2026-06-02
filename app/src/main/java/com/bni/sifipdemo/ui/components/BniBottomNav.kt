package com.bni.sifipdemo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bni.sifipdemo.ui.theme.BniTeal
import com.bni.sifipdemo.ui.theme.BniMuted

enum class BniTab { Accueil, Consultations, Operations, Gestion }

private data class TabSpec(
    val tab: BniTab,
    val label: String,
    val icon: ImageVector,
)

private val Tabs = listOf(
    TabSpec(BniTab.Accueil, "Accueil", Icons.Filled.Home),
    TabSpec(BniTab.Consultations, "Consultations", Icons.Filled.Search),
    TabSpec(BniTab.Operations, "Opérations", Icons.Filled.SwapHoriz),
    TabSpec(BniTab.Gestion, "Gestion", Icons.Filled.Tune),
)

/**
 * Barre de navigation bas style BNI : 4 onglets fixes. Couleur verte pour
 * l'onglet sélectionné, grisé pour les autres. Les onglets non sélectionnés
 * sont inertes dans la démo.
 */
@Composable
fun BniBottomNav(
    selected: BniTab,
    onTabSelected: (BniTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 6.dp,
    ) {
        Tabs.forEach { spec ->
            val isSelected = spec.tab == selected
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(spec.tab) },
                icon = {
                    Icon(
                        imageVector = spec.icon,
                        contentDescription = spec.label,
                    )
                },
                label = { Text(spec.label, style = MaterialTheme.typography.bodyMedium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BniTeal,
                    selectedTextColor = BniTeal,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = BniMuted,
                    unselectedTextColor = BniMuted,
                ),
            )
        }
    }
}
