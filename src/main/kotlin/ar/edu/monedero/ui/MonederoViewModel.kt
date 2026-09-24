package ar.edu.monedero.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ar.edu.monedero.domain.Monedero
import ar.edu.monedero.exceptions.BusinessException
import java.math.BigDecimal

/**
 * Wrapper de presentación sobre [Monedero]: es el único lugar que conoce a Compose
 * y el único lugar que atrapa [BusinessException]. El dominio no sabe que existe.
 */
class MonederoViewModel(private val monedero: Monedero) {

    var monto: BigDecimal by mutableStateOf(monedero.monto)
        private set

    var montoIngresado: String by mutableStateOf("")
        private set

    var error: String? by mutableStateOf(null)
        private set

    /** Hay algo parseable en el campo. La vista lo usa para habilitar las acciones. */
    val puedeOperar: Boolean
        get() = montoIngresado.toBigDecimalOrNull() != null

    /** Acepta un signo menos opcional, dígitos y un punto decimal opcional; cualquier otro texto se ignora. */
    fun ingresarMonto(texto: String) {
        if (FORMATO_MONTO.matches(texto)) {
            montoIngresado = texto
        }
    }

    fun poner() = operar { cuanto -> monedero.poner(cuanto) }

    fun sacar() = operar { cuanto -> monedero.sacar(cuanto) }

    fun cerrarError() {
        error = null
    }

    private fun operar(operacion: (BigDecimal) -> Unit) {
        try {
            operacion(montoIngresado.toBigDecimal())
            monto = monedero.monto
            montoIngresado = ""
        } catch (excepcion: BusinessException) {
            error = excepcion.message
        }
    }

    private companion object {
        val FORMATO_MONTO = Regex("""-?\d*(\.\d*)?""")
    }
}
