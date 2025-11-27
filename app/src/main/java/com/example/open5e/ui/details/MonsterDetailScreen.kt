package com.example.open5e.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.open5e.models.Monster
import com.example.open5e.ui.components.TextMarkdown
import com.example.open5e.viewmodels.MainViewModel

@Composable
fun MonsterDetailScreen(
    slug: String,
    viewModel: MainViewModel = viewModel()
) {
    var monster by remember { mutableStateOf<Monster?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(slug) {
        viewModel.fetchMonsterDetail(slug) { result, err ->
            monster = result
            error = err
        }
    }

    if (monster == null && error == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    error?.let {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    monster?.let { m ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(m.name ?: "Unknown", style = MaterialTheme.typography.headlineLarge)
                TextMarkdown("${m.size ?: ""} ${m.type ?: ""}".trim())
                TextMarkdown("Alignment: ${m.alignment ?: "Unknown"}")
                Spacer(Modifier.height(8.dp))
            }

            item {
                Text("Stats", style = MaterialTheme.typography.titleMedium)
                TextMarkdown("Armor Class: ${m.armor_class ?: 0} (${m.armor_desc ?: "no armor info"})")
                TextMarkdown("Hit Points: ${m.hit_points ?: 0} (${m.hit_dice ?: "-"})")
            }

            item {
                Text("Speed", style = MaterialTheme.typography.titleMedium)
                Text(
                    buildString {
                        append("Walk: ${m.speed?.walk ?: 0} ft")
                        m.speed?.swim?.let { append(" | Swim: $it ft") }
                        m.speed?.fly?.let { append(" | Fly: $it ft") }
                        m.speed?.climb?.let { append(" | Climb: $it ft") }
                        m.speed?.burrow?.let { append(" | Burrow: $it ft") }
                    }
                )
            }

            item {
                Text("Abilities", style = MaterialTheme.typography.titleMedium)
                TextMarkdown("STR ${m.strength ?: 0}   |   DEX ${m.dexterity ?: 0}")
                TextMarkdown("CON ${m.constitution ?: 0}   |   INT ${m.intelligence ?: 0}")
                TextMarkdown("WIS ${m.wisdom ?: 0}   |   CHA ${m.charisma ?: 0}")
            }

            if (
                m.strength_save != null || m.dexterity_save != null || m.constitution_save != null ||
                m.intelligence_save != null || m.wisdom_save != null || m.charisma_save != null
            ) {
                item {
                    Text("Saving Throws", style = MaterialTheme.typography.titleMedium)
                    if (m.strength_save != null) Text("STR Save: +${m.strength_save}")
                    if (m.dexterity_save != null) Text("DEX Save: +${m.dexterity_save}")
                    if (m.constitution_save != null) Text("CON Save: +${m.constitution_save}")
                    if (m.intelligence_save != null) Text("INT Save: +${m.intelligence_save}")
                    if (m.wisdom_save != null) Text("WIS Save: +${m.wisdom_save}")
                    if (m.charisma_save != null) Text("CHA Save: +${m.charisma_save}")
                }
            }

            if (!m.skills.isNullOrEmpty()) {
                item {
                    Text("Skills", style = MaterialTheme.typography.titleMedium)
                    m.skills.forEach { (skill, mod) ->
                        TextMarkdown("$skill: +$mod")
                    }
                }
            }

            if (!m.damage_vulnerabilities.isNullOrBlank() ||
                !m.damage_resistances.isNullOrBlank() ||
                !m.damage_immunities.isNullOrBlank() ||
                !m.condition_immunities.isNullOrBlank()
            ) {
                item { Text("Defenses", style = MaterialTheme.typography.titleMedium) }

                if (!m.damage_vulnerabilities.isNullOrBlank()) {
                    item { Text("Vulnerabilities: ${m.damage_vulnerabilities}") }
                }
                if (!m.damage_resistances.isNullOrBlank()) {
                    item { Text("Resistances: ${m.damage_resistances}") }
                }
                if (!m.damage_immunities.isNullOrBlank()) {
                    item { Text("Immunities: ${m.damage_immunities}") }
                }
                if (!m.condition_immunities.isNullOrBlank()) {
                    item { Text("Condition Immunities: ${m.condition_immunities}") }
                }
            }

            if (!m.senses.isNullOrBlank()) {
                item { TextMarkdown("Senses: ${m.senses}") }
            }
            if (!m.languages.isNullOrBlank()) {
                item { TextMarkdown("Languages: ${m.languages}") }
            }

            item {
                TextMarkdown("Challenge Rating: ${m.challenge_rating ?: "?"}")
            }

            if (!m.actions.isNullOrEmpty()) {
                item { Text("Actions", style = MaterialTheme.typography.titleMedium) }

                items(m.actions) { action ->
                    Column {
                        Text("• ${action.name}", style = MaterialTheme.typography.titleSmall)
                        TextMarkdown(action.desc ?: "No description")
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }

            if (!m.bonus_actions.isNullOrEmpty()) {
                item { Text("Bonus Actions", style = MaterialTheme.typography.titleMedium) }

                items(m.bonus_actions) { ba ->
                    Column {
                        Text("• ${ba.name}", style = MaterialTheme.typography.titleSmall)
                        TextMarkdown(ba.desc ?: "No description")
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }

            if (!m.reactions.isNullOrEmpty()) {
                item { Text("Reactions", style = MaterialTheme.typography.titleMedium) }

                items(m.reactions) { r ->
                    Column {
                        Text("• ${r.name}", style = MaterialTheme.typography.titleSmall)
                        TextMarkdown(r.desc ?: "No description")
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }

            if (!m.legendary_actions.isNullOrEmpty()) {
                item {
                    Text("Legendary Actions", style = MaterialTheme.typography.titleMedium)
                    if (!m.legendary_desc.isNullOrBlank())
                        TextMarkdown(m.legendary_desc)
                }

                items(m.legendary_actions) { la ->
                    Column {
                        Text("• ${la.name}", style = MaterialTheme.typography.titleSmall)
                        TextMarkdown(la.desc ?: "No description")
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }

            if (!m.special_abilities.isNullOrEmpty()) {
                item { Text("Special Abilities", style = MaterialTheme.typography.titleMedium) }

                items(m.special_abilities) { sa ->
                    Column {
                        Text("✦ ${sa.name}", style = MaterialTheme.typography.titleSmall)
                        TextMarkdown(sa.desc ?: "No description")
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }

            if (!m.desc.isNullOrBlank()) {
                item {
                    Text("Description", style = MaterialTheme.typography.titleMedium)
                    TextMarkdown(m.desc)
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                TextMarkdown("Source: ${m.document__title ?: "Unknown"}")
            }
        }
    }
}