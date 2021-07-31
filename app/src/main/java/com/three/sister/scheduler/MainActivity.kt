package com.three.sister.scheduler

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculator = ScheduleCalculator()

        val employees = listOf(
            Employee(name = "Sergey", surname = "Buvaka", schedule = Schedule(2, 2), Room.FIRST_FLOOR),
            Employee(name = "Anna", surname = "Smith", schedule = Schedule(2, 2), Room.LFK),
            Employee(name = "Kirill", surname = "Karl", schedule = Schedule(2, 2), Room.FOURTH_FLOOR),
            Employee(name = "Anton", surname = "Dom", schedule = Schedule(2, 1)),
            Employee(name = "Natalia", surname = "Serp", schedule = Schedule(4, 1), Room.SECOND_FLOOR_NEW),
            Employee(name = "Vera", surname = "Asslo", schedule = Schedule(6, 1)),
            Employee(name = "Nikolay", surname = "Drozdov", schedule = Schedule.MAX),
            Employee(name = "Arga", surname = "Bula", schedule = Schedule.MAX),
            Employee(name = "Fatima", surname = "Sarah", schedule = Schedule.MAX),
            Employee(name = "Arka", surname = "Dolli", schedule = Schedule.MAX),
            Employee(name = "Mira", surname = "Uber", schedule = Schedule.MAX),
        )

        val matrix = calculator.calculate(employees)

        getEmptyDays(matrix)
        writeLog(matrix)

        val workbook: Workbook = generateWorkBook(matrix)

        storeExcelInStorage(workbook, this, "Schedule")
    }

    private fun generateWorkBook(matrix: List<Array<String?>>): Workbook {
        val workbook: Workbook = HSSFWorkbook()

        val cellStyle: CellStyle = workbook.createCellStyle()
        cellStyle.fillForegroundColor = HSSFColor.CORAL.index
        cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND
        cellStyle.alignment = CellStyle.ALIGN_CENTER
        cellStyle.verticalAlignment = CellStyle.VERTICAL_CENTER

        val employeeCellStyle: CellStyle = workbook.createCellStyle()
        employeeCellStyle.alignment = CellStyle.ALIGN_CENTER
        employeeCellStyle.verticalAlignment = CellStyle.VERTICAL_CENTER

        val sheet = workbook.createSheet("Schedule")
        sheet.setColumnWidth(0, (15 * 400))
        for (index in 1..matrix.first().size) {
            sheet.setColumnWidth(index, (15 * 200))
        }

        for (roomIndex in matrix.indices) {
            val row = sheet.createRow(roomIndex + 1)
            val cell = row.createCell(0)
            cell.setCellValue(Room.values()[roomIndex].name)
        }

        val days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)


        for (roomIndex in 0..matrix.size) {
            if (roomIndex == 0) {
                val dateRow = sheet.createRow(roomIndex)
                for (date in 1..days) {
                    val cell = dateRow.createCell(date)
                    cell.setCellValue(date.toString())
                    cell.cellStyle = cellStyle
                }
            } else {
                val row = sheet.createRow(roomIndex)
                row.heightInPoints = 20F
                val roomCell = row.createCell(0)
                roomCell.setCellValue(Room.values()[roomIndex - 1].title)
                roomCell.cellStyle = cellStyle

                for (employeeIndex in matrix[roomIndex - 1].indices) {
                    val cell = row.createCell(employeeIndex + 1)
                    cell.setCellValue(matrix[roomIndex - 1][employeeIndex])
                    cell.cellStyle = employeeCellStyle
                }
            }
        }
        return workbook
    }

    private fun storeExcelInStorage(workbook: Workbook, context: Context, fileName: String): Boolean {
        var isSuccess: Boolean
        val file: File = File(context.getExternalFilesDir(null), fileName)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            Log.e("TAG", "Writing file$file")
            isSuccess = true
            finish()
        } catch (e: IOException) {
            Log.e("TAG", "Error writing Exception: ", e)
            isSuccess = false
        } catch (e: Exception) {
            Log.e("TAG", "Failed to save file due to Exception: ", e)
            isSuccess = false
        } finally {
            try {
                fileOutputStream?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return isSuccess
    }

    private fun getEmptyDays(matrix: List<Array<String?>>): Int {
        var counter = 0

        for (roomArray in matrix) {
            for (employeeName in roomArray) {
                if (employeeName == null) counter++
            }
        }

        return counter
    }

    private fun writeLog(matrix: List<Array<String?>?>) {
        val employeeMap: MutableMap<String, MutableList<Pair<Int, Room>>> = mutableMapOf()

        for (roomIndex in matrix.indices) {
            for (index in matrix[roomIndex]!!.indices) {
                if (employeeMap[matrix[roomIndex]!![index]] == null) {
                    if (matrix[roomIndex]!![index] != null) {
                        employeeMap[matrix[roomIndex]!![index]!!] = mutableListOf(index + 1 to Room.values()[roomIndex])
                    }
                } else {
                    employeeMap[matrix[roomIndex]!![index]!!]?.add(index + 1 to Room.values()[roomIndex])
                }
            }
        }

        employeeMap.keys.forEach { key ->
            employeeMap[key]?.sortBy { it.first }
        }

        Log.d("XXX", employeeMap.toString())
    }
}
