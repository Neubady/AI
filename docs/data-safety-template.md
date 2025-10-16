# FlowPulse for n8n — Data Safety Template

| Tipo de dato | Dónde se almacena | Uso | Se comparte | Opcional |
| --- | --- | --- | --- | --- |
| Token FCM | Dispositivo (EncryptedSharedPreferences) | Notificaciones push | No | Sí (se puede borrar) |
| IDs de instancia n8n | DataStore cifrada | Gestión multi-instancia | No | No |
| Tokens API n8n | EncryptedSharedPreferences | Autenticación API | No | No |
| Logs/Export JSON | Almacenamiento local/Compartir manual | Diagnóstico | Solo si el usuario comparte | Sí |
| ID de anuncios | Servicios de Google (AdMob) | Monetización (Free) | Sí (con consentimiento) | Sí |
| Datos de facturación | Google Play Billing | Gestión de compras Pro | Sí (Google) | No |

> Usa esta tabla como base para completar el formulario de Data Safety en Google Play Console.
