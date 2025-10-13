# AI Nexus Hub

Aplicación Android nativa desarrollada en Kotlin que muestra un hub de herramientas y noticias de Inteligencia Artificial. Implementa arquitectura MVVM con ViewModel + LiveData, consumo de datos con Retrofit/Moshi y persistencia local mediante Room.

## Características principales

- **Home** con buscador, recomendaciones diarias y lista de las IAs más populares.
- **Noticias** con feed actualizado de artículos sobre Inteligencia Artificial (fuente: [HN Algolia](https://hn.algolia.com/api)).
- **Favoritos** con almacenamiento local de herramientas preferidas mediante Room.
- **BottomNavigationView** con pestañas IAs, Noticias y Favoritos.
- **Splash screen** animado con branding de la app.
- **Modo oscuro** basado en Material Design 3.

## Datos remotos

- Herramientas IA: se consultan desde un JSON remoto (`https://raw.githubusercontent.com/AI-Nexus-Hub/data/main/tools.json`). Si la petición falla, la app utiliza el archivo local `assets/tools.json` incluido en el proyecto.
- Noticias: se obtienen desde la API pública de [HN Algolia](https://hn.algolia.com/api/v1/search_by_date?query=artificial%20intelligence&tags=story&hitsPerPage=20).

## Estructura técnica

- Lenguaje: **Kotlin**
- Arquitectura: **MVVM** con `ViewModel` + `LiveData`
- UI: **XML** + Material Design 3 + ViewBinding
- Networking: **Retrofit** + **Moshi**
- Persistencia: **Room Database**
- Carga de imágenes: **Coil**
- Navegación: **Navigation Component**

## Ejecución

1. Abrir el proyecto en **Android Studio Iguana o superior**.
2. Sincronizar Gradle (AGP 8.6, Kotlin 1.9).
3. Ejecutar la app en un dispositivo o emulador con Android 7.0 (API 24) o superior.

## Notas

- Para apuntar a un endpoint propio de herramientas IA, actualiza la constante `TOOLS_BASE_URL` en `AiNexusHubApp.kt`.
- Los assets `tools.json` y `recommendations.json` sirven como fallback local y ejemplos de datos.

¡Disfruta explorando el AI Nexus Hub! 🚀
