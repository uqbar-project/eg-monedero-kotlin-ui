package ar.edu.monedero.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import ar.edu.monedero.domain.Monedero
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalTestApi::class)
class MonederoScreenTest : DescribeSpec({

    describe("Dada la pantalla del monedero") {

        it("muestra el saldo inicial") {
            pantalla {
                onNodeWithText("$ 100").assertIsDisplayed()
            }
        }

        it("sin monto cargado las acciones están deshabilitadas") {
            pantalla {
                accionesDeshabilitadas()
            }
        }

        it("con sólo un signo o un punto siguen deshabilitadas") {
            pantalla {
                campoMonto().performTextInput("-")
                accionesDeshabilitadas()
                campoMonto().performTextClearance()
                campoMonto().performTextInput(".")
                accionesDeshabilitadas()
            }
        }

        it("tipear un monto habilita las acciones") {
            pantalla {
                campoMonto().performTextInput("50")
                onNodeWithText("Poner").assertIsEnabled()
                onNodeWithText("Sacar").assertIsEnabled()
            }
        }

        it("borrar el monto vuelve a deshabilitarlas") {
            pantalla {
                campoMonto().performTextInput("50")
                campoMonto().performTextClearance()
                accionesDeshabilitadas()
            }
        }

        it("poner plata actualiza el saldo y limpia el campo") {
            pantalla { viewModel ->
                campoMonto().performTextInput("50")
                onNodeWithText("Poner").performClick()
                onNodeWithText("$ 150").assertIsDisplayed()
                accionesDeshabilitadas()
                viewModel.montoIngresado shouldBe ""
            }
        }

        it("sacar plata actualiza el saldo") {
            pantalla {
                campoMonto().performTextInput("30")
                onNodeWithText("Sacar").performClick()
                onNodeWithText("$ 70").assertIsDisplayed()
            }
        }

        it("la excepción de negocio se muestra como popup") {
            pantalla {
                campoMonto().performTextInput("500")
                onNodeWithText("Sacar").performClick()
                onNodeWithText("Error").assertIsDisplayed()
                onNodeWithText("No puede sacar más de 100 $").assertIsDisplayed()
                onNodeWithText("$ 100").assertIsDisplayed()
            }
        }

        it("aceptar cierra el popup") {
            pantalla { viewModel ->
                campoMonto().performTextInput("500")
                onNodeWithText("Sacar").performClick()
                onNodeWithText("Aceptar").performClick()
                onNodeWithText("Error").assertDoesNotExist()
                viewModel.error.shouldBeNull()
            }
        }

        it("el campo ignora texto no numérico") {
            pantalla { viewModel ->
                campoMonto().performTextInput("abc")
                accionesDeshabilitadas()
                viewModel.montoIngresado shouldBe ""
            }
        }
    }
})

/** Monta [MonederoScreen] con un monedero de $ 100 en una escena offscreen y ejecuta el bloque sobre ella. */
@OptIn(ExperimentalTestApi::class)
private fun pantalla(bloque: ComposeUiTest.(MonederoViewModel) -> Unit) = runComposeUiTest {
    val viewModel = MonederoViewModel(Monedero(100.0))
    setContent {
        MaterialTheme {
            MonederoScreen(viewModel)
        }
    }
    bloque(viewModel)
}

@OptIn(ExperimentalTestApi::class)
private fun ComposeUiTest.campoMonto() = onNode(hasSetTextAction())

@OptIn(ExperimentalTestApi::class)
private fun ComposeUiTest.accionesDeshabilitadas() {
    onNodeWithText("Poner").assertIsNotEnabled()
    onNodeWithText("Sacar").assertIsNotEnabled()
}
