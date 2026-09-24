# Monedero - Kotlin + Compose for Desktop

[![Build](https://github.com/uqbar-project/eg-monedero-kotlin-ui/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-monedero-kotlin-ui/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/uqbar-project/eg-monedero-kotlin-ui/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-monedero-kotlin-ui)

Port a Kotlin del ejercicio [eg-monedero-xtend](https://github.com/uqbar-project/eg-monedero-xtend) (branch `simple`),
con una UI desktop hecha en [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).

## Conceptos a ver

- Manejo de errores con excepciones de negocio (`BusinessException`).
- Testeo unitario de los errores, con [Kotest](https://kotest.io/) en estilo `DescribeSpec`.
- Cómo mostrar una excepción de negocio como popup sin acoplar el dominio a la vista.

## Estructura

- `domain` / `exceptions`: el modelo, idéntico al original. No importa nada de Compose.
- `ui/MonederoViewModel`: wrapper de presentación. Es el único lugar que atrapa `BusinessException` y la deja en un estado observable (`error`).
- `ui/MonederoApp`: la ventana. Cuando `error` no es nulo, muestra un `AlertDialog`.

## Cómo correrlo

```bash
./gradlew run     # abre la ventana
./gradlew test    # corre los tests de dominio y de view model
```

Requiere JDK 21.

## CI

GitHub Actions corre `./gradlew build` (compila y ejecuta los tests) en cada push a `master` y en cada pull request.
Si falla, el reporte HTML de los tests queda disponible como artifact del workflow.

La cobertura se mide con [Kover](https://github.com/Kotlin/kotlinx-kover) y se publica en [Codecov](https://codecov.io/gh/uqbar-project/eg-monedero-kotlin-ui).
Para verla en local:

```bash
./gradlew koverHtmlReport
open build/reports/kover/html/index.html
```
