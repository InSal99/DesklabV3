import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.edts.desklabv3.core.util.ListTagHandler
import java.text.SimpleDateFormat
import java.util.*

fun String.formatDateRange(endDateTime: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val startDate = format.parse(this)!!
        val endDate = format.parse(endDateTime)!!

        val indonesianLocale = Locale("id", "ID")

        if (startDate.isSameDay(endDate)) {
            SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale).format(startDate)
        } else {
            if (startDate.isSameYear(endDate)) {
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM", indonesianLocale)
                "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)} ${SimpleDateFormat("yyyy", indonesianLocale).format(endDate)}"
            } else {
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale)
                "${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}"
            }
        }
    } catch (e: Exception) {
        "Tanggal tidak valid"
    }
}

fun String.formatTimeRange(endDateTime: String): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val startDate = format.parse(this)!!
        val endDate = format.parse(endDateTime)!!

        val indonesianLocale = Locale("id", "ID")
        val timeFormat = SimpleDateFormat("HH:mm", indonesianLocale)

        "${timeFormat.format(startDate)} - ${timeFormat.format(endDate)} WIB"
    } catch (e: Exception) {
        "Waktu tidak valid"
    }
}

fun Date.isSameDay(other: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val day1 = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.time = other
    val day2 = calendar.get(Calendar.DAY_OF_YEAR)
    return day1 == day2
}

fun Date.isSameYear(other: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val year1 = calendar.get(Calendar.YEAR)
    calendar.time = other
    val year2 = calendar.get(Calendar.YEAR)
    return year1 == year2
}

fun TextView.setupHtmlDescription(htmlContent: String) {
    val preprocessed = htmlContent
        .replace("<ul>", "<myul>")
        .replace("</ul>", "</myul>")
        .replace("<ol>", "<myol>")
        .replace("</ol>", "</myol>")
        .replace("<li>", "<myli>")
        .replace("</li>", "</myli>")

    val handler = ListTagHandler(this)

    val spanned: Spanned = preprocessed.parseAsHtml(HtmlCompat.FROM_HTML_MODE_LEGACY, null, handler)
    this.text = spanned
    this.movementMethod = LinkMovementMethod.getInstance()
}