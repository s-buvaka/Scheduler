package com.three.sister.scheduler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.apache.poi.ss.usermodel.Workbook


class MainActivity : AppCompatActivity() {

    private val employeeAdapter = EmployeeAdapter()
    private val employees: MutableList<Employee> = mutableListOf()
    private val calculator = ScheduleCalculator()
    private val excelFileGenerator = ExcelFileGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ADD_EMPLOYEE_REQUEST_CODE && data != null) {
            employees.add(data.getSerializableExtra(AddEmployeeActivity.EMPLOYEE_DATA) as Employee)
            employeeAdapter.update(employees)
        }
    }

    private fun initViews() {
        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener {
            startActivityForResult(
                AddEmployeeActivity.createIntent(this),
                ADD_EMPLOYEE_REQUEST_CODE
            )
        }
        initRecycler()
        initgenerateButton()
    }

    private fun initgenerateButton() {
        findViewById<Button>(R.id.generateButton).setOnClickListener {
            val matrix = calculator.calculate(employees)
            val workbook: Workbook = excelFileGenerator.generateWorkBook(matrix)

            val intentShareFile = Intent(Intent.ACTION_SEND)
            val fileWithinMyDir = excelFileGenerator.getExcelFile(workbook, this, "Schedule")

            val sharing = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", fileWithinMyDir)
            if (fileWithinMyDir.exists()) {
                intentShareFile.type = "application/xls"
                intentShareFile.putExtra(Intent.EXTRA_STREAM, sharing)
                intentShareFile.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Sharing File..."
                )
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
                startActivity(Intent.createChooser(intentShareFile, "Share File"))
            }
        }
    }

    private fun initRecycler() {
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.adapter = employeeAdapter
    }

    companion object {

        private const val ADD_EMPLOYEE_REQUEST_CODE = 100
    }
}
