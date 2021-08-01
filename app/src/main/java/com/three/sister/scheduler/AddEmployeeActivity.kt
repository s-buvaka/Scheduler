package com.three.sister.scheduler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
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
    private lateinit var maxDaysToggle: SwitchCompat
    private lateinit var weekdaysTitle: TextView
    private lateinit var weekendsTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        initViews()
        addValidation()

        confirmButton.setOnClickListener {
            val schedule = if (maxDaysToggle.isEnabled) {
                Schedule.MAX
            } else {
                Schedule(
                    weekdays = weekdaysInput.text.toString().toInt(),
                    weekends = weekendsInput.text.toString().toInt()
                )
            }
            val priority = Room.values().find { it.title == roomSpinner.selectedItem.toString() }
            val employee = Employee(
                name = nameInput.text.toString(),
                surname = surnameInput.text.toString(),
                schedule = schedule,
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
                (weekdaysInput.text?.isNotEmpty() == true || maxDaysToggle.isEnabled) &&
                (weekendsInput.text?.isNotEmpty() == true || maxDaysToggle.isEnabled)

        confirmButton.isEnabled = isButtonEnabled
    }

    private fun initViews() {
        nameInput = findViewById(R.id.nameInput)
        surnameInput = findViewById(R.id.surnameInput)
        weekdaysInput = findViewById(R.id.weekdaysInput)
        weekendsInput = findViewById(R.id.weekendsInput)
        roomSpinner = findViewById(R.id.roomSpinner)
        confirmButton = findViewById(R.id.confirmButton)
        maxDaysToggle = findViewById(R.id.maxDaysToggle)
        weekdaysTitle = findViewById(R.id.weekdaysTitle)
        weekendsTitle = findViewById(R.id.weekendsTitle)

        maxDaysToggle.setOnCheckedChangeListener { _, isChecked ->
            weekdaysTitle.isVisible = !isChecked
            weekendsTitle.isVisible = !isChecked
            weekdaysInput.isVisible = !isChecked
            weekendsInput.isVisible = !isChecked
        }
    }

    companion object {

        const val EMPLOYEE_DATA = "employee_data"

        fun createIntent(context: Context): Intent =
            Intent(context, AddEmployeeActivity::class.java)
    }
}
