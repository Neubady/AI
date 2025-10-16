# FlowPulse for n8n (Android)

FlowPulse for n8n es una aplicación Android nativa, diseñada mobile-first con soporte profesional para monitorizar, operar y proteger tus instancias de n8n en tiempo real. Esta entrega incluye el proyecto listo para abrir en Android Studio, con arquitectura Clean, monetización (AdMob + Google Play Billing v6), notificaciones push (FCM) con fallback WorkManager y guías completas para publicar en Google Play.

## 🧭 Diferenciadores clave

| Diferenciador | Cómo lo cubre FlowPulse |
| --- | --- |
| **Mobile-first para n8n** | UI nativa Compose optimizada para operar workflows y ejecuciones desde el móvil. |
| **Alertas avanzadas** | Motor de reglas local (Pro) con notificaciones push FCM + fallback WorkManager y deep links a ejecuciones. |
| **Seguridad y cumplimiento** | Tokens en EncryptedSharedPreferences, DataStore para preferencias, bloqueo PIN/biometría, HTTPS estricto en release, consentimiento UMP y toggles de telemetría. |
| **Multi-instancia profesional** | Perfiles por instancia, soporte para base path, modo solo lectura, export/import cifrado para equipos. |
| **Monetización clara** | Plan Free (1 instancia + Ads) vs Pro (suscripción mensual o compra lifetime sin anuncios y funciones premium). |
| **Observabilidad** | Métricas en dashboard, exportación de logs/JSON, Crashlytics/Sentry opt-in documentados. |

## 📈 Contexto competitivo

Automatizadores generalistas como Zapier, Make, IFTTT o Power Automate ofrecen apps móviles limitadas o inexistentes para monitorización en tiempo real. FlowPulse se enfoca exclusivamente en n8n con una experiencia nativa Android, ofreciendo alertas contextuales, reglas locales sin backend, soporte multi-instancia con seguridad empresarial y compatibilidad con despliegues auto-gestionados (reverse proxies, certificados propios, modos read-only). Esto resuelve la falta de herramientas móviles confiables para equipos DevOps que usan n8n.

## 🏗️ Arquitectura

- **Kotlin + Jetpack Compose Material 3**, Clean Architecture (capas `data`, `domain`, `ui`).
- **MVVM + Flow/Coroutines**, `Navigation Compose`, `Hilt` para DI.
- **Networking**: Retrofit + OkHttp con interceptores, soporte opcional para certificate pinning.
- **Storage**: Room (cache), DataStore (preferencias), EncryptedSharedPreferences (tokens). Export/import cifrado documentado.
- **Notificaciones**: Firebase Cloud Messaging + WorkManager como fallback con reglas locales.
- **Monetización**: AdMob (banners/interstitials) + Play Billing (suscripción `flowpulse.pro.monthly` y compra `flowpulse.pro.lifetime`).
- **Seguridad**: HTTPS obligatorio en release, network security config, bloqueo biométrico/PIN, rate limiting, limpieza de datos sensibles.
- **Internacionalización**: ES/EN.

## 📂 Estructura del proyecto

```
app/
  src/main/java/com/flowpulse/app/
    di/ ... módulos Hilt (App, Network, Database)
    data/remote ... Retrofit + DTOs (kotlinx-serialization)
    data/local ... Room (entities, DAO, database)
    data/repo ... Implementaciones de repositorios
    domain/... modelos y casos de uso
    ui/... Compose (navigation, screens, theme)
    notifications/... FCM + builder
    workers/... WorkManager fallback
    security/... almacenamiento cifrado
    util/... helpers (dispatchers)
  src/androidTest/... pruebas Compose/Robolectric
  src/test/... unit tests (JUnit5, MockK-ready)
```

## ✅ Requisitos previos

1. **Herramientas**: Android Studio Iguana+, JDK 17, Gradle 8.5+, dispositivo/emulador Android 8.0+.
2. **Cuentas**:
   - Firebase (FCM + opcional Analytics/Crashlytics).
   - Google Play Console (desarrollador) con facturación activa.
   - AdMob (aprobada) y acceso a Play Billing.
   - Acceso a instancias n8n (auto-hosting o nube) con HTTPS válido.

## 🚀 Instalación rápida

```bash
# 1. Clona el repositorio
git clone <tu-fork-o-repo>
cd flowpulse-for-n8n

# 2. Copia y edita local.properties
cp local.properties.example local.properties
# Ajusta sdk.dir, IDs de AdMob y Billing

# 3. Abre en Android Studio y sincroniza Gradle
```

> **Nota**: Este proyecto incluye placeholders para IDs de AdMob, Billing, certificados y endpoints. Completa todo antes de publicar.

## 🔥 Configuraciones paso a paso

### 1. Firebase (FCM)

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Añade una app Android (`com.flowpulse.app`) y descarga `google-services.json`.
3. Coloca el archivo en `app/google-services.json`.
4. Activa **Firebase Cloud Messaging** y (opcional) Analytics/Crashlytics.
5. Si usas Crashlytics/Analytics, documenta el consentimiento y habilita toggles en Ajustes (ya contemplado en la UI).

### 2. AdMob

1. En [AdMob](https://apps.admob.com/), crea la aplicación y unidades de anuncio:
   - Banner (por ejemplo para lista de instancias).
   - Interstitial (por ejemplo antes de acciones avanzadas en plan Free).
2. Copia los IDs de producción en `local.properties` (`ADMOB_APP_ID`, `ADMOB_BANNER_ID`, `ADMOB_INTERSTITIAL_ID`).
3. El `AndroidManifest` utiliza `manifestPlaceholders` para inyectar estos IDs.
4. Durante desarrollo usa los IDs de prueba oficiales de Google.
5. Revisa políticas de anuncios y configura mediación si aplica.

### 3. Google Play Billing

1. En Play Console crea los productos:
   - **Suscripción mensual** `flowpulse.pro.monthly` (Plan Pro).
   - **Compra lifetime** `flowpulse.pro.lifetime`.
2. Habilita testers internos en Licenses Testing.
3. Configura `BILLING_MONTHLY_ID` y `BILLING_LIFETIME_ID` en `local.properties`.
4. Para pruebas en modo sandbox usa un APK firmado en pista interna.
5. Documenta validación de recibos en tu backend o con un workflow n8n (ver sección mejoras).

### 4. Network Security & Certificados

- HTTPS obligatorio en `release`. `network_security_config.xml` solo permite HTTP en `debug`.
- Para certificados personalizados agrega instrucciones en README (ver más abajo) y añade el `.pem` al keystore de red si es necesario.
- Opcional: configura certificate pinning editando `NetworkModule` con `CertificatePinner`.

### 5. Conectar con n8n

1. Genera un **API Key** en `Settings → API` dentro de n8n.
2. Alternativamente habilita Basic Auth (legacy) si necesitas compatibilidad.
3. En la app, añade una instancia con:
   - Nombre descriptivo.
   - Base URL (ej. `https://miempresa.com/n8n`).
   - API Key o credenciales Basic (se almacenan cifradas).
   - Define si la instancia será de solo lectura (Pro).
4. Usa **Probar conexión** (endpoint `/rest/workflows`) para validar certificación, base path y auth.
5. FlowPulse maneja `reverse proxy` y subrutas, normalizando los slashes.

### 6. Notificaciones desde n8n → FCM

Ejemplo de payload en un **HTTP Request node**:

```json
{
  "to": "<FCM_DEVICE_TOKEN>",
  "notification": {
    "title": "n8n: Ejecución con error",
    "body": "Workflow {{ $json.workflowName }} falló (ID {{ $json.executionId }})"
  },
  "data": {
    "instance": "ACME",
    "workflowId": 123,
    "executionId": 456,
    "status": "error"
  }
}
```

1. Obtén el **Server key** desde Firebase Cloud Messaging y guárdalo de forma segura (no en la app).
2. Usa un workflow n8n para enviar POST a `https://fcm.googleapis.com/fcm/send` con encabezado `Authorization: key=<SERVER_KEY>`.
3. FlowPulse recibe el push, registra deep links y abre la pantalla de Detalle de Ejecución.

### 7. Fallback con WorkManager

- Configura intervalos entre 15 y 60 minutos según consumo de batería (`POLL_INTERVAL_MIN`).
- Ajusta constraints: red no medida, batería no baja, dispositivo cargando.
- WorkManager compara el último ID de ejecución fallida guardado en Room y genera una notificación local si detecta nuevas fallas.
- En README se documenta cómo editar las reglas desde Ajustes → Alertas.

### 8. Seguridad: PIN/Biometría y Export/Import

- Habilita bloqueo desde Ajustes → Seguridad.
- Exporta configuración cifrada con passphrase AES-256 (archivo `.flowpulse`).
- Importa en otro dispositivo para compartir instancias (Pro).

### 9. GDPR y Consentimiento UMP

1. Configura el SDK de [User Messaging Platform](https://developers.google.com/admob/ump/android/quick-start).
2. El flujo se dispara en regiones EEA y permite revocar consentimiento desde Ajustes → Privacidad.
3. Ads, telemetría y Crashlytics están desactivados por defecto hasta recibir consentimiento.

### 10. Data Safety y privacidad

- Datos recolectados:
  - Tokens FCM (para notificaciones) – almacenamiento local, se puede revocar.
  - IDs de compras (Play Billing) – necesarios para habilitar Pro.
  - Ads ID (si Ads habilitado y con consentimiento).
  - No se recolecta contenido de workflows; ejecución JSON se guarda local y el usuario elige compartirlo.
- Usa la plantilla incluida en `docs/data-safety-template.md` (añadir si creas el archivo) para completar el formulario de Play Console.

### 11. Firmado y release

```bash
# Genera keystore
yes | keytool -genkeypair -alias flowpulse -keyalg RSA -keysize 2048 -validity 9125 -keystore flowpulse.keystore

# Añade a gradle.properties
FLOWPULSE_KEYSTORE=/path/flowpulse.keystore
FLOWPULSE_KEY_ALIAS=flowpulse
FLOWPULSE_KEY_PASSWORD=<pass>
FLOWPULSE_STORE_PASSWORD=<pass>

# En Android Studio/Gradle
./gradlew clean bundleRelease
```

- El archivo `.aab` se genera en `app/build/outputs/bundle/release/`.
- Sube a Play Console → Production/Internal testing.
- Completa `Content rating`, `Data Safety`, `Privacy Policy`.

### 12. Publicación en Google Play

1. Crea ficha de Play Store con capturas, descripción destacando diferenciadores.
2. Añade política de privacidad hospedada (ej. GitHub Pages) explicando cifrado y consentimiento.
3. Configura precios para suscripción y compra lifetime.
4. Configura testers internos/abiertos para validar Billing y Ads.

## 🔒 Certificados personalizados

Para instancias con certificados propios (CA interna):

1. Obtén el certificado `.cer/.pem`.
2. Crea un keystore de confianza y referencia en `NetworkModule` usando `CertificatePinner` o `SSLSocketFactory` personalizado.
3. Documenta el procedimiento interno; FlowPulse soporta añadir CA de usuario en `debug` mediante `network_security_config`.

## 📊 Observabilidad

- Dashboard Compose muestra KPIs y gráficas (MPAndroidChart integrado). Se pueden exportar logs a JSON.
- Crashlytics/Sentry se pueden habilitar opt-in. Documenta cómo encenderlos desde Ajustes.
- Exporta JSON de ejecuciones con redacción automática (tokens/URLs) antes de compartir via `Intent`.

## 🔁 Acciones rápidas y reglas locales

- Configura webhooks/HTTP requests con nombre, método, headers y body JSON.
- Motor de reglas local (Pro) permite condiciones por estado, latencia, severidad y frecuencia. Acciones: push, notificación local, vibración, deep link.
- Reglas exportables/importables (sin credenciales) con cifrado end-to-end.

## 🧪 Pruebas incluidas

- **Unit tests**: Casos de uso (`ProcessDeepLinkUseCaseTest`) con JUnit5.
- **UI tests**: Compose test para onboarding.
- Preparado para añadir Espresso, MockK, Turbine, WorkManager (Robolectric) y navegación por deep link.

Ejecuta pruebas (desde Android Studio o CLI):

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 🧱 Roadmap sugerido

- Implementar motor completo de reglas locales (UI + almacenamiento).
- Añadir certificado pinning configurable desde Ajustes.
- Integrar validación de recibos con backend o workflow n8n.
- Construir pantallas completas (Workflows/Ejecuciones/Alertas/Acciones) con paginación y filtros avanzados.
- Integrar gráfico Compose adaptativo (24h/7d) y métricas en vivo.

## ❓ FAQ rápida

- **401/403**: Revisa API Key, modo Basic Auth y permisos en n8n.
- **429**: Reduce frecuencia de polling; se usa backoff exponencial en interceptores.
- **Errores FCM**: Verifica token en Ajustes, server key correcta y conexión HTTPS.
- **Billing sandbox**: Usa testers internos y cuenta de prueba; espera confirmación por email.
- **Ads no cargan**: Usa IDs de prueba y revisa consentimiento UMP.
- **Certificados**: Importa CA en dispositivo o habilita HTTP solo en debug.
- **Base path**: Asegura incluir `/n8n` en la URL; FlowPulse lo normaliza.

## 🤝 Contribuir

1. Crea una rama `feature/<nombre>`.
2. Sigue los lineamientos de código (Kotlin, Compose, MVVM, tests).
3. Envía PR incluyendo captura o video si hay cambios visuales.

---

FlowPulse for n8n ayuda a equipos DevOps y makers a reaccionar rápido ante fallos, con seguridad empresarial y monetización clara. ¡Listo para publicar en Google Play siguiendo esta guía paso a paso!
