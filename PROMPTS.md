# Registro de asistencia

La rama se implementó con Codex a partir de la guía y de la solicitud de preparar MuroCampus paso a paso. El registro de interacciones con Gemini queda pendiente de una sesión real en Android Studio.

| Mejora realizada | Decisión técnica |
|---|---|
| Búsqueda por título | Filtrar las publicaciones ya cargadas para que escribir no inicie peticiones HTTP. La búsqueda respeta el usuario seleccionado. |
| Encabezado `Accept` | Aplicar `application/json` en un interceptor de OkHttp para centralizar el encabezado. |
| Eliminación | Revisar el estado HTTP mediante `Response<Unit>` en el repositorio, pedir confirmación y mostrar el resultado simulado en el detalle. |

## Interacciones de Gemini para la sustentación

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
