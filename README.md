# Monedero - Kotlin + Compose for Desktop

Port a Kotlin del ejercicio [eg-monedero-xtend](https://github.com/uqbar-project/eg-monedero-xtend) (branch `simple`),
con una UI desktop hecha en [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).

## Conceptos a ver

- Manejo de errores con excepciones de negocio (`BusinessException`).
- Testeo unitario de los errores.
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
