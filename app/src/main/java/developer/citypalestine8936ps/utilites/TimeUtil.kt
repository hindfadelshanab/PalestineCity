package developer.citypalestine8936ps.utilites

import java.text.SimpleDateFormat
import java.util.*

fun Long.toConversationDateFormat(): String {
    val datePattern = "MM-dd-yyyy"
    val simpleDateFormat = SimpleDateFormat(datePattern, Locale.getDefault())
    val date = simpleDateFormat.format(Date(this))
    return date
}

fun Long.toConversationTimeFormat(): String {
    val timePattern = "HH:mm"
    val simpleTimeFormat = SimpleDateFormat(timePattern, Locale.getDefault())
    val time = simpleTimeFormat.format(Date(this))
    return time
}
