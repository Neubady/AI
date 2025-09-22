"""NichirinScript command line interface."""
from __future__ import annotations

import argparse
import pathlib
import runpy
import sys
from typing import Any, Dict

from . import runtime
from .translator import translate_text


def build_namespace(filename: str) -> Dict[str, Any]:
    """Return an execution namespace pre-populated with runtime helpers."""

    namespace: Dict[str, Any] = {
        "__name__": "__main__",
        "__file__": filename,
    }
    namespace.update({name: getattr(runtime, name) for name in runtime.__all__})
    return namespace


def execute_file(path: pathlib.Path, *, emit_python: bool = False) -> int:
    """Translate and execute a NichirinScript program."""

    source = path.read_text(encoding="utf-8")
    python_code = translate_text(source)

    if emit_python:
        sys.stdout.write(python_code)
        if not python_code.endswith("\n"):
            sys.stdout.write("\n")

    namespace = build_namespace(str(path))
    exec(python_code, namespace)  # noqa: S102 - executing trusted local source
    return 0


def execute_module(module: str) -> int:
    """Run a NichirinScript-aware Python module."""

    runpy.run_module(module, run_name="__main__")
    return 0


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(prog="nichirinscript", description="Ejecutor de NichirinScript")
    parser.add_argument("program", nargs="?", help="Archivo .nichirin a ejecutar")
    parser.add_argument(
        "-m",
        "--module",
        dest="module",
        help="Ejecuta un módulo Python como si fuese __main__",
    )
    parser.add_argument(
        "--emit-python",
        action="store_true",
        help="Muestra el código Python traducido antes de ejecutar",
    )

    args = parser.parse_args(argv)

    if args.module:
        return execute_module(args.module)

    if not args.program:
        parser.error("Debes especificar un archivo .nichirin o usar -m")

    program_path = pathlib.Path(args.program)
    if not program_path.exists():
        parser.error(f"No existe el archivo {program_path}")

    return execute_file(program_path, emit_python=args.emit_python)


if __name__ == "__main__":  # pragma: no cover - entry point
    raise SystemExit(main())
