# Documentación Completa de AdminPanelScreen.kt

## 📋 Tabla de Contenidos
1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Arquitectura General](#arquitectura-general)
3. [Dependencias y Librerías](#dependencias-y-librer%C3%ADas)
4. [Modelos de Datos](#modelos-de-datos)
5. [ViewModel y Gestión de Estado](#viewmodel-y-gesti%C3%B3n-de-estado)
6. [Componentes UI Personalizados](#componentes-ui-personalizados)
7. [Sistema de Diseño](#sistema-de-dise%C3%B1o)
8. [Flujo de Datos Completo](#flujo-de-datos-completo)
9. [Problema Actual y Solución](#problema-actual-y-soluci%C3%B3n)
10. [Archivos Críticos](#archivos-cr%C3%ADticos)
11. [Checklist Final y Pasos Siguientes](#checklist-final-y-pasos-siguientes)

---

## 🎯 Resumen Ejecutivo

`AdminPanelScreen.kt` es la pantalla principal del panel de administración que permite gestionar:
- Mensajes Automáticos (contenidos programados)
- Actividades Programadas (eventos del programa)
- Recursos Disponibles (materiales educativos)
- Métricas (estadísticas y análisis)

Tecnologías principales:
- Jetpack Compose (UI declarativa)
- MVVM (separación de responsabilidades)
- Kotlin Coroutines + StateFlow (manejo de estado asíncrono)
- Retrofit (comunicación con backend ASP.NET Core)

---

## 🏗️ Arquitectura General

```
┌─────────────────────────────────────────────┐
│         AdminPanelScreen.kt                 │
│         (UI Layer - Compose)                │
└──────────────────┬──────────────────────────┘
                   │
                   │ viewModel.method()
                   ↓
┌─────────────────────────────────────────────┐
│       AdminPanelViewModel.kt                │
│       (Presentation Layer)                  │
│  • StateFlow<List<ContentResponse>>         │
│  • StateFlow<List<ActivityItem>>            │
│  • StateFlow<Boolean> (isLoading)           │
└──────────────────┬──────────────────────────┘
                   │
                   │ repository.get/post/put/delete()
                   ↓
┌─────────────────────────────────────────────┐
│         Repository / RetrofitService        │
│         (Data Layer)                        │
└──────────────────┬──────────────────────────┘
                   │
                   │ HTTP Request
                   ↓
┌─────────────────────────────────────────────┐
│         Backend ASP.NET Core                │
│         (MongoDB + C#)                      │
└─────────────────────────────────────────────┘
```

---

## 📦 Dependencias y Librerías

### Jetpack Compose - Layout

- androidx.compose.foundation.layout.*
- androidx.compose.foundation.lazy.LazyColumn
- androidx.compose.foundation.lazy.items

Propósito:
- `LazyColumn`: lista con scroll eficiente para renderizar contenidos, actividades y recursos
- `Column/Row/Spacer/Modifier`: layouts y espaciado


### Material Design 3

- androidx.compose.material3.* (Scaffold, TopAppBar, FloatingActionButton, AlertDialog, LinearProgressIndicator, Button, TextButton, Icon, Text)
- Iconos: ArrowBack, Logout, Add, Person

Propósito:
- Estructura de la pantalla (Scaffold), TopBar, FAB, diálogos y componentes de material


### Runtime Compose

- androidx.compose.runtime.* (remember, mutableStateOf, LaunchedEffect, collectAsState, getValue/setValue)

Propósito:
- Manejo de estado local y reactividad con StateFlow del ViewModel

---

## 📊 Modelos de Datos

> Nota importante: **El campo `usuarioId` en los DTOs de Android debe ser `String?`** para coincidir con el backend ASP.NET Core que espera un ObjectId representado como string. Si en Android está definido como lista (List<String>), producirá errores de deserialización.

### 1. ContentResponse (Backend → Android)

Ubicación esperada: `app/src/main/java/com/example/chatbot_diseo/network/dto/response/ContentResponse.kt`

Estructura recomendada:

```kotlin
data class ContentResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("diaGatillo") val diaGatillo: Int,
    @SerializedName("prioridad") val prioridad: String,
    @SerializedName("canales") val canales: List<String>,
    @SerializedName("activo") val activo: Boolean,
    @SerializedName("segmento") val segmento: String,
    @SerializedName("horaEnvio") val horaEnvio: String,
    @SerializedName("usuarioId") val usuarioId: String? // <- STRING, no List
)
```

### 2. ActivityResponse (Backend → Android)

Ubicación esperada: `app/src/main/java/com/example/chatbot_diseo/network/dto/response/ActivityResponse.kt`

Estructura recomendada:

```kotlin
data class ActivityResponse(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("dia") val dia: Int,
    @SerializedName("horaInicio") val horaInicio: String,
    @SerializedName("horaFin") val horaFin: String?,
    @SerializedName("modalidad") val modalidad: String,
    @SerializedName("lugar") val lugar: String?,
    @SerializedName("facilitador") val facilitador: String?,
    @SerializedName("capacidadMaxima") val capacidadMaxima: Int?,
    @SerializedName("participantesActuales") val participantesActuales: Int?,
    @SerializedName("temas") val temas: List<String>?,
    @SerializedName("recursos") val recursos: List<String>?,
    @SerializedName("activa") val activa: Boolean?,
    @SerializedName("usuarioId") val usuarioId: String? // <- STRING, no List
)
```

### 3. ActivityItem (Modelo simplificado para UI)

Ubicación: `app/src/main/java/com/example/chatbot_diseo/data/admin/ActivityItem.kt`

```kotlin
data class ActivityItem(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val modalidad: String
)
```

### 4. ResourceItem

Ubicación: `app/src/main/java/com/example/chatbot_diseo/data/admin/ResourceItem.kt`

(Ejemplo simplificado para UI)

```kotlin
data class ResourceItem(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val url: String,
    val tipo: String,
    val categoria: String
)
```

---

## 🧠 ViewModel y Gestión de Estado

Ubicación: `app/src/main/java/com/example/chatbot_diseo/presentation/admin/page/AdminPanelViewModel.kt`

Estados (StateFlow) observables en la UI:
- `contents: StateFlow<List<ContentResponse>>`
- `activities: StateFlow<List<ActivityItem>>`
- `resources: StateFlow<List<ResourceItem>>`
- `isLoading: StateFlow<Boolean>`
- `errorMessage: StateFlow<String?>`
- `successMessage: StateFlow<String?>`
- `selectedActivity: StateFlow<ActivityResponse?>`
- `selectedContent: StateFlow<ContentResponse?>`

Métodos usados por la UI (resumen):
- `loadAllData()`
- `getCompletionRate()`
- CRUD: `addContent`, `getContentById`, `updateContent`, `deleteContent`
- CRUD: `addActivity`, `getActivityById`, `updateActivityComplete`, `deleteActivity`
- CRUD: `addResource`, `updateResource`, `deleteResource`
- `clearSelectedActivity()`, `clearSelectedContent()`, `clearMessages()`

---

## 🎨 Componentes UI Personalizados

Lista de componentes usados por `AdminPanelScreen.kt`:

- `AdminDashboardHeader` - muestra 4 tarjetas con estadísticas
- `AdminTabs` - navegación por pestañas (Mensajes, Actividades, Recursos, Métricas)
- `AdminContentCard` - tarjeta de cada mensaje automático
- `AdminActivityCard` - tarjeta de cada actividad
- `AdminResourceCard` - tarjeta de cada recurso
- `AdminContentDialog` - diálogo crear/editar contenido
- `AdminActivityDialog2` - diálogo crear actividad
- `AdminResourceDialog` - diálogo crear/editar recurso
- `EditarActividadDialog` - diálogo editar actividad completa
- `MessageBanner` - muestra errores/éxitos
- `MetricsPage` - página con métricas (Tab 3)

Cada componente está ubicado en `app/src/main/java/com/example/chatbot_diseo/presentation/admin/components/`.

---

## 🔄 Flujo de Datos Completo

1. La UI (`AdminPanelScreen`) al iniciarse llama `viewModel.loadAllData()` dentro de `LaunchedEffect(Unit)`.
2. El `ViewModel` llama al `Repository`/`RetrofitService` para obtener:
   - `/api/contents`
   - `/api/activities`
   - `/api/resources`
3. Retrofit deserializa las respuestas JSON a los DTOs (`ContentResponse`, `ActivityResponse`, etc.).
4. Los StateFlows del ViewModel se actualizan y la UI se re-renderiza.

Punto crítico: la deserialización fallará si los tipos en los DTOs de Android no coinciden exactamente con lo que envía el backend (por ejemplo, `usuarioId` como array vs string).

---

## ⚠️ Problema Actual y Solución

### Error reportado (ejemplo)

```
Error 500 del servidor: {"message":"Error al obtener actividades","error":"An error occurred while deserializing the UsuarioId property of class ChatbotTCS.AdminAPI.Models.Actividad: Cannot deserialize a 'String' from BsonType 'Array'."}
```

### Causa raíz

- En MongoDB el documento tiene `usuarioId` como arreglo:

```json
{
  "_id": "...",
  "titulo": "Actividad de prueba",
  "usuarioId": ["673652dbf9aaf5da4ce5fc34"]
}
```

- El backend en C# espera `UsuarioId` como String (representación ObjectId):

```csharp
[BsonElement("UsuarioID")]
[BsonRepresentation(BsonType.ObjectId)]
[JsonPropertyName("usuarioId")]
public string? UsuarioId { get; set; }
```

- Si en Android el DTO está definido con `usuarioId: List<String>?`, o similar, la deserialización en el backend (al procesar o re-serializar) puede fallar, o el cliente al parsear JSON fallará.

### Solución recomendada (en Android)

Revisar los DTOs en `app/src/main/java/com/example/chatbot_diseo/network/dto/response/` y asegurarse de que `usuarioId` esté definido como `String?` donde corresponda.

Ejemplo: cambiar cualquier definición de este tipo:

```kotlin
@SerializedName("usuarioId")
val usuarioId: List<String>? = null // ❌ INCORRECTO
```

por:

```kotlin
@SerializedName("usuarioId")
val usuarioId: String? = null // ✅ CORRECTO
```

---

## 📁 Archivos Críticos

Archivos que debes revisar y/o modificar (si está el error):

- `app/src/main/java/com/example/chatbot_diseo/network/dto/response/ContentResponse.kt`  ⚠️ revisar `usuarioId`
- `app/src/main/java/com/example/chatbot_diseo/network/dto/response/ActivityResponse.kt` ⚠️ revisar `usuarioId`
- `app/src/main/java/com/example/chatbot_diseo/network/dto/response/ResourceResponse.kt` (si existe) ⚠️ revisar `usuarioId`

Archivos relacionados a la comunicación que conviene revisar:

- `app/src/main/java/com/example/chatbot_diseo/network/RetrofitService.kt`
- `app/src/main/java/com/example/chatbot_diseo/network/ApiClient.kt`

Archivos UI (no modificar salvo que sea necesario):

- `app/src/main/java/com/example/chatbot_diseo/presentation/admin/page/AdminPanelScreen.kt` (archivo analizado)
- `app/src/main/java/com/example/chatbot_diseo/presentation/admin/page/AdminPanelViewModel.kt`
- Componentes en `presentation/admin/components/`

---

## 📝 Checklist Final y Pasos Siguientes

- [ ] Verificar `ContentResponse.kt`: `usuarioId` debe ser `String?`
- [ ] Verificar `ActivityResponse.kt`: `usuarioId` debe ser `String?`
- [ ] Verificar `ResourceResponse.kt` (si aplica): `usuarioId` debe ser `String?`
- [ ] Limpiar y recompilar el proyecto en caso de cambios: `./gradlew clean build`
- [ ] Ejecutar la app y confirmar que ya no aparece el error 500
- [ ] Probar cargar lista de actividades, contenidos y recursos
- [ ] Probar crear/editar/eliminar un elemento para confirmar que la API funciona correctamente

---

## 🧾 Notas adicionales

- Guardé este documento en la carpeta `ui.theme` del proyecto para que puedas verlo y abrirlo desde Android Studio: `app/src/main/java/com/example/chatbot_diseo/ui/theme/AdminPanelScreen_Documentacion.md`.
- Si quieres, puedo:
  - Abrir y mostrar el contenido aquí mismo.
  - Aplicar el cambio necesario en los DTOs (si me autorizas a editar esos archivos).
  - Ejecutar una compilación (si quieres que lo haga desde aquí) y revisar errores.

---

Fecha de generación: 2025-12-09

Document created by assistant as requested.

