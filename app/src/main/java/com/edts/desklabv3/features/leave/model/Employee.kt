package com.edts.desklabv3.features.leave.ui.laporantim

data class Employee(
    val employeeImg: Int,
    val employeeName: String,
    val employeeRole: String,
    val counterText: String,
    val counterDays: Int = extractDaysFromCounterText(counterText)
) {
    companion object {
        private fun extractDaysFromCounterText(counterText: String): Int {
            return try {
                counterText.replace(" Hari", "").toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }
    }
}