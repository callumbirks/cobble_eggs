package uk.co.callumbirks.config

import kotlin.random.Random

fun Float.rollShiny() : Boolean {
    val roll = Random.nextFloat() * this
    return if (roll <= 1f) {
        true
    } else {
        false
    }
}