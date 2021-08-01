package com.three.sister.scheduler

import android.content.Context
import android.util.Log
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * @author s.buvaka
 */
class ExcelFileGenerator {

    fun generateWorkBook(matrix: List<Array<String?>>): Workbook {
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
            sheet.setColumnWidth(index, (15 * 300))
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

    fun getExcelFile(workbook: Workbook, context: Context, fileName: String): File {
        val file = File(context.getExternalFilesDir(null), fileName)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            Log.e("TAG", "Writing file$file")
        } catch (e: IOException) {
            Log.e("TAG", "Error writing Exception: ", e)
        } catch (e: Exception) {
            Log.e("TAG", "Failed to save file due to Exception: ", e)
        } finally {
            try {
                fileOutputStream?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return file
    }
}
