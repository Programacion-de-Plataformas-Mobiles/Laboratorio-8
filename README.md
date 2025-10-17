# Laboratorio 8: Integración de Room en la Galería

Este proyecto es una aplicación de galería de fotos para Android que demuestra el uso de Room para la persistencia de datos locales. La aplicación permite a los usuarios buscar fotos, verlas en una cuadrícula, marcarlas como favoritas y ver los detalles de cada foto. También incluye un historial de búsquedas recientes.

## Decisiones de Modelado

El esquema de la base de datos se diseñó para ser simple y eficiente. Se crearon dos entidades principales:

*   `PhotoEntity`: Almacena la información de cada foto, incluyendo su ID, URL, fotógrafo y si es favorita. También incluye una clave de consulta (`query`) para asociar la foto con una búsqueda específica.
*   `RecentQueryEntity`: Almacena cada consulta de búsqueda reciente con una marca de tiempo para poder ordenar las búsquedas por uso.

## Estrategia de Caché y Paginación

La aplicación utiliza una estrategia de "network-then-persist" para la carga de datos. Cuando se realiza una búsqueda, la aplicación primero intenta obtener los datos de la red. Si la llamada a la red es exitosa, los datos se guardan en la base de datos local. Si no hay conexión a la red, la aplicación muestra los datos que están disponibles en la caché local.

La paginación no se implementó en este laboratorio, pero la estructura de la base de datos está diseñada para soportarla en el futuro. Cada `PhotoEntity` tiene un `pageIndex` que se puede usar para cargar datos de forma paginada.

## Manejo de Estado sin ViewModel

El estado de la interfaz de usuario se maneja utilizando `remember` y `mutableStateOf` de Compose. Esto permite que la aplicación sea simple y fácil de entender, sin la necesidad de un `ViewModel`. Sin embargo, para aplicaciones más grandes y complejas, se recomienda utilizar un `ViewModel` para separar la lógica de la interfaz de usuario de la lógica de negocio.

## Consideraciones de Offline y Trade-offs

La aplicación ofrece una experiencia offline básica. Los usuarios pueden ver las fotos de la última búsqueda que realizaron, así como sus fotos favoritas. Sin embargo, no pueden realizar nuevas búsquedas sin conexión a la red.

Un trade-off de este enfoque es que la caché puede volverse obsoleta si los datos en el servidor cambian. Para solucionar esto, se podría implementar una estrategia de invalidación de caché, como la caducidad por tiempo o la invalidación manual.

## Preguntas de reflexión

**Consistencia: ¿Cómo invalidarías cache por caducidad (updatedAt) o cambio de parámetros?**

Para invalidar la caché por caducidad, se podría agregar un campo `updatedAt` a la entidad `PhotoEntity`. Al cargar datos de la caché, se podría comprobar si la fecha de actualización es anterior a un umbral de tiempo determinado (por ejemplo, 24 horas). Si es así, los datos se considerarían obsoletos y se eliminarían de la caché.

Para invalidar la caché por cambio de parámetros, se podría utilizar una combinación de la consulta de búsqueda y el número de página como clave principal de la caché. Si el usuario cambia los parámetros de búsqueda (por ejemplo, agrega un filtro), se utilizaría una nueva clave de caché, lo que daría como resultado una nueva solicitud a la red.

**Offline-first: ¿Qué conflictos surgen entre cache parcial y paginado? ¿Cómo los resolverías?**

El principal conflicto que surge entre la caché parcial y el paginado es la inconsistencia de los datos. Por ejemplo, si el usuario está viendo la página 2 de los resultados de búsqueda y luego se desconecta, es posible que no pueda cargar la página 3. Además, si los datos en el servidor cambian mientras el usuario está desconectado, la caché local puede volverse obsoleta.

Para resolver estos conflictos, se podría implementar una estrategia de "offline-first" más sólida. Esto podría incluir:

*   **Precarga de datos:** La aplicación podría precargar datos adicionales en segundo plano para que estén disponibles sin conexión.
*   **Sincronización en segundo plano:** La aplicación podría sincronizar los datos en segundo plano cuando se restablezca la conexión a la red.
*   **Resolución de conflictos:** La aplicación podría implementar una estrategia de resolución de conflictos para manejar los casos en que los datos en el servidor y la caché local son diferentes.

**Escalabilidad: ¿Cuándo conviene introducir ViewModel/Repository y RemoteMediator formal en un proyecto grande?**

Conviene introducir `ViewModel`/`Repository` y `RemoteMediator` en un proyecto grande cuando la complejidad de la aplicación comienza a aumentar. Estas abstracciones ayudan a separar las preocupaciones y a que el código sea más fácil de mantener y probar.

*   **ViewModel:** El `ViewModel` es responsable de mantener el estado de la interfaz de usuario y de comunicarse con el `Repository`. Esto ayuda a separar la lógica de la interfaz de usuario de la lógica de negocio.
*   **Repository:** El `Repository` es responsable de obtener los datos de las diferentes fuentes de datos (por ejemplo, la red y la base de datos local). Esto ayuda a abstraer la lógica de acceso a los datos de la aplicación.
*   **RemoteMediator:** El `RemoteMediator` es una clase de Paging 3 que ayuda a sincronizar los datos de la red con la base de datos local. Esto es especialmente útil para aplicaciones que requieren una experiencia offline sólida.
