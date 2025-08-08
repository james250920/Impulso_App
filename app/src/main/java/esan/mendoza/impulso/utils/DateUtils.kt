package esan.mendoza.impulso.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    // Formato estándar para guardar en la base de datos (ISO 8601)
    private val databaseDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    // Formato para mostrar al usuario (más legible)
    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    // Formato para mostrar solo la fecha
    private val displayDateOnlyFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Formato para mostrar fecha relativa
    private val monthYearFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    init {
        // Configurar zona horaria para el formato de base de datos
        databaseDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    /**
     * Obtiene la fecha y hora actual en formato estándar para la base de datos
     */
    fun getCurrentDateTimeString(): String {
        return databaseDateFormat.format(Date())
    }

    /**
     * Obtiene solo la fecha actual en formato estándar
     */
    fun getCurrentDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * Convierte una fecha de string a Date
     */
    fun parseDate(dateString: String): Date? {
        return try {
            when {
                // Formato ISO completo
                dateString.contains("T") && dateString.contains("Z") ->
                    databaseDateFormat.parse(dateString)
                // Formato de solo fecha
                dateString.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) ->
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formatea una fecha para mostrar al usuario de forma legible
     */
    fun formatForDisplay(dateString: String): String {
        val date = parseDate(dateString)
        return if (date != null) {
            displayDateFormat.format(date)
        } else {
            dateString // Si no se puede parsear, devolver el string original
        }
    }

    /**
     * Formatea una fecha para mostrar solo el día/mes/año
     */
    fun formatDateOnly(dateString: String): String {
        val date = parseDate(dateString)
        return if (date != null) {
            displayDateOnlyFormat.format(date)
        } else {
            dateString
        }
    }

    /**
     * Formatea una fecha de forma relativa (hace X días, esta semana, etc.)
     */
    fun formatRelativeDate(dateString: String): String {
        val date = parseDate(dateString) ?: return dateString
        val now = Date()
        val diffInMillis = now.time - date.time
        val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)

        return when {
            diffInDays == 0L -> "Hoy"
            diffInDays == 1L -> "Ayer"
            diffInDays < 7L -> "Hace ${diffInDays} días"
            diffInDays < 30L -> "Hace ${diffInDays / 7} semanas"
            diffInDays < 365L -> monthYearFormat.format(date)
            else -> "Hace ${diffInDays / 365} años"
        }
    }

    /**
     * Verifica si una fecha es de hoy
     */
    fun isToday(dateString: String): Boolean {
        val date = parseDate(dateString) ?: return false
        val today = Calendar.getInstance()
        val dateCalendar = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * Verifica si una fecha es de esta semana
     */
    fun isThisWeek(dateString: String): Boolean {
        val date = parseDate(dateString) ?: return false
        val today = Calendar.getInstance()
        val dateCalendar = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.WEEK_OF_YEAR) == dateCalendar.get(Calendar.WEEK_OF_YEAR)
    }
}
