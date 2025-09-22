"""NichirinScript translator.

This module turns NichirinScript source code into valid Python code. The
translator is intentionally lightweight: the syntax sugar is replaced via
pattern rules so that the resulting program can be executed directly by the
standard Python interpreter.
"""
from __future__ import annotations

from dataclasses import dataclass, field
import re
from typing import Iterable, List


@dataclass
class NichirinTranslator:
    """Translate NichirinScript code into Python."""

    prefix_rules: List[tuple[str, str]] = field(default_factory=lambda: [
        ("juramento de las ", "from "),
        ("juramento de los ", "from "),
        ("juramento de la ", "from "),
        ("juramento ", "import "),
        ("aliento ", "def "),
        ("pilar ", "class "),
        ("si cazador ", "if "),
        ("sino hashira ", "elif "),
        ("sino demonio", "else"),
        ("infinito mientras ", "while "),
        ("respiracion total concentracion", "while True"),
        ("regresa ", "return "),
        ("mata demonio", "break"),
        ("persevera cazador", "continue"),
    ])

    regex_rules: List[tuple[re.Pattern[str], str]] = field(default_factory=lambda: [
        (re.compile(r"\bmision (?P<var>[A-Za-z_][\w]*)\s+en\b"), r"for \g<var> in"),
        (re.compile(r"(?<!\w)kagura(?=\s*\()"), "print"),
        (re.compile(r"(?<!\w)cuervo(?=\s*\()"), "input"),
        (re.compile(r"\brespiracion total\b"), "while True"),
        (re.compile(r"\brespiracion constante\b"), "while True"),
        (re.compile(r"\brango\b"), "range"),
    ])

    def translate_line(self, line: str) -> str:
        """Translate a single line preserving indentation."""
        if not line.strip():
            return line

        indent_match = re.match(r"^\s*", line)
        indent = indent_match.group(0) if indent_match else ""
        stripped = line[len(indent):]

        for prefix, replacement in self.prefix_rules:
            if stripped.startswith(prefix):
                stripped = replacement + stripped[len(prefix):]
                break

        translated = stripped
        for pattern, replacement in self.regex_rules:
            translated = pattern.sub(replacement, translated)

        return f"{indent}{translated}"

    def translate(self, source: Iterable[str]) -> str:
        """Translate an iterable of lines into executable Python code."""
        return "".join(self.translate_line(line) for line in source)


def translate_text(text: str) -> str:
    """Helper to translate NichirinScript text into Python code."""
    translator = NichirinTranslator()
    return translator.translate(text.splitlines(keepends=True))


__all__ = ["NichirinTranslator", "translate_text"]
