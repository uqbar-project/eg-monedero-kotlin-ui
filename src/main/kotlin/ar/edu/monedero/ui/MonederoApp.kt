package ar.edu.monedero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ar.edu.monedero.domain.Monedero

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

@Composable
fun MonederoScreen(viewModel: MonederoViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Saldo", style = MaterialTheme.typography.labelLarge)
        Text("$ ${viewModel.monto}", style = MaterialTheme.typography.displaySmall)

        OutlinedTextField(
            value = viewModel.montoIngresado,
            onValueChange = viewModel::ingresarMonto,
            label = { Text("Monto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = viewModel::poner, modifier = Modifier.weight(1f), enabled = viewModel.puedeOperar) {
                Text("Poner")
            }
            OutlinedButton(onClick = viewModel::sacar, modifier = Modifier.weight(1f), enabled = viewModel.puedeOperar) {
                Text("Sacar")
            }
        }
    }

    viewModel.error?.let { mensaje ->
        ErrorDialog(mensaje = mensaje, onCerrar = viewModel::cerrarError)
    }
}

@Composable
private fun ErrorDialog(mensaje: String, onCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCerrar,
        icon = { Icon(Icons.Default.Warning, contentDescription = null) },
        title = { Text("Error") },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = onCerrar) { Text("Aceptar") }
        },
    )
}
