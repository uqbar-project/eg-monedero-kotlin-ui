package ar.edu.monedero.domain

import ar.edu.monedero.exceptions.BusinessException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal

class MonederoTest : DescribeSpec({

    lateinit var monedero: Monedero

    beforeEach {
        monedero = Monedero(100.0)
    }

    describe("Dado un monedero") {

        it("al poner plata sube el monto del monedero") {
            monedero.poner(BigDecimal(50))
            monedero.monto shouldBe BigDecimal(150)
        }

        it("no se puede poner un monto negativo") {
            shouldThrow<BusinessException> { monedero.poner(BigDecimal(-50)) }
        }

        it("no se puede poner 0 pesos") {
            shouldThrow<BusinessException> { monedero.poner(BigDecimal(0)) }
        }

        it("al sacar plata baja el monto del monedero") {
            monedero.sacar(BigDecimal(50))
            monedero.monto shouldBe BigDecimal(50)
        }

        it("no se puede sacar un monto negativo") {
            shouldThrow<BusinessException> { monedero.sacar(BigDecimal(-50)) }
        }

        it("no se puede sacar 0 pesos") {
            shouldThrow<BusinessException> { monedero.sacar(BigDecimal(0)) }
        }

        it("no se puede sacar más plata de la que hay") {
            val excepcion = shouldThrow<BusinessException> { monedero.sacar(BigDecimal(101)) }
            excepcion.message shouldBe "No puede sacar más de 100 $"
        }

        it("al sacar toda la plata el monedero debe quedar en 0 - caso borde") {
            monedero.sacar(BigDecimal(100))
            monedero.monto shouldBe BigDecimal(0)
        }
    }
})
