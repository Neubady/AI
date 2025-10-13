import os
from pathlib import Path
from dominio.alumno import Alumno

class AlumnosMatriculados:
    """
    Operaciones sobre el fichero de alumnos.
    """
    # Ruta del archivo en la raíz del proyecto
    _BASE_DIR = Path(__file__).resolve().parents[1]
    ruta_archivo: Path = _BASE_DIR / "alumnos.txt"

    @staticmethod
    def matricular_alumno(alumno: Alumno) -> None:
        """Añade un alumno al archivo (append)."""
        nombre = alumno.nombre
        if not nombre:
            print("⚠️ Nombre vacío: no se ha matriculado.")
            return
        try:
            with open(AlumnosMatriculados.ruta_archivo, "a", encoding="utf-8") as f:
                f.write(nombre + "\n")
            print(f"✅ Alumno '{nombre}' matriculado correctamente.")
        except OSError as e:
            print(f"❌ Error al matricular: {e}")

    @staticmethod
    def listar_alumnos() -> None:
        """Lista los alumnos matriculados leyendo el fichero línea a línea."""
        try:
            if not os.path.exists(AlumnosMatriculados.ruta_archivo):
                print("⚠️ No hay alumnos matriculados (fichero inexistente).")
                return
            with open(AlumnosMatriculados.ruta_archivo, "r", encoding="utf-8") as f:
                lineas = [l.strip() for l in f.readlines() if l.strip()]
            if not lineas:
                print("⚠️ El archivo está vacío.")
                return
            print("\n📋 Alumnos matriculados:")
            for i, nombre in enumerate(lineas, start=1):
                print(f"{i}. {nombre}")
        except OSError as e:
            print(f"❌ Error al leer: {e}")

    @staticmethod
    def eliminar_alumnos() -> None:
        """Elimina el archivo de alumnos si existe."""
        try:
            if os.path.exists(AlumnosMatriculados.ruta_archivo):
                os.remove(AlumnosMatriculados.ruta_archivo)
                print("🗑️ Archivo de alumnos eliminado.")
            else:
                print("⚠️ No existe el archivo de alumnos.")
        except OSError as e:
            print(f"❌ Error al eliminar: {e}")
