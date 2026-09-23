package ar.edu.monedero.domain

import ar.edu.monedero.exceptions.BusinessException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

@DisplayName("Dado un monedero")
class MonederoTest {

    private lateinit var monedero: Monedero

    @BeforeEach
    fun init() {
        monedero = Monedero(100.0)
    }

    @DisplayName("Al poner plata sube el monto del monedero")
    @Test
    fun ponerMontoMayorACeroDebePoderse() {
        monedero.poner(BigDecimal(50))
        assertEquals(BigDecimal(150), monedero.monto)
    }

    @DisplayName("No se puede poner un monto negativo")
    @Test
    fun ponerMontoNegativoDebeFallar() {
        assertThrows<BusinessException> { monedero.poner(BigDecimal(-50)) }
    }

    @DisplayName("No se puede poner 0 pesos")
    @Test
    fun ponerCeroPesosDebeFallar() {
        assertThrows<BusinessException> { monedero.poner(BigDecimal(0)) }
    }

    @DisplayName("Al sacar plata baja el monto del monedero")
    @Test
    fun sacarMontoMayorACeroDebePoderse() {
        monedero.sacar(BigDecimal(50))
        assertEquals(BigDecimal(50), monedero.monto)
    }

    @DisplayName("No se puede sacar un monto negativo")
    @Test
    fun sacarMontoNegativoDebeFallar() {
        assertThrows<BusinessException> { monedero.sacar(BigDecimal(-50)) }
    }

    @DisplayName("No se puede sacar 0 pesos")
    @Test
    fun sacarCeroPesosDebeFallar() {
        assertThrows<BusinessException> { monedero.sacar(BigDecimal(0)) }
    }

    @DisplayName("No se puede sacar más plata de la que hay")
    @Test
    fun sacarMasPlataQueLaDisponibleDebeFallar() {
        assertThrows<BusinessException> { monedero.sacar(BigDecimal(101)) }
    }

    @DisplayName("Al sacar toda la plata el monedero debe quedar en 0 - caso borde")
    @Test
    fun sacarTodaLaPlataDejaElMonederoEnCero() {
        monedero.sacar(BigDecimal(100))
        assertEquals(BigDecimal(0), monedero.monto)
    }
}
