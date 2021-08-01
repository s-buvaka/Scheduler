package com.three.sister.scheduler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author s.buvaka
 */
class EmployeeAdapter : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    private var items: List<Employee> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(items: List<Employee>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val priority: TextView = itemView.findViewById(R.id.priority)
        private val schedule: TextView = itemView.findViewById(R.id.schedule)

        fun bind(employee: Employee) {
            name.text = "${employee.name} ${employee.surname}"
            priority.text = employee.priority?.title
            schedule.text = "${employee.schedule.weekdays} / ${employee.schedule.weekends}"
        }
    }
}
