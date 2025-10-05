# Trending Wallpapers

Aplicación Android creada con Jetpack Compose que muestra una selección curada de fondos de pantalla que están marcando tendencia y permite descargarlos directamente en el dispositivo mediante `DownloadManager`.

## Características principales

- Listado vertical con tarjetas que muestran título, categoría, fotógrafo y descripción breve de cada fondo.
- Imágenes remotas renderizadas con Coil y optimizadas para formato vertical.
- Botón de descarga con retroalimentación inmediata al usuario.
- Tematización con Material 3 y soporte para modo claro/oscuro.

## Requisitos

- Android Studio Giraffe (o superior) con JDK 17 configurado.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Acceso a Internet para cargar y descargar las imágenes.

## Ejecución

1. Clona este repositorio y ábrelo en Android Studio.
2. Sincroniza el proyecto para descargar las dependencias de Gradle.
3. Ejecuta la app en un emulador o dispositivo físico con acceso a Internet.

## Nota sobre permisos

En dispositivos con Android 10 o superior no se requiere ningún permiso adicional para descargar archivos en la carpeta pública `Pictures/TrendingWallpapers`. En versiones anteriores, el sistema solicitará permiso de almacenamiento la primera vez que se inicie la descarga.
