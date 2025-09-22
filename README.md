# NichirinScript

NichirinScript es un lenguaje temático inspirado en *Kimetsu no Yaiba* que se
apoya totalmente en el intérprete estándar de Python. El repositorio incluye
un traductor y un pequeño runtime que permiten escribir archivos `.nichirin`
utilizando vocabulario del anime y ejecutarlos como si fueran programas
Python.

## Características del lenguaje

Las reglas son ligeras y buscan evocar el mundo de los cazadores de demonios
sin dejar de lado la sintaxis conocida de Python. Estas son las principales
construcciones disponibles:

| NichirinScript                           | Python equivalente        | Descripción |
| ---------------------------------------- | ------------------------- | ----------- |
| `aliento nombre(parametros):`            | `def nombre(parametros):` | Declara funciones utilizando el término “aliento”. |
| `pilar Nombre(Hashira):`                 | `class Nombre(Hashira):`  | Define clases llamadas “pilares”. |
| `si cazador condicion:`                  | `if condicion:`           | Condicional principal. |
| `sino hashira condicion:`                | `elif condicion:`         | Rama intermedia del condicional. |
| `sino demonio:`                          | `else:`                   | Rama final. |
| `mision x en rango(...):`                | `for x in range(...):`    | Bucle `for`. Se acepta cualquier iterable tras `en`. |
| `infinito mientras condicion:`           | `while condicion:`        | Bucle `while`. |
| `respiracion total` / `respiracion constante` | `while True`             | Bucle infinito listo para romperse con `mata demonio`. |
| `respiracion total concentracion:`       | `while True:`             | Otra forma épica de iniciar un bucle infinito. |
| `kagura("mensaje")`                    | `print("mensaje")`       | Envía mensajes, inspirado en la Danza del Dios del Fuego. |
| `cuervo("texto")`                      | `input("texto")`         | Solicita datos como los cuervos mensajeros. |
| `regresa valor`                          | `return valor`            | Retorno de funciones. |
| `mata demonio`                           | `break`                   | Sale de bucles. |
| `persevera cazador`                      | `continue`                | Salta a la siguiente iteración. |
| `juramento modulo`                       | `import modulo`           | Importa módulos estándar de Python. |
| `juramento de la/las/los paquete`        | `from paquete`            | Import estilo `from`. |

Cualquier otro elemento (operadores, literales, indentación, comentarios con
`#`, etc.) se comporta exactamente igual que en Python, lo que facilita la
creación de scripts elaborados manteniendo la ambientación del anime.

## Runtime con sabor a Kimetsu

El paquete proporciona un módulo `nichirinscript.runtime` que expone utilidades
para modelar respiraciones y técnicas:

```python
from nichirinscript.runtime import (
    Tecnica,
    Respiracion,
    tecnica,
    forjar_respiration,
    invocar_respiration,
    grito_de_guerra,
)
```

Estas funciones permiten registrar respiraciones, listar técnicas y producir
mensajes temáticos dentro de los programas traducidos.

## Ejecución

El repositorio incluye un ejecutor CLI. Una vez dentro del directorio del
proyecto se puede lanzar un archivo NichirinScript así:

```bash
python -m nichirinscript ruta/al/programa.nichirin
```

Opcionalmente, añade `--emit-python` para imprimir el código Python traducido
antes de ejecutarlo.

## Ejemplo

El archivo [`examples/mision_principal.nichirin`](examples/mision_principal.nichirin)
demuestra varios elementos del lenguaje:

```text
juramento math

aliento danza_del_dios_del_fuego(nombre_cazador):
    kagura(grito_de_guerra(nombre_cazador))
    respiracion = invocar_respiration("Solar")
    si cazador respiracion:
        si cazador respiracion.tecnicas:
            kagura("Formas heredadas:")
            mision forma en respiracion.desplegar():
                kagura(f" - {forma}")
        sino demonio:
            kagura("Tanjiro aún no ha dominado ninguna forma.")

si cazador __name__ == "__main__":
    forjar_respiration(
        "Solar",
        "Kamado Tanjiro",
        [
            tecnica("Primera Forma", "Golpe circular ardiente"),
            tecnica("Tercera Forma", "Danza purificadora"),
        ],
    )

    danza_del_dios_del_fuego("Kamado Tanjiro")
```

Al ejecutarlo, verás una salida completamente funcional gracias a que el
traductor entrega Python válido al intérprete.

## Pruebas

Ejecuta `pytest` para comprobar que el traductor respeta las reglas anteriores.
