package com.three.sister.scheduler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculator = ScheduleCalculator()

        val employees = listOf(
            Employee(name = "Sergey", surname = "Buvaka", schedule = Schedule(2, 2), Room.FIRST_FLOOR),
            Employee(name = "Anna", surname = "Smith", schedule = Schedule(2, 2), Room.LFK),
            Employee(name = "Kirill", surname = "Karl", schedule = Schedule(2, 1), Room.FOURTH_FLOOR),
            Employee(name = "Anton", surname = "Dom", schedule = Schedule(2, 1)),
            Employee(name = "Natalia", surname = "Serp", schedule = Schedule(4, 1), Room.SECOND_FLOOR_NEW),
            Employee(name = "Vera", surname = "Asslo", schedule = Schedule(6, 2)),
            Employee(name = "Alexander", surname = "Kinka", schedule = Schedule(3, 3), Room.SECOND_FLOOR_OLD),
            Employee(name = "John", surname = "Bil", schedule = Schedule(7, 1), Room.THIRD_FLOOR_NEW),
            Employee(name = "Will", surname = "Rollin", schedule = Schedule(3, 2)),
        )
        calculator.calculate(employees)
    }
}
