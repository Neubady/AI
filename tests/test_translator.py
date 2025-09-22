import textwrap

import pytest

from nichirinscript.translator import translate_text


def dedent(source: str) -> str:
    return textwrap.dedent(source).lstrip("\n")


def test_basic_translation():
    source = dedent(
        """
        aliento saludo(nombre):
            kagura(f"Hola, {nombre}")
            si cazador nombre == "Tanjiro":
                regresa "Nezuko te espera"
            sino demonio:
                regresa "Hashira en camino"
        """
    )

    expected = dedent(
        """
        def saludo(nombre):
            print(f"Hola, {nombre}")
            if nombre == "Tanjiro":
                return "Nezuko te espera"
            else:
                return "Hashira en camino"
        """
    )

    assert translate_text(source) == expected


def test_loop_and_runtime_words():
    source = dedent(
        """
        mision demonio en rango(3):
            kagura(demonio)
            si cazador demonio == 1:
                persevera cazador
            si cazador demonio == 2:
                mata demonio
        """
    )

    translated = translate_text(source)
    assert "for demonio in range(3):" in translated
    assert translated.count("continue") == 1
    assert translated.count("break") == 1


def test_respiration_loop_synonyms():
    source = dedent(
        """
        respiracion total:
            mata demonio
        respiracion constante:
            mata demonio
        respiracion total concentracion:
            mata demonio
        """
    )

    translated = translate_text(source)
    assert translated.count("while True") == 3


def test_input_keyword_only_when_calling():
    source = dedent(
        """
        mensaje = "cuervo mensajero"
        respuesta = cuervo("¿Listo?")
        kagura(mensaje)
        kagura(respuesta)
        """
    )

    translated = translate_text(source)
    assert "mensaje = \"cuervo mensajero\"" in translated
    assert "respuesta = input(\"¿Listo?\")" in translated


@pytest.mark.parametrize(
    "line, expected",
    [
        ("juramento math\n", "import math\n"),
        ("juramento de la coleccion import Counter\n", "from coleccion import Counter\n"),
    ],
)
def test_import_synonyms(line, expected):
    assert translate_text(line) == expected
