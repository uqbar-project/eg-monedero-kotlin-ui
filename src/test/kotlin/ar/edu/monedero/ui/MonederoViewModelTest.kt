package ar.edu.monedero.ui

import ar.edu.monedero.domain.Monedero
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@DisplayName("Dado el view model del monedero")
class MonederoViewModelTest {

    private lateinit var viewModel: MonederoViewModel

    @BeforeEach
    fun init() {
        viewModel = MonederoViewModel(Monedero(100.0))
    }

    @Nested
    @DisplayName("al ingresar un monto")
    inner class IngresoDeMonto {

        @DisplayName("acepta enteros")
        @Test
        fun aceptaEnteros() {
            viewModel.ingresarMonto("150")
            assertEquals("150", viewModel.montoIngresado)
        }

        @DisplayName("acepta decimales con punto, incluso mientras se está tipeando")
        @Test
        fun aceptaDecimales() {
            viewModel.ingresarMonto("12.")
            assertEquals("12.", viewModel.montoIngresado)
            viewModel.ingresarMonto("12.5")
            assertEquals("12.5", viewModel.montoIngresado)
        }

        @DisplayName("acepta negativos, para que sea el dominio quien los rechace")
        @Test
        fun aceptaNegativos() {
            viewModel.ingresarMonto("-")
            assertEquals("-", viewModel.montoIngresado)
            viewModel.ingresarMonto("-10")
            assertEquals("-10", viewModel.montoIngresado)
        }

        @DisplayName("ignora letras, comas y signos fuera de lugar, conservando lo válido anterior")
        @Test
        fun ignoraTextoNoNumerico() {
            viewModel.ingresarMonto("12")
            viewModel.ingresarMonto("12a")
            viewModel.ingresarMonto("1,5")
            viewModel.ingresarMonto("12-")
            viewModel.ingresarMonto("--12")
            assertEquals("12", viewModel.montoIngresado)
        }

        @DisplayName("ignora un segundo punto decimal")
        @Test
        fun ignoraSegundoPunto() {
            viewModel.ingresarMonto("1.5")
            viewModel.ingresarMonto("1.5.")
            assertEquals("1.5", viewModel.montoIngresado)
        }

        @DisplayName("permite borrar todo")
        @Test
        fun permiteBorrar() {
            viewModel.ingresarMonto("12")
            viewModel.ingresarMonto("")
            assertEquals("", viewModel.montoIngresado)
        }
    }

    @Nested
    @DisplayName("al operar")
    inner class Operaciones {

        @DisplayName("una operación válida actualiza el saldo y limpia el monto ingresado")
        @Test
        fun ponerActualizaElSaldo() {
            viewModel.ingresarMonto("50")
            viewModel.poner()
            assertEquals(BigDecimal(150), viewModel.monto)
            assertEquals("", viewModel.montoIngresado)
            assertNull(viewModel.error)
        }

        @DisplayName("sacar plata baja el saldo")
        @Test
        fun sacarActualizaElSaldo() {
            viewModel.ingresarMonto("30")
            viewModel.sacar()
            assertEquals(BigDecimal(70), viewModel.monto)
        }

        @DisplayName("la excepción de negocio se transforma en un error visible")
        @Test
        fun errorDeNegocioQuedaEnElEstado() {
            viewModel.ingresarMonto("500")
            viewModel.sacar()
            assertEquals("No puede sacar más de 100 $", viewModel.error)
            assertEquals(BigDecimal(100), viewModel.monto)
        }

        @DisplayName("un monto negativo lo rechaza el dominio y se ve como popup")
        @Test
        fun montoNegativoEsErrorDeNegocio() {
            viewModel.ingresarMonto("-10")
            viewModel.poner()
            assertEquals("-10: el monto a ingresar debe ser un valor positivo", viewModel.error)
            assertEquals(BigDecimal(100), viewModel.monto)
        }

        @DisplayName("un error deja el monto ingresado para poder corregirlo")
        @Test
        fun errorConservaElMontoIngresado() {
            viewModel.ingresarMonto("0")
            viewModel.poner()
            assertEquals("0", viewModel.montoIngresado)
            assertTrue(viewModel.error!!.contains("positivo"))
        }

        @DisplayName("operar sin monto informa un error")
        @Test
        fun montoVacioEsError() {
            viewModel.poner()
            assertEquals("Ingresá un monto", viewModel.error)
            assertEquals(BigDecimal(100), viewModel.monto)
        }

        @DisplayName("operar con sólo el signo menos también informa un error")
        @Test
        fun soloSignoEsError() {
            viewModel.ingresarMonto("-")
            viewModel.sacar()
            assertEquals("Ingresá un monto", viewModel.error)
        }

        @DisplayName("cerrar el error lo limpia")
        @Test
        fun cerrarErrorLoLimpia() {
            viewModel.ingresarMonto("0")
            viewModel.poner()
            viewModel.cerrarError()
            assertNull(viewModel.error)
        }
    }
}
