package uk.co.callumbirks

import net.minecraft.server.MinecraftServer

interface CobbleEggsImplementation {
    val modAPI: ModAPI

    fun registerItems()

    fun environment(): Environment

    fun server(): MinecraftServer?
}

enum class ModAPI {
    FABRIC,
    FORGE
}

enum class Environment {
    CLIENT,
    SERVER
}