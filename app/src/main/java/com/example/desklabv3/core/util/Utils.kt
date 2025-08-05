package com.example.desklabv3.core.util

import com.example.desklabv3.core.DatePattern
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {
    fun convertFullFormatDate(date: String?, withDay: Boolean): String {
        val tz = TimeZone.getTimeZone("Asia/Jakarta")
        val formatDateFromDB = SimpleDateFormat("yyyy-MM-dd", Locale("ID"))

        val pattern = if (withDay) "EEEE, dd MMMM yyyy" else "dd MMMM yyyy"
        val convertYearDate = SimpleDateFormat(pattern, Locale("ID"))
        convertYearDate.timeZone = tz
        return if(date != null){
            if (withDay) convertDayDate(date, formatDateFromDB, tz, convertYearDate)
            else convertYearDate.format(formatDateFromDB.parse(date) as Date)
        } else {
            "-"
        }
    }

    private fun convertDayDate(date: String, formatDateFromDB: SimpleDateFormat, tz: TimeZone, convertYearDate: SimpleDateFormat): String {
        try {
            val parsedDate = formatDateFromDB.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate!!

            val todayCalendar = Calendar.getInstance()
            todayCalendar.timeZone = tz

            val yesterdayCalendar = Calendar.getInstance()
            yesterdayCalendar.timeZone = tz
            yesterdayCalendar.add(Calendar.DATE, -1)

            return when {
                calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR) -> {
                    "Hari ini"
                }
                calendar.get(Calendar.YEAR) == yesterdayCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == yesterdayCalendar.get(Calendar.DAY_OF_YEAR) -> {
                    "Kemarin"
                }
                else -> convertYearDate.format(parsedDate)
            }
        } catch (_ : Exception) {
            return ""
        }
    }

    fun convertFullFormatDateToUTC7(date: String?, pattern: DatePattern): String {
        if (date.isNullOrEmpty()) return "-"

        val tz = TimeZone.getTimeZone("Asia/Jakarta")

        val formatDateFromDB = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale("ID"))
        formatDateFromDB.timeZone = TimeZone.getTimeZone("UTC") // Parse as UTC first

        val convertYearDate = SimpleDateFormat(pattern.toString(), Locale("ID"))
        convertYearDate.timeZone = tz

        return try {
            val parsedDate = formatDateFromDB.parse(date)
            if (parsedDate != null) convertYearDate.format(parsedDate) else "-"
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }
}