"""Runtime helpers for NichirinScript programs.

The runtime keeps a compact piece of Kimetsu no Yaiba lore so that programs
can interact with familiar entities.  The helpers are optional but they make
it easier to build immersive examples while still running on the Python
interpreter.
"""
from __future__ import annotations

from dataclasses import dataclass
from typing import Dict, Iterable, List


@dataclass
class Tecnica:
    """Una técnica de respiración."""

    nombre: str
    descripcion: str

    def ejecutar(self) -> str:
        return f"{self.nombre}: {self.descripcion}"


@dataclass
class Respiracion:
    """Representa un estilo de respiración."""

    estilo: str
    usuario: str
    tecnicas: List[Tecnica]

    def desplegar(self) -> List[str]:
        return [tecnica.ejecutar() for tecnica in self.tecnicas]


RESPIRACIONES: Dict[str, Respiracion] = {}


def tecnica(nombre: str, descripcion: str) -> Tecnica:
    """Crea una técnica con su descripción."""

    return Tecnica(nombre=nombre, descripcion=descripcion)


def forjar_respiration(estilo: str, usuario: str, tecnicas: Iterable[Tecnica]) -> Respiracion:
    """Registra un estilo de respiración en el compendio global."""

    registro = Respiracion(estilo=estilo, usuario=usuario, tecnicas=list(tecnicas))
    RESPIRACIONES[estilo] = registro
    return registro


def invocar_respiration(estilo: str) -> Respiracion:
    """Recupera un estilo de respiración previamente registrado."""

    try:
        return RESPIRACIONES[estilo]
    except KeyError as exc:  # pragma: no cover - defensive guard
        raise KeyError(f"No existe la respiración '{estilo}'. Usa forjar_respiration primero.") from exc


def grito_de_guerra(nombre: str) -> str:
    """Devuelve un mensaje heroico para ambientar las misiones."""

    return f"\U0001f5e1️ ¡{nombre}, que tu espada Nichirin ilumine la noche!"


__all__ = [
    "Tecnica",
    "Respiracion",
    "RESPIRACIONES",
    "tecnica",
    "forjar_respiration",
    "invocar_respiration",
    "grito_de_guerra",
]
