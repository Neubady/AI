# matriculas_alumnos

Pequeño proyecto de persistencia en ficheros de texto.

## Estructura
- `dominio/Alumno`: entidad simple con `nombre` y `__str__`.
- `servicio/AlumnosMatriculados`: métodos estáticos para **matricular**, **listar** y **eliminar** usando `alumnos.txt` en la raíz.
- `test_matriculas_alumnos.py`: CLI con menú (4 opciones).

## Requisitos
- Python 3.9+ (sin dependencias externas).

## Ejecutar
```bash
python test_matriculas_alumnos.py
```

El archivo alumnos.txt se creará automáticamente al matricular.
