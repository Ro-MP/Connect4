package com.example.connect4.screens.game

// Enumerate the diferent conditions of each direction
enum class Directions {
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    fun nextColumnIndex(currentColumnIndex: Int) : Int{
        return when(this) {
            NORTHEAST, EAST, SOUTHEAST -> {currentColumnIndex + 1}
            SOUTH -> currentColumnIndex
            SOUTHWEST, WEST, NORTHWEST -> {currentColumnIndex - 1}
        }
    }

    fun nextRaw(currentRaw: Int) : Int{
        return when(this) {
            NORTHEAST, NORTHWEST  -> {currentRaw - 1}
            EAST, WEST -> currentRaw
            SOUTHWEST, SOUTHEAST, SOUTH -> {currentRaw + 1}
        }
    }

    fun columnCondition(currentColumnIndex: Int, numberOfColumns: Int) : Boolean{
        return when(this) {
            NORTHEAST, EAST, SOUTHEAST -> {currentColumnIndex < numberOfColumns-1}
            SOUTH -> true
            SOUTHWEST, WEST, NORTHWEST -> {currentColumnIndex > 0}
        }
    }

    fun rawCondition(currentRaw: Int, numberOfRaws: Int) : Boolean{
        return when(this) {
            NORTHWEST, NORTHEAST -> {currentRaw > 0}
            EAST, WEST -> true
            SOUTHEAST, SOUTH, SOUTHWEST -> {currentRaw < numberOfRaws-1}
        }
    }
}