class Alumno:
    """Representa a un objeto alumno."""
    def __init__(self, nombre: str):
        self.nombre = nombre.strip()

    def __str__(self) -> str:
        return f"Alumno: {self.nombre}"
