package com.three.sister.scheduler

/**
 * @author s.buvaka
 */
class Employee(
    val name: String,
    val surname: String,
    val schedule: Schedule,
    val priority: Room? = null
)

class Schedule(val weekdays: Int, val weekends: Int)

enum class Room {
    FIRST_FLOOR,
    LFK,
    SECOND_FLOOR_OLD,
    SECOND_FLOOR_NEW,
    THIRD_FLOOR_OLD,
    THIRD_FLOOR_NEW,
    FOURTH_FLOOR,
    GENERAL_CLEANING,
    NIGHT
}

