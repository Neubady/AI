# FlowPulse for n8n (Android)

FlowPulse for n8n is a Kotlin/Jetpack Compose application designed to monitor, control, and receive alerts from one or more self-hosted n8n instances. The project is production-ready for Google Play distribution, integrates monetisation (Google AdMob + Play Billing v6), supports Firebase Cloud Messaging (with WorkManager fallback), follows Clean Architecture with MVVM/Hilt, and includes full release guidance.

---

## 1. Requisitos previos

| Herramienta | Versión recomendada |
|-------------|---------------------|
| Android Studio | Iguana (2023.2.1) o superior |
| JDK | 17 (incluido en Android Studio) |
| Cuenta Firebase | Para FCM y Analytics |
| Cuenta Google Play Console | Publicación y facturación |
| Cuenta AdMob | Monetización con anuncios |
| Facturación Google Play | Activada en Play Console |

Asegúrate también de tener configurado `JAVA_HOME` y el SDK de Android 35 descargado.

---

## 2. Clonar e instalar

```bash
git clone <repo-url>
cd FlowPulse-for-n8n
cp local.properties.example local.properties
# Edita local.properties con las rutas/IDs reales
./gradlew tasks # descarga dependencias y verifica el wrapper
```

Abre el proyecto en Android Studio y espera a que Gradle sincronice.

---

## 3. Configurar Firebase (FCM)

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Añade una aplicación Android con el ID `com.flowpulse.app`.
3. Descarga `google-services.json` y colócalo en `app/google-services.json`.
4. Activa **Firebase Cloud Messaging** y, opcionalmente, **Analytics**.
5. En Android Studio, sincroniza Gradle y asegúrate de que el plugin `google-services` se aplique correctamente (ya configurado en `app/build.gradle.kts`).

---

## 4. Configurar AdMob

1. En [AdMob](https://admob.google.com/), crea una nueva aplicación Android.
2. Genera un ID de aplicación y unidades de anuncio (banner e interstitial).
3. Añade los valores a tu `local.properties`:
   ```
   APP_ADMOB_APP_ID=ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy
   ADMOB_BANNER_UNIT_ID=ca-app-pub-xxxxxxxxxxxxxxxx/zzzzzzzzzz
   ADMOB_INTERSTITIAL_UNIT_ID=ca-app-pub-xxxxxxxxxxxxxxxx/aaaaaaaaaa
   ```
4. En `AndroidManifest`, los `manifestPlaceholders` insertarán automáticamente los IDs.
5. Durante desarrollo se emplean IDs de prueba (Google) para evitar bloqueos.

---

## 5. Configurar Google Play Billing (v6)

1. En Google Play Console, habilita **Play Billing** y crea productos:
   * Suscripción mensual `flowpulse.pro.monthly`
   * Compra única `flowpulse.pro.lifetime`
2. Añade testers con licencias en Play Console.
3. Actualiza `local.properties` si usas IDs personalizados.
4. Para pruebas, sube un build a **Internal Testing** y prueba las compras con cuentas de prueba.

---

## 6. Seguridad de red

* En `release` la app fuerza HTTPS (`android:usesCleartextTraffic="false"`).
* El archivo `app/src/main/res/xml/network_security_config.xml` permite HTTP **solo en debug** (ver `app/src/debug/AndroidManifest.xml`).
* Puedes añadir pinning editando `CERTIFICATE_PIN_SHA256` en `gradle.properties`. Se aplica vía `CertificatePinner` en OkHttp.

---

## 7. Conectar con n8n

1. En tu instancia n8n, genera un **API Token** en `Settings → API`.
2. Desde la app (Añadir instancia):
   * Nombre descriptivo.
   * Base URL (https://mi-servidor.com/).
   * Token o credenciales Basic Auth.
   * Usa “Probar conexión” (valida `/rest/workflows`).
3. Los tokens se guardan en `EncryptedSharedPreferences` con `MasterKey` AES-256. No se registran en logs.

---

## 8. Notificaciones desde n8n

### Push (FCM)

1. Obtén el token desde Ajustes → Notificaciones (la app lo almacena localmente).
2. Crea un workflow en n8n con un nodo **HTTP Request** apuntando a `https://fcm.googleapis.com/fcm/send`.
3. Usa la clave de servidor de Firebase como Bearer.
4. Payload ejemplo:
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
5. Añade deep links si quieres abrir la pantalla de detalle (`flowpulse://execution/<instance>/<execution>`).

### Fallback WorkManager

* `ErrorPollingWorker` se programa al iniciar la app.
* Consulta `/rest/executions?status=error` cada `BuildConfig.POLL_INTERVAL_MINUTES` (ajustable en `gradle.properties`).
* Si hay nuevos errores (ID mayor al último guardado), genera una notificación local.
* Ajusta el intervalo en `DEFAULT_POLL_INTERVAL_MINUTES` para balancear batería y latencia.

---

## 9. Exportar/Importar configuración

En Ajustes puedes exportar la configuración cifrada y reimportarla (en roadmap). Las preferencias (tema, idioma, polling) se almacenan en Jetpack DataStore.

---

## 10. Firma y ofuscación

1. Genera tu keystore:
   ```bash
   keytool -genkeypair -alias flowpulse -keyalg RSA -keysize 2048 -validity 3650 \
     -keystore flowpulse-release.keystore
   ```
2. Añade a `local.properties` o `gradle.properties` (no versionado):
   ```
   RELEASE_STORE_FILE=/absolute/path/flowpulse-release.keystore
   RELEASE_STORE_PASSWORD=********
   RELEASE_KEY_ALIAS=flowpulse
   RELEASE_KEY_PASSWORD=********
   ```
3. `build.gradle.kts` usa esa configuración para `release`.
4. R8/Proguard (`app/proguard-rules.pro`) preserva modelos de serialization, Hilt, Room y SDKs (Billing, Ads, FCM).

---

## 11. Compilar release

```bash
./gradlew clean bundleRelease
```

> ℹ️ El repositorio no incluye `gradle-wrapper.jar` para evitar binarios. El script `./gradlew` descargará automáticamente la distribución declarada en `gradle/wrapper/gradle-wrapper.properties` la primera vez que se ejecute (requiere `curl` o `wget`).

* Obtendrás `app/build/outputs/bundle/release/app-release.aab`.
* Sube a Play Console → `Production/Internal testing` → crea release, añade notas, políticas y rating de contenido.

---

## 12. Privacidad y seguridad

* **Tokens** y secretos encriptados con `EncryptedSharedPreferences`.
* **Exportación cifrada** (planificada) y copia de seguridad controlada (`backup_rules.xml`).
* **Bloqueo biométrico/PIN** gestionado por `AppLockManager` (activable desde Ajustes → Seguridad).
* HTTPS obligatorio en release + posibilidad de pinning.

---

## 13. FAQ / Errores comunes

| Problema | Solución |
|----------|----------|
| 401/403 al validar instancia | Comprueba el token y que la URL incluya `https://`. Para Basic Auth habilita usuario/contraseña en n8n. |
| 429 Rate Limit | El repositorio aplica retry básico en códigos 5xx; considera aumentar límites en tu servidor n8n. |
| Notificaciones FCM no llegan | Verifica `google-services.json`, la clave de servidor y que el dispositivo tenga conexión. Usa logs de Firebase. |
| Billing en sandbox | Solo funciona con cuentas registradas como testers y builds subidos a Play Console. |
| Anuncios no cargan | Usa IDs de prueba en debug, revisa conectividad y cumplimiento de políticas AdMob. |
| HTTP en producción | No está permitido. Activa modo desarrollador en Ajustes solo para pruebas con HTTP. |

---

## Arquitectura y componentes clave

* **Clean Architecture** separada en `data/`, `domain/`, `ui/`.
* **DI** con Hilt (`di/NetworkModule`, `DatabaseModule`, `RepositoryModule`).
* **Networking**: Retrofit + OkHttp (interceptores de Auth, logging, pinning opcional). Serialización con `kotlinx.serialization`.
* **Almacenamiento**: Room (`FlowPulseDatabase`), DataStore (`AppPreferences`), EncryptedSharedPreferences (`SecureStorage`).
* **Concurrencia**: Kotlin Coroutines + Flow.
* **UI**: Jetpack Compose (Material3), Navigation Compose, MPAndroidChart para métricas, soporte i18n (ES/EN).
* **Monetización**: Google Mobile Ads SDK + Play Billing v6 (`BillingManager`).
* **Notificaciones**: FCM (`FlowPulseFirebaseMessagingService`) + WorkManager fallback (`ErrorPollingWorker`).
* **Seguridad**: HTTPS, pinning opcional, encriptado de tokens, R8/Proguard, control biométrico.

---

## Ejecución de pruebas

```bash
# Pruebas unitarias (JUnit5 + MockK)
./gradlew test

# Pruebas instrumentadas/Compose UI
./gradlew connectedAndroidTest
```

Incluye pruebas de casos de uso (`AddOrUpdateInstanceUseCaseTest`) y verificación de arranque (`MainActivityLaunchTest`). Añade tus propias pruebas Robolectric/E2E en `app/src/test` y `app/src/androidTest` según sea necesario.

---

## Scripts útiles

```bash
# Analizar dependencias
./gradlew app:dependencies

# Lint (no bloquea build)
./gradlew lint

# Generar informe de licencia OSS
./gradlew app:ossLicensesReport
```

---

## Estructura principal

```
app/
 ├─ src/
 │   ├─ main/
 │   │   ├─ java/com/flowpulse/app/
 │   │   │   ├─ di/…
 │   │   │   ├─ data/remote|local|repo/…
 │   │   │   ├─ domain/model|usecase/…
 │   │   │   ├─ ui/screens/… (Compose + ViewModels)
 │   │   │   ├─ workers/, notifications/, security/
 │   │   └─ res/ (Material3, strings ES/EN, network security, iconos)
 │   ├─ androidTest/ (Compose UI test)
 │   └─ test/ (JUnit + MockK)
 ├─ build.gradle.kts
 └─ proguard-rules.pro
```

---

## Licencia

MIT License – consulta `LICENSE`.

¡Listo! Sigue los pasos anteriores para configurar credenciales, compilar y publicar FlowPulse for n8n en Google Play.
