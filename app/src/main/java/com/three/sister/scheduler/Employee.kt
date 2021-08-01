package com.three.sister.scheduler

import java.io.Serializable

/**
 * @author s.buvaka
 */
class Employee(
    val name: String,
    val surname: String,
    val schedule: Schedule,
    val priority: Room? = null
) : Serializable {

    fun generateName(): String = "$name \n$surname"
}

data class Schedule(val weekdays: Int, val weekends: Int) : Serializable {

    companion object {
        private const val MAX_DAYS = -1

        val MAX = Schedule(MAX_DAYS, 0)
    }
}

enum class Room(val title: String) {
    FIRST_FLOOR("Первый этаж"),
    LFK("ЛФК"),
    SECOND_FLOOR_OLD("Второй этаж: Старый"),
    SECOND_FLOOR_NEW("Второй этаж: Новый"),
    THIRD_FLOOR_OLD("Третий этаж: Старый"),
    THIRD_FLOOR_NEW("Третий этаж: Новый"),
    FOURTH_FLOOR("Четвертый этаж"),
    NIGHT("Ночная смена"),
    GENERAL_CLEANING("Генеральная уборка");
}

