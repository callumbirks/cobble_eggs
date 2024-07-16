package uk.co.callumbirks.util

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats

fun ServerPlayerEntity.getBlocksTravelled(): Int {
    val walkStat = Stats.CUSTOM.getOrCreateStat(Stats.WALK_ONE_CM)
    val sprintStat = Stats.CUSTOM.getOrCreateStat(Stats.SPRINT_ONE_CM)
    val statHandler = statHandler
    return statHandler.getStat(walkStat) / 100 + statHandler.getStat(sprintStat) / 100
}