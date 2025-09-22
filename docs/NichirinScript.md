# NichirinScript

> "Cada línea de código es un corte. Cada variable, un respiro. Cada programa, una noche sin demonios." — Kagaya Ubuyashiki

NichirinScript es un lenguaje de programación ceremonial que emula la disciplina del Cuerpo de Exterminio de Demonios. Fue concebido en la Forja de Espadas de la Aldea Oculta, donde los herreros de nichirin colaboraron con los Kasugai para convertir los patrones de respiración en instrucciones ejecutables.

## Lore y filosofía

- **Respiración Constante**: La ejecución de un programa mantiene un flujo continuo llamado *Total Concentration Loop*. Si un hilo pierde la concentración, el demonio interno del programa (las condiciones de error) lo devora.
- **Nichirin como tipo primordial**: Cada valor emana de un color de la espada nichirin. Los colores determinan afinidades y potencias, reflejando las personalidades de slayers como Tanjiro, Zenitsu o Inosuke.
- **Kasugai Crows Runtime**: El runtime está personificado por cuervos mensajeros que anuncian eventos asíncronos y sincronizan a los Hashira (hilos maestros).
- **Respiraciones como paradigmas**: Las respiraciones clásicas representan paradigmas de programación:
  - *Respiración del Agua*: programación reactiva y fluida.
  - *Respiración del Fuego*: programación imperativa y orientada a efectos.
  - *Respiración de la Bestia*: metaprogramación y macros salvajes.
  - *Respiración del Sol*: programación funcional pura.

## Elementos fundamentales del lenguaje

| Concepto | Palabra clave | Descripción |
| --- | --- | --- |
| Declaración | `forge` | Forja una nueva variable o constante nichirin. |
| Tipos | `nichirin`, `wisteria`, `oni`, `kasumi`, `kasugai` | Enteros, cadenas, booleanos, números decimales y colecciones. |
| Función | `breath` | Declara un patrón de respiración reutilizable (función). |
| Bloque | `form { ... }` | Delimita una forma de respiración; asegura el flujo constante. |
| Retorno | `kiai` | Descarga de energía final que retorna un valor. |
| Condicional | `slay` / `else form` | Ejecuta cortes selectivos según una condición. |
| Bucle de entrenamiento | `train` | Repite un bloque hasta dominar una técnica. |
| Iteración sobre misión | `patrol` | Recorre colecciones enviadas por Kasugai. |
| Asincronía | `crowcall` | Lanza una misión que responde mediante cuervo mensajero. |

### Temperamentos de Nichirin

Cada tipo puede afinarse con un color que afecta su comportamiento:

- `nichirin flame`: enteros ardientes, ideales para conteos agresivos como el estilo de Kyojuro Rengoku.
- `nichirin mist`: enteros discretos y ocultos, usados en algoritmos sigilosos.
- `wisteria bloom`: cadenas que purifican y neutralizan estados demoníacos.
- `oni blood`: booleanos, solo admiten `slayer` (verdadero) o `demon` (falso).
- `kasugai murder`: colecciones distribuidas entre cuervos mensajeros para procesamiento paralelo.

## Gramática ceremoniosa

La sintaxis equilibra solemnidad y claridad. Algunos principios clave:

1. **Trazos kanji-simbolistas**: Las cadenas se delimitan con las comillas ceremoniales `「` y `」`.
2. **Comentarios respiratorios**: Se anotan con `// breath:` y describen la intención del Slayer.
3. **Indentación en cuatro golpes**: Cada nivel de bloque aumenta cuatro espacios, simbolizando las *Cuatro Posturas Iniciales*.
4. **Invocaciones a técnicas**: Las funciones se invocan con el operador `::` para resaltar la jerarquía maestro-estudiante.

### Estructura básica de un archivo

```nichirin
// breath: Preparar misión matutina
Hashira Flame import 「rengoku/kasugai」

forge nichirin flame misiones = 3 flame;

breath SunDawn forge nichirin flame ciclos form {
    forge wisteria bloom saludo = 「Respiración del Sol: Forma Uno」;
    Kasugai::caw(saludo);
    kiai ciclos * 108;
}

crowcall SunDawn(misiones);
```

## Control del flujo de respiración

### Condicional `slay`

```nichirin
slay oni blood presencia = detectarOni() form {
    Kasugai::caw(「Tanjiro, hay un demonio en la aldea」);
} else form {
    Kasugai::caw(「La luna está en calma」);
}
```

### Entrenamiento continuo con `train`

```nichirin
train while stamina > 0 form {
    realizarForma(「Hinokami Kagura」);
    stamina = stamina - 10;
}
```

### Patrullas sobre colecciones

```nichirin
patrol Hashira forge nichirin flame dia in 1..7 flame form {
    slay dia == 6 form {
        Kasugai::caw(「Reunión con Kagaya-sama」);
    }
}
```

## Respiraciones maestras (funciones)

Las funciones se declaran con `breath Nombre`, pueden recibir parámetros forjados y retornan mediante `kiai`.

```nichirin
breath Thunderclap forge nichirin mist latido form {
    Kasugai::caw(「Zenitsu ejecuta el Rayo」);
    kiai latido * latido;
}
```

Para métodos de Hashira específicos, se emplea el sufijo del estilo:

```nichirin
breath Water.Hashira forge nichirin mist flujo, wisteria bloom mantra form {
    Giyu::silence();
    kiai flujo ::tranquilizadoCon(mantra);
}
```

## Modularidad: Dominios y Aldeas

- **Dominios (`domain`)**: agrupan respiraciones relacionadas con un elemento (Agua, Viento, Amor).
- **Aldeas (`village`)**: encapsulan recursos físicos como forjas, wisterias o mansiones de recuperación.
- **Juramentos (`oath`)**: interfaces que prometen técnicas Hashira que otros pueden emular.

Ejemplo de declaración de dominio:

```nichirin
domain SwordsmithVillage form {
    village forge WisteriaGarden;
    oath NichirinCrafter {
        breath ForgeKatana forge nichirin flame temperatura;
    }
}
```

## Gestión de memoria y demonios

- **Luz de Wisteria**: Los valores salen de alcance liberando fragancias de wisteria, inmunes a la corrupción demoníaca.
- **Marcas del Demon Slayer**: Las optimizaciones de compilación aparecen como marcas en los hashira (hilos) y aceleran la ejecución.
- **Decapitación garantizada**: Cualquier excepción no controlada se llama `decapitation` y corta el hilo culpable, registrando el canto de Urokodaki en el log.

## Tooling del Cuerpo de Exterminio

- **Kasugai CLI**: `kasugai run misión.nichirin` ejecuta el programa con bendiciones de Kagaya.
- **Herrero de Nichirin (compilador)**: `haganezuka forge` transforma respiraciones en bytecode *Sunsteel*.
- **NichoLint**: analiza que cada forma mantenga respiración constante y que no existan `demon` sin purificar.

## Ejemplo completo

```nichirin
// breath: Gestionar una misión nocturna inspirada en los viajes de Tanjiro
Hashira Sun import 「kamado/hinokami」
Hashira Love import 「kanroji/charisma」

forge kasugai murder escuadron = {「Tanjiro」, 「Zenitsu」, 「Inosuke」};
forge wisteria bloom reporte = 「」;

breath MissionPlanner forge kasugai murder slayers form {
    patrol Hashira forge wisteria bloom miembro in slayers bloom form {
        reporte = reporte + 「
」 + miembro + 「 en posición」;
    }

    slay Love::affectionLevel() > 900 form {
        reporte = reporte + 「
Mitsuri comparte energía positiva」;
    }

    kiai reporte;
}

breath BattleLoop forge nichirin flame enemigos form {
    forge nichirin flame contador = 0 flame;

    train while contador < enemigos form {
        Kasugai::caw(「Hinokami Kagura, Forma 」 + contador);
        contador = contador + 1;
    }

    kiai contador;
}

forge wisteria bloom informeFinal = MissionPlanner(escuadron);
forge nichirin flame total = BattleLoop(12 flame);

Kasugai::caw(informeFinal);
Kasugai::caw(「Total de demonios eliminados: 」 + total);
```

## Glosario de términos

- **Kasugai**: Entidades mensajeras que representan al runtime y al sistema de registro.
- **Hashira**: Tanto los módulos principales como los hilos de ejecución. Cada Hashira mantiene su propia respiración.
- **Respiración**: Equivale a funciones o patrones de ejecución reutilizables.
- **Forma**: Bloque de código con un inicio y un cierre ceremonial.
- **Wisteria**: Recursos que purifican memoria, strings y contextos.
- **Oni**: Representan valores booleanos o procesos hostiles.
- **Hinokami Kagura**: El modo de optimización supremo que se activa cuando el compilador detecta pureza funcional.

NichirinScript está llamado a evolucionar con nuevas formas de respiración y estilos Hashira. Cada contribución debe honrar la tradición de los cazadores que, entre alientos y katanas, convirtieron su lucha eterna en un lenguaje digno de la luna escarlata.
