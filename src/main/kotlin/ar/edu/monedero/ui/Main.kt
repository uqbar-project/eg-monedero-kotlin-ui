package ar.edu.monedero.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ar.edu.monedero.domain.Monedero

/**
 * Raíz de composición: abre la ventana y monta [MonederoScreen]. No tiene lógica,
 * por eso está excluida de la cobertura en el build.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Monedero",
        state = rememberWindowState(width = 380.dp, height = 340.dp),
    ) {
        val viewModel = remember { MonederoViewModel(Monedero(100.0)) }
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                MonederoScreen(viewModel)
            }
        }
    }
}
