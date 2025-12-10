# 🔧 Solución: Usuarios no se cargan en el dropdown + No enviar usuarioId null

## ❌ Problemas identificados:

1. **Los usuarios no aparecen en el dropdown "Asignar usuario"**
   - El dropdown muestra "Sin asignar" pero no hay usuarios en la lista
   - No se están cargando correctamente desde el backend

2. **Se envía `usuarioId: null` en el JSON**
   - Cuando no hay usuario asignado, se envía `"usuarioId": null`
   - El usuario NO quiere enviar este campo cuando es null

## ✅ Soluciones implementadas:

### 1. **Corregí la configuración de Gson (RetrofitClient.kt)**

**ANTES:**
```kotlin
private val gson: Gson = GsonBuilder()
    .setLenient()
    .serializeNulls()  // ❌ Esto enviaba todos los campos null
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .create()
```

**AHORA:**
```kotlin
private val gson: Gson = GsonBuilder()
    .setLenient()
    // NO serializar nulls - si un campo es null, no se incluye en el JSON
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .create()
```

**Resultado:**
- ✅ Cuando `usuarioID = null`, el campo NO se incluye en el JSON
- ✅ Solo se envía cuando hay un usuario realmente asignado

---

### 2. **Mejoré el UI del dropdown de usuarios (EditarActividadDialog.kt)**

Agregué:
- **Mensaje útil** cuando no hay usuarios disponibles
- **Logs de debug** para verificar cuántos usuarios se cargan
- **Opción "Sin asignar"** siempre visible

```kotlin
ExposedDropdownMenuBox(
    expanded = usuarioExpanded,
    onExpandedChange = { 
        usuarioExpanded = !usuarioExpanded
        println("🔵 Usuarios disponibles: ${usuariosAsignables.size}")
        usuariosAsignables.forEach { 
            println("   - ${it.nombreCompleto} (${it.rol})")
        }
    }
) {
    // ...
    ExposedDropdownMenu(...) {
        // Opción "Sin asignar" siempre visible
        DropdownMenuItem(
            text = { Text("Sin asignar") },
            onClick = {
                usuarioSeleccionadoId = null
                usuarioSeleccionadoNombre = "Sin asignar"
                usuarioExpanded = false
            }
        )
        
        // Mensaje cuando no hay usuarios
        if (usuariosAsignables.isEmpty()) {
            DropdownMenuItem(
                text = { 
                    Text(
                        "No hay usuarios disponibles",
                        style = TextStyle(fontStyle = Italic),
                        color = Color.Gray
                    )
                },
                onClick = { usuarioExpanded = false },
                enabled = false
            )
        }
        
        // Lista de usuarios
        usuariosAsignables.forEach { usuario ->
            DropdownMenuItem(
                text = { Text(usuario.nombreCompleto ?: "Usuario sin nombre") },
                onClick = {
                    usuarioSeleccionadoId = usuario.id
                    usuarioSeleccionadoNombre = usuario.nombreCompleto
                    usuarioExpanded = false
                }
            )
        }
    }
}
```

---

## 🔍 Cómo verificar si se están cargando los usuarios:

### Paso 1: Ejecuta la app y revisa los logs

Cuando abras el panel de admin, busca en Logcat:

```
🔵 Cargando usuarios asignables...
   Response Code: 200
   Response Successful: true
   Total usuarios obtenidos: X
   [0] Juan Pérez - Rol: 'Usuario'
   [1] María García - Rol: 'Usuario'
   ✅ Usuarios asignables cargados: 2
```

### Paso 2: Abre el dropdown de usuarios

Cuando abras el dropdown, verás en Logcat:

```
🔵 Usuarios disponibles: 2
   - Juan Pérez (Usuario)
   - María García (Usuario)
```

### Paso 3: Si no hay usuarios, verás:

```
🔵 Usuarios disponibles: 0
```

Y en el dropdown aparecerá: **"No hay usuarios disponibles"**

---

## 🚨 Posibles causas si NO se cargan usuarios:

### 1. **El backend no tiene usuarios con rol "Usuario"**

El sistema filtra usuarios con el rol **"Usuario"** (ignorando mayúsculas/minúsculas).

**Verifica en tu base de datos MongoDB:**

```javascript
db.usuarios.find({ rol: /^usuario$/i })
```

**Solución:** Crea usuarios con `rol: "Usuario"` en tu backend.

---

### 2. **El endpoint `/api/Usuario` no existe o falla**

El `AdminViewModel` llama a:

```kotlin
val response = usuarioApiService.getAllUsuarios()
```

Que hace una petición GET a: `http://10.0.2.2:5288/api/Usuario`

**Verifica que tu backend ASP.NET Core tenga este endpoint:**

```csharp
[ApiController]
[Route("api/[controller]")]
public class UsuarioController : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var usuarios = await _context.Usuarios.ToListAsync();
        return Ok(usuarios);
    }
}
```

**Prueba manualmente en el navegador:**
```
http://10.0.2.2:5288/api/Usuario
```

Deberías ver un JSON con la lista de usuarios.

---

### 3. **El modelo `UsuarioCompleto` no coincide con el backend**

El frontend espera este formato:

```kotlin
data class UsuarioCompleto(
    val id: String,
    val nombreCompleto: String?,
    val email: String,
    val rol: String,
    // ... otros campos
)
```

**Verifica que el backend devuelva:**

```json
[
  {
    "id": "6938bd682ec30134be1d6481",
    "nombreCompleto": "Juan Pérez",
    "email": "juan@tcs.com",
    "rol": "Usuario",
    ...
  }
]
```

---

## 📊 Comportamiento esperado:

### Cuando NO hay usuario asignado:

**JSON enviado al backend:**
```json
{
  "titulo": "...",
  "descripcion": "...",
  "dia": 1,
  ...
  // ⚠️ usuarioId NO aparece en el JSON
}
```

### Cuando SÍ hay usuario asignado:

**JSON enviado al backend:**
```json
{
  "titulo": "...",
  "descripcion": "...",
  "dia": 1,
  ...
  "usuarioId": "6938bd682ec30134be1d6481"  ✅
}
```

---

## 🎯 Próximos pasos:

1. **Ejecuta la app** y revisa los logs cuando se abra el panel de admin
2. **Abre el dropdown** "Asignar usuario" y verifica los logs
3. **Si no hay usuarios:**
   - Verifica que el endpoint `/api/Usuario` funcione
   - Verifica que haya usuarios con rol "Usuario" en MongoDB
   - Verifica que el modelo coincida con el backend

4. **Si aparecen usuarios:**
   - Selecciona uno y guarda la actividad
   - Verifica en MongoDB que el campo `usuarioId` tenga el ID correcto

---

## ✅ Resumen de cambios:

| Archivo | Cambio | Motivo |
|---------|--------|--------|
| `RetrofitClient.kt` | Removí `.serializeNulls()` | Para NO enviar `usuarioId: null` |
| `EditarActividadDialog.kt` | Agregué logs + mensaje útil | Para debug y mejor UX |
| `ActivityRequest.kt` | Cambié `@SerializedName("usuarioID")` a `"usuarioId"` | Para coincidir con MongoDB |

---

## 📝 Notas importantes:

- El campo `usuarioId` ahora **solo se envía cuando tiene un valor**
- Si seleccionas "Sin asignar", el campo NO se incluye en el JSON
- Los logs te ayudarán a identificar si el problema está en el backend o frontend

