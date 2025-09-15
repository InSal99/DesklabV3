package com.edts.desklabv3.features.leave.ui.laporantim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.leave.card.LeaveCounter
import com.edts.desklabv3.databinding.ItemLeaveCardBinding

class LeaveCardAdapter(
    private var employees: List<Employee>,
    private val onEmployeeClick: (Employee) -> Unit = {}
) : RecyclerView.Adapter<LeaveCardAdapter.LeaveCardViewHolder>() {

    class LeaveCardViewHolder(private val binding: ItemLeaveCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee, onEmployeeClick: (Employee) -> Unit) {
            binding.leaveCard.apply {
                employeeName = employee.employeeName
                employeeRole = employee.employeeRole
                counterText = employee.counterText

                counterType = if (employee.counterDays > 0) {
                    LeaveCounter.CounterType.NORMAL
                } else {
                    LeaveCounter.CounterType.CRITICAL
                }

                employeeImage = com.edts.desklabv3.R.drawable.image_avatar_placeholder

                setOnClickListener {
                    onEmployeeClick(employee)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveCardViewHolder {
        val binding = ItemLeaveCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaveCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaveCardViewHolder, position: Int) {
        holder.bind(employees[position], onEmployeeClick)
    }

    override fun getItemCount(): Int = employees.size

    fun updateEmployees(newEmployees: List<Employee>) {
        employees = newEmployees
        notifyDataSetChanged()
    }
}