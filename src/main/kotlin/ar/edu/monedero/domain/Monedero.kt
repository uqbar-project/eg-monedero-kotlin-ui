package ar.edu.monedero.domain

import ar.edu.monedero.exceptions.BusinessException
import java.math.BigDecimal

class Monedero(montoInicial: Double) {

    var monto: BigDecimal = BigDecimal(montoInicial)
        private set

    fun poner(cuanto: BigDecimal) {
        validarMonto(cuanto)
        sumarMonto(cuanto)
    }

    fun sacar(cuanto: BigDecimal) {
        validarMonto(cuanto)
        if (monto < cuanto) {
            throw BusinessException("No puede sacar más de $monto $")
        }
        sumarMonto(cuanto.negate())
    }

    private fun sumarMonto(valor: BigDecimal) {
        monto = monto.add(valor)
    }

    private fun validarMonto(cuanto: BigDecimal) {
        if (cuanto <= BigDecimal(0)) {
            throw BusinessException("$cuanto: el monto a ingresar debe ser un valor positivo")
        }
    }

    override fun toString() = "Monedero ($ $monto)"
}
