package esan.mendoza.impulso.presentation.component

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import esan.mendoza.impulso.data.local.entities.Recurso

object ShareHelper {

    fun shareResource(context: Context, recurso: Recurso, categoryName: String? = null) {
        val shareText = buildString {
            appendLine("📚 ${recurso.nombre}")
            appendLine()
            appendLine("📝 Descripción:")
            appendLine(recurso.descripcion)

            categoryName?.let {
                appendLine("📂 Categoría: $it")
            }

            if (recurso.link.isNotBlank()) {
                appendLine("🔗 Enlace: ${recurso.link}")
            }

            appendLine("Compartido desde Impulso - Organiza tu universo")
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Recurso: ${recurso.nombre}")
        }

        val chooserIntent = Intent.createChooser(shareIntent, "Compartir recurso")

        try {
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            // Manejar error si no hay apps disponibles para compartir
            e.printStackTrace()
        }
    }
}

@Composable
fun rememberShareHelper(): (Recurso, String?) -> Unit {
    val context = LocalContext.current

    return { recurso, categoryName ->
        ShareHelper.shareResource(context, recurso, categoryName)
    }
}
