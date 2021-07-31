package com.three.sister.scheduler

import android.util.Log
import java.util.*

/**
 * @author s.buvaka
 */
class ScheduleCalculator {

    fun calculate(employees: List<Employee>) {
        val sorted = employees.sortedWith(EmployeeByRoomComparator)
        val matrix = createMatrix(sorted)

        val sergey = mutableListOf<Pair<Int, Room>>()
        val anna = mutableListOf<Pair<Int, Room>>()
        val kirill = mutableListOf<Pair<Int, Room>>()
        val anton = mutableListOf<Pair<Int, Room>>()
        val natalia = mutableListOf<Pair<Int, Room>>()
        val vera = mutableListOf<Pair<Int, Room>>()
        val alexander = mutableListOf<Pair<Int, Room>>()
        val john = mutableListOf<Pair<Int, Room>>()
        val will = mutableListOf<Pair<Int, Room>>()

        for (roomIndex in matrix.indices) {
            for (index in matrix[roomIndex]!!.indices) {
                when (matrix[roomIndex]!![index]) {
                    "Sergey" -> sergey.add(index + 1 to Room.values()[roomIndex])
                    "Anna" -> anna.add(index + 1 to Room.values()[roomIndex])
                    "Kirill" -> kirill.add(index + 1 to Room.values()[roomIndex])
                    "Anton" -> anton.add(index + 1 to Room.values()[roomIndex])
                    "Natalia" -> natalia.add(index + 1 to Room.values()[roomIndex])
                    "Vera" -> vera.add(index + 1 to Room.values()[roomIndex])
                    "Alexander" -> alexander.add(index + 1 to Room.values()[roomIndex])
                    "John" -> john.add(index + 1 to Room.values()[roomIndex])
                    "Will" -> will.add(index + 1 to Room.values()[roomIndex])
                }
            }
        }

        Log.d("XXX", "Sergey = ${sergey.sortBy { it.first }}")
        Log.d("XXX", "Anna = ${anna.sortBy { it.first }}")
        Log.d("XXX", "Kirill = ${kirill.sortBy { it.first }}")
        Log.d("XXX", "Anton = ${anton.sortBy { it.first }}")
        Log.d("XXX", "Natalia = ${natalia.sortBy { it.first }}")
        Log.d("XXX", "Vera = ${vera.sortBy { it.first }}")
        Log.d("XXX", "Alexander = ${alexander.sortBy { it.first }}")
        Log.d("XXX", "John = ${john.sortBy { it.first }}")
        Log.d("XXX", "Will = ${will.sortBy { it.first }}")
    }

    private fun createMatrix(employees: List<Employee>): List<Array<String?>?> {
        var isOrdered = true
        val rooms = Room.values()

        val days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

        val matrix = mutableListOf<Array<String?>>()
        repeat(rooms.count()) { matrix.add(arrayOfNulls(days)) }

        employees.forEach { employee ->
            val priorityIndex = employee.priority?.let { rooms.indexOf(employee.priority) }
            var weekdays = employee.schedule.weekdays
            var weekends = 0

            if (isOrdered) {
                for (dateIndex in 0 until days) {
                    var isAddedToPriority = false
                    if (priorityIndex != null) {
                        if (weekdays > 0 && matrix[priorityIndex][dateIndex] == null) {
                            matrix[priorityIndex][dateIndex] = employee.name
                            isAddedToPriority = true
                        }
                    }
                    if (!isAddedToPriority) {
                        for (roomIndex in matrix.indices) {
                            if (priorityIndex != null && roomIndex == priorityIndex) continue
                            if (weekends > 0) break

                            if (weekdays > 0 && matrix[roomIndex][dateIndex] == null) {
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
                        if (weekdays > 0 && matrix[priorityIndex][dateIndex] == null) {
                            matrix[priorityIndex][dateIndex] = employee.name
                            isAddedToPriority = true
                        }
                    }
                    if (!isAddedToPriority) {
                        for (roomIndex in matrix.indices) {
                            if (weekends > 0) break

                            if (weekdays > 0 && matrix[roomIndex][dateIndex] == null) {
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
