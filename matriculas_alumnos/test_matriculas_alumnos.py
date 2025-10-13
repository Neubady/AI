from dominio.alumno import Alumno
from servicio.alumnos_matriculados import AlumnosMatriculados

def mostrar_menu() -> None:
    print("""
=== GESTIÓN DE MATRÍCULAS ===
1) Matricular alumno
2) Listar alumnos
3) Eliminar archivo de alumnos
4) Salir
""")

def main() -> None:
    while True:
        mostrar_menu()
        opcion = input("Selecciona una opción (1-4): ").strip()

        if opcion == "1":
            nombre = input("Nombre del alumno: ").strip()
            AlumnosMatriculados.matricular_alumno(Alumno(nombre))

        elif opcion == "2":
            AlumnosMatriculados.listar_alumnos()

        elif opcion == "3":
            confirm = input("¿Seguro que quieres eliminar el archivo? (s/n): ").strip().lower()
            if confirm == "s":
                AlumnosMatriculados.eliminar_alumnos()
            else:
                print("Operación cancelada.")

        elif opcion == "4":
            print("👋 Saliendo del programa...")
            break

        else:
            print("❌ Opción no válida. Inténtalo de nuevo.")

if __name__ == "__main__":
    main()
