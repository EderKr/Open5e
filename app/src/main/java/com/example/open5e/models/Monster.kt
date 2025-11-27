package com.example.open5e.models

data class Monster(
    val slug: String?,
    val name: String?,
    val desc: String?,
    val size: String?,
    val type: String?,
    val subtype: String?,
    val group: String?,
    val alignment: String?,
    val armor_class: Int?,
    val armor_desc: String?,
    val hit_points: Int?,
    val hit_dice: String?,
    val speed: Speed?,
    val strength: Int?,
    val dexterity: Int?,
    val constitution: Int?,
    val intelligence: Int?,
    val wisdom: Int?,
    val charisma: Int?,
    val strength_save: Int?,
    val dexterity_save: Int?,
    val constitution_save: Int?,
    val intelligence_save: Int?,
    val wisdom_save: Int?,
    val charisma_save: Int?,
    val perception: Int?,
    val skills: Map<String, Int>?,
    val damage_vulnerabilities: String?,
    val damage_resistances: String?,
    val damage_immunities: String?,
    val condition_immunities: String?,
    val senses: String?,
    val languages: String?,
    val challenge_rating: String?,
    val cr: Double?,
    val actions: List<MonsterAction>?,
    val bonus_actions: List<MonsterAction>?,
    val reactions: List<MonsterAction>?,
    val legendary_desc: String?,
    val legendary_actions: List<MonsterAction>?,
    val special_abilities: List<MonsterAbility>?,
    val spell_list: List<String>?,
    val environments: List<String>?,
    val img_main: String?,
    val document__slug: String?,
    val document__title: String?,
    val document__license_url: String?,
    val document__url: String?
)

data class Speed(
    val walk: Int? = null,
    val swim: Int? = null,
    val burrow: Int? = null,
    val fly: Int? = null,
    val climb: Int? = null
)

data class MonsterAction(
    val name: String?,
    val desc: String?,
    val attack_bonus: Int?,
    val damage_dice: String?
) {
}

data class MonsterAbility(
    val name: String?,
    val desc: String?
) {
}