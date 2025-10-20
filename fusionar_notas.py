#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Utilidad para fusionar las notas de dos ficheros CSV.

Este script combina los contenidos de ``Notas_Alumnos_UF1.csv`` y
``Notas_Alumnos_UF2.csv`` en un único archivo ``notas_alumnos.csv``. Los CSV
se leen utilizando ``csv.DictReader`` y se escriben con ``csv.DictWriter``,
empleando ``;`` como delimitador y codificación ``latin-1`` para respetar
caracteres con tildes o eñes. El resultado se ordena por el identificador del
alumno y, si falta la nota de alguna unidad formativa, se deja la cadena en
blanco.

El script acepta argumentos opcionales para personalizar las rutas de entrada
y salida:

``--uf1`` Ruta al CSV de la UF1.
``--uf2`` Ruta al CSV de la UF2.
``--out`` Ruta del CSV resultante.
"""

from __future__ import annotations

import argparse
import csv
from pathlib import Path
from typing import Dict, Iterable, List


DELIMITER = ";"
ENCODING = "latin-1"

DEFAULT_UF1 = "Notas_Alumnos_UF1.csv"
DEFAULT_UF2 = "Notas_Alumnos_UF2.csv"
DEFAULT_OUTPUT = "notas_alumnos.csv"


def leer_csv(ruta: Path, campo_nota: str) -> Dict[str, Dict[str, str]]:
    """Lee un fichero CSV y devuelve un diccionario indexado por ``Id``.

    Se esperan cabeceras con los campos ``Id``, ``Apellidos``, ``Nombre`` y el
    nombre de ``campo_nota`` (``UF1`` o ``UF2``). Las cadenas se recortan para
    evitar espacios adicionales. Las filas sin identificador se omiten.
    """

    datos: Dict[str, Dict[str, str]] = {}
    with ruta.open("r", encoding=ENCODING, newline="") as descriptor:
        reader = csv.DictReader(descriptor, delimiter=DELIMITER)
        cabeceras = set(reader.fieldnames or [])
        esperados = {"Id", "Apellidos", "Nombre", campo_nota}

        if not esperados.issubset(cabeceras):
            raise ValueError(
                "El archivo {} debe contener las cabeceras: {}. Encontrado: {}".format(
                    ruta, sorted(esperados), reader.fieldnames
                )
            )

        for fila in reader:
            identificador = (fila.get("Id") or "").strip()
            if not identificador:
                continue

            datos[identificador] = {
                "Id": identificador,
                "Nombre": (fila.get("Nombre") or "").strip(),
                "Apellidos": (fila.get("Apellidos") or "").strip(),
                campo_nota: (fila.get(campo_nota) or "").strip(),
            }

    return datos


def fusionar_registros(
    uf1: Dict[str, Dict[str, str]], uf2: Dict[str, Dict[str, str]]
) -> List[Dict[str, str]]:
    """Fusiona los registros de las dos unidades formativas por ``Id``."""

    ids = set(uf1) | set(uf2)
    fusionados: List[Dict[str, str]] = []

    for identificador in ids:
        fila = {"Id": identificador, "Nombre": "", "Apellidos": "", "UF1": "", "UF2": ""}

        if identificador in uf1:
            datos_uf1 = uf1[identificador]
            fila["Nombre"] = datos_uf1.get("Nombre", "")
            fila["Apellidos"] = datos_uf1.get("Apellidos", "")
            fila["UF1"] = datos_uf1.get("UF1", "")

        if identificador in uf2:
            datos_uf2 = uf2[identificador]
            if not fila["Nombre"]:
                fila["Nombre"] = datos_uf2.get("Nombre", "")
            if not fila["Apellidos"]:
                fila["Apellidos"] = datos_uf2.get("Apellidos", "")
            fila["UF2"] = datos_uf2.get("UF2", "")

        fusionados.append(fila)

    def clave_ordenacion(registro: Dict[str, str]):
        try:
            return int(registro["Id"])
        except (TypeError, ValueError):
            return registro["Id"]

    return sorted(fusionados, key=clave_ordenacion)


def escribir_csv(ruta_salida: Path, filas: Iterable[Dict[str, str]]) -> None:
    """Escribe las filas proporcionadas en el CSV de salida."""

    cabeceras = ["Id", "Nombre", "Apellidos", "UF1", "UF2"]
    with ruta_salida.open("w", encoding=ENCODING, newline="") as descriptor:
        writer = csv.DictWriter(descriptor, fieldnames=cabeceras, delimiter=DELIMITER)
        writer.writeheader()
        for fila in filas:
            writer.writerow(fila)


def parsear_argumentos() -> argparse.Namespace:
    """Construye y procesa los argumentos de línea de comandos."""

    parser = argparse.ArgumentParser(description="Fusiona notas de UF1 y UF2 por Id.")
    parser.add_argument("--uf1", default=DEFAULT_UF1, help="Ruta del CSV de la UF1")
    parser.add_argument("--uf2", default=DEFAULT_UF2, help="Ruta del CSV de la UF2")
    parser.add_argument("--out", default=DEFAULT_OUTPUT, help="Ruta del CSV de salida")
    return parser.parse_args()


def main() -> None:
    argumentos = parsear_argumentos()

    ruta_uf1 = Path(argumentos.uf1)
    ruta_uf2 = Path(argumentos.uf2)
    ruta_salida = Path(argumentos.out)

    if not ruta_uf1.exists():
        raise FileNotFoundError(f"No se encuentra el fichero {ruta_uf1}")
    if not ruta_uf2.exists():
        raise FileNotFoundError(f"No se encuentra el fichero {ruta_uf2}")

    datos_uf1 = leer_csv(ruta_uf1, "UF1")
    datos_uf2 = leer_csv(ruta_uf2, "UF2")

    filas = fusionar_registros(datos_uf1, datos_uf2)
    escribir_csv(ruta_salida, filas)

    print(f"OK -> Generado {ruta_salida} con {len(filas)} alumnos.")


if __name__ == "__main__":
    main()
