package ar.edu.monedero.ui

import ar.edu.monedero.domain.Monedero
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.math.BigDecimal

class MonederoViewModelTest : DescribeSpec({

    lateinit var viewModel: MonederoViewModel

    beforeEach {
        viewModel = MonederoViewModel(Monedero(100.0))
    }

    describe("Dado el view model del monedero") {

        describe("al ingresar un monto") {

            it("acepta enteros") {
                viewModel.ingresarMonto("150")
                viewModel.montoIngresado shouldBe "150"
            }

            it("acepta decimales con punto, incluso mientras se está tipeando") {
                viewModel.ingresarMonto("12.")
                viewModel.montoIngresado shouldBe "12."
                viewModel.ingresarMonto("12.5")
                viewModel.montoIngresado shouldBe "12.5"
            }

            it("acepta negativos, para que sea el dominio quien los rechace") {
                viewModel.ingresarMonto("-")
                viewModel.montoIngresado shouldBe "-"
                viewModel.ingresarMonto("-10")
                viewModel.montoIngresado shouldBe "-10"
            }

            it("ignora letras, comas y signos fuera de lugar, conservando lo válido anterior") {
                viewModel.ingresarMonto("12")
                viewModel.ingresarMonto("12a")
                viewModel.ingresarMonto("1,5")
                viewModel.ingresarMonto("12-")
                viewModel.ingresarMonto("--12")
                viewModel.montoIngresado shouldBe "12"
            }

            it("ignora un segundo punto decimal") {
                viewModel.ingresarMonto("1.5")
                viewModel.ingresarMonto("1.5.")
                viewModel.montoIngresado shouldBe "1.5"
            }

            it("permite borrar todo") {
                viewModel.ingresarMonto("12")
                viewModel.ingresarMonto("")
                viewModel.montoIngresado shouldBe ""
            }
        }

        describe("al operar") {

            it("una operación válida actualiza el saldo y limpia el monto ingresado") {
                viewModel.ingresarMonto("50")
                viewModel.poner()
                viewModel.monto shouldBe BigDecimal(150)
                viewModel.montoIngresado shouldBe ""
                viewModel.error.shouldBeNull()
            }

            it("sacar plata baja el saldo") {
                viewModel.ingresarMonto("30")
                viewModel.sacar()
                viewModel.monto shouldBe BigDecimal(70)
            }

            it("la excepción de negocio se transforma en un error visible") {
                viewModel.ingresarMonto("500")
                viewModel.sacar()
                viewModel.error shouldBe "No puede sacar más de 100 $"
                viewModel.monto shouldBe BigDecimal(100)
            }

            it("un monto negativo lo rechaza el dominio y se ve como popup") {
                viewModel.ingresarMonto("-10")
                viewModel.poner()
                viewModel.error shouldBe "-10: el monto a ingresar debe ser un valor positivo"
                viewModel.monto shouldBe BigDecimal(100)
            }

            it("un error deja el monto ingresado para poder corregirlo") {
                viewModel.ingresarMonto("0")
                viewModel.poner()
                viewModel.montoIngresado shouldBe "0"
                viewModel.error.shouldNotBeNull() shouldContain "positivo"
            }

            it("operar sin monto informa un error") {
                viewModel.poner()
                viewModel.error shouldBe "Ingresá un monto"
                viewModel.monto shouldBe BigDecimal(100)
            }

            it("operar con sólo el signo menos también informa un error") {
                viewModel.ingresarMonto("-")
                viewModel.sacar()
                viewModel.error shouldBe "Ingresá un monto"
            }

            it("cerrar el error lo limpia") {
                viewModel.ingresarMonto("0")
                viewModel.poner()
                viewModel.cerrarError()
                viewModel.error.shouldBeNull()
            }
        }
    }
})
