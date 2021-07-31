package com.three.sister.scheduler

import java.util.*
import kotlin.math.roundToInt

/**
 * @author s.buvaka
 */
class ScheduleCalculator {

    fun calculate(employees: List<Employee>): List<Array<String?>> {
        val sorted = employees.sortedWith(EmployeeByRoomComparator)

        return createMatrix(sorted)
    }

    private fun createMatrix(employees: List<Employee>): List<Array<String?>> {
        var isOrdered = true
        val rooms = Room.values()

        val nightIndex = rooms.indexOf(Room.NIGHT)
        val lfkIndex = rooms.indexOf(Room.LFK)

        val days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

        val matrix = mutableListOf<Array<String?>>()
        repeat(rooms.count()) { matrix.add(arrayOfNulls(days)) }

        employees.forEach { employee ->
            val priorityIndex = employee.priority?.let { rooms.indexOf(employee.priority) }
            var weekdays = if (employee.schedule == Schedule.MAX) employee.schedule.weekdays else (employee.schedule.weekdays.toFloat() / 2).roundToInt()
            var weekends = 0

            if (isOrdered) {
                for (dateIndex in 0 until days) {
                    var isAddedToPriority = false
                    if (priorityIndex != null) {
                        if ((weekdays == -1 || weekdays > 0) && matrix[priorityIndex][dateIndex] == null) {
                            matrix[priorityIndex][dateIndex] = employee.name
                            isAddedToPriority = true
                        }
                    }
                    if (!isAddedToPriority) {
                        for (roomIndex in matrix.indices) {
                            if (priorityIndex != null && roomIndex == priorityIndex) continue
                            if (weekends > 0) break

                            if (roomIndex == nightIndex && matrix[lfkIndex][dateIndex] != null) continue

                            if ((weekdays == -1 || weekdays > 0) && matrix[roomIndex][dateIndex] == null) {
                                matrix[roomIndex][dateIndex] = employee.name
                                break
                            }
                        }
                    }
                    if (weekdays > 0) {
                        weekdays--
                        if (weekdays == 0) {
                            weekends = employee.schedule.weekends
                        }
                    } else if (weekends > 0) {
                        weekends--
                        if (weekends == 0) {
                            weekdays = employee.schedule.weekdays
                        }
                    }
                }
            } else {
                for (dateIndex in days - 1 downTo 0) {
                    var isAddedToPriority = false
                    if (priorityIndex != null) {
                        if ((weekdays == -1 || weekdays > 0) && matrix[priorityIndex][dateIndex] == null) {
                            matrix[priorityIndex][dateIndex] = employee.name
                            isAddedToPriority = true
                        }
                    }
                    if (!isAddedToPriority) {
                        for (roomIndex in matrix.indices) {
                            if (weekends > 0) break

                            if (roomIndex == nightIndex && matrix[lfkIndex][dateIndex] != null) continue

                            if ((weekdays == -1 || weekdays > 0) && matrix[roomIndex][dateIndex] == null) {
                                matrix[roomIndex][dateIndex] = employee.name
                                break
                            }
                        }
                    }
                    if (weekdays > 0) {
                        weekdays--
                        if (weekdays == 0) {
                            weekends = employee.schedule.weekends
                        }
                    } else if (weekends > 0) {
                        weekends--
                        if (weekends == 0) {
                            weekdays = employee.schedule.weekdays
                        }
                    }
                }
            }
            isOrdered = !isOrdered
        }
        return matrix
    }

    private object EmployeeByRoomComparator : Comparator<Employee> {
        override fun compare(o1: Employee?, o2: Employee?): Int {
            return when {
                o1?.priority == null -> 1
                o2?.priority == null -> -1
                else -> o1.priority.compareTo(o2.priority)
            }
        }
    }
}
