package com.three.sister.scheduler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var surnameInput: TextInputEditText
    private lateinit var weekdaysInput: EditText
    private lateinit var weekendsInput: EditText
    private lateinit var roomSpinner: Spinner
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        initViews()
        addValidation()

        confirmButton.setOnClickListener {
            val priority = Room.values().find { it.title == roomSpinner.selectedItem.toString() }
            val employee = Employee(
                name = nameInput.text.toString(),
                surname = surnameInput.text.toString(),
                schedule = Schedule(
                    weekdays = weekdaysInput.text.toString().toInt(),
                    weekends = weekendsInput.text.toString().toInt()
                ),
                priority = priority
            )

            setResult(Activity.RESULT_OK, Intent().putExtra(EMPLOYEE_DATA, employee as Serializable))
            finish()
        }
    }

    private fun addValidation() {
        nameInput.addTextChangedListener(afterTextChanged = { validate() })
        surnameInput.addTextChangedListener(afterTextChanged = { validate() })
        weekdaysInput.addTextChangedListener(afterTextChanged = { validate() })
        weekendsInput.addTextChangedListener(afterTextChanged = { validate() })
    }

    private fun validate() {
        val isButtonEnabled = nameInput.text?.isNotEmpty() == true &&
                surnameInput.text?.isNotEmpty() == true &&
                weekdaysInput.text?.isNotEmpty() == true &&
                weekendsInput.text?.isNotEmpty() == true

        confirmButton.isEnabled = isButtonEnabled
    }

    private fun initViews() {
        nameInput = findViewById(R.id.nameInput)
        surnameInput = findViewById(R.id.surnameInput)
        weekdaysInput = findViewById(R.id.weekdaysInput)
        weekendsInput = findViewById(R.id.weekendsInput)
        roomSpinner = findViewById(R.id.roomSpinner)
        confirmButton = findViewById(R.id.confirmButton)
    }

    companion object {

        const val EMPLOYEE_DATA = "employee_data"

        fun createIntent(context: Context): Intent =
            Intent(context, AddEmployeeActivity::class.java)
    }
}
