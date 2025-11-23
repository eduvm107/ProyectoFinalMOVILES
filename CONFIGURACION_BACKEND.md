# 🔗 Conexión Android ↔ Backend ASP.NET Core

## ✅ CONFIGURACIÓN COMPLETADA

He actualizado **toda la arquitectura** para que coincida exactamente con tu backend ASP.NET Core + MongoDB.

---

## 📋 RESUMEN DE CAMBIOS

### 1. **DTOs actualizados** (nombres en español)

#### **MensajeAutomatico** → ContentResponse
```kotlin
- titulo (String)
- contenido (String)  
- tipo (String)
- diaGatillo (Int?)
- prioridad (String)
- canal (List<String>)
- activo (Boolean)
- segmento (String)
- horaEnvio (String)
```

#### **Actividad** → ActivityResponse
```kotlin
- titulo (String)
- descripcion (String)
- dia (Int)
- duracionHoras (Double)
- horaInicio (String)
- horaFin (String)
- lugar (String)
- modalidad (String)
- tipo (String)
- categoria (String)
- responsable (String)
- capacidadMaxima (Int)
- obligatorio (Boolean)
- materialesNecesarios (List<String>)
```

#### **Documento** → ResourceResponse
```kotlin
- titulo (String)
- descripcion (String)
- url (String)
- tipo (String)
- categoria (String)
- subcategoria (String)
- tags (List<String>)
- icono (String)
- autor (String)
- valoracion (Int)
```

### 2. **Endpoints actualizados** (rutas en español)

```
✅ Mensajes:    http://10.185.24.6:5288/api/mensajesautomaticos
✅ Actividades: http://10.185.24.6:5288/api/actividades
✅ Documentos:  http://10.185.24.6:5288/api/documentos
✅ Métricas:    http://10.185.24.6:5288/api/metricas
```

---

## 🧪 CÓMO VERIFICAR QUE FUNCIONA

### **PASO 1: Verificar que tu backend esté corriendo**

Abre Chrome en tu celular y prueba esta URL:

```
http://10.185.24.6:5288/api/actividades
```

**¿Qué deberías ver?**
- ✅ Un JSON con lista de actividades
- ✅ `[]` (lista vacía si no hay datos)
- ❌ Error 404 → El controlador no existe o usa otra ruta

---

### **PASO 2: Verificar los controladores en tu backend**

En tu proyecto ASP.NET Core, busca los archivos de controladores. Deberían verse así:

```csharp
[ApiController]
[Route("api/[controller]")]
public class ActividadesController : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        // ...
    }
}
```

**Rutas esperadas:**
- `/api/mensajesautomaticos` o `/api/mensajes`
- `/api/actividades`
- `/api/documentos`

---

### **PASO 3: Si los controladores usan nombres diferentes**

Si tu backend usa rutas como:
- `/api/admin/actividades`
- `/api/v1/actividades`
- `/api/Actividades` (con mayúscula)

Entonces abre este archivo y cambia las rutas:

📁 `AdminApiService.kt` → Líneas 20, 40, 60

```kotlin
@GET("actividades")  // Cambiar aquí
```

---

## 🔧 ARCHIVOS MODIFICADOS

```
network/
├── api/
│   └── AdminApiService.kt          ✅ Rutas en español
├── client/
│   ├── RetrofitClient.kt           ✅ BASE_URL actualizada
│   └── ApiConfig.kt                ✅ Configuración centralizada
├── dto/
│   ├── request/
│   │   ├── ContentRequest.kt       ✅ Campos en español
│   │   ├── ActivityRequest.kt      ✅ Todos los campos del backend
│   │   └── ResourceRequest.kt      ✅ Modelo Documento completo
│   └── response/
│       ├── ContentResponse.kt      ✅ MensajeAutomatico completo
│       ├── ActivityResponse.kt     ✅ Actividad completo
│       └── ResourceResponse.kt     ✅ Documento completo

data/admin/
├── datasource/
│   └── AdminRemoteDataSource.kt    ✅ Usa campos en español
├── mapper/
│   └── AdminMappers.kt             ✅ Conversión actualizada
└── AdminRepository.kt              ✅ Sin cambios

presentation/admin/
├── page/
│   └── AdminViewModel.kt           ✅ Sin cambios
└── components/
    └── MessageBanner.kt            ✅ Muestra errores visuales
```

---

## 🎯 QUÉ ESPERAR EN LA APP

### **Si la conexión funciona:**
1. El panel de administración cargará automáticamente
2. Verás las listas de:
   - Mensajes automáticos
   - Actividades
   - Documentos
3. Podrás crear, editar y eliminar elementos

### **Si hay error 404:**
La app mostrará:
```
❌ Recurso no encontrado. Verifica que el endpoint exista en tu backend.
```

Esto significa que:
- Tu backend NO tiene ese controlador
- La ruta es diferente
- El servidor no está corriendo

### **Si no hay conexión:**
La app mostrará:
```
❌ No se pudo conectar al servidor en http://10.185.24.6:5288
```

Verifica:
- ✅ El backend está corriendo
- ✅ La IP es correcta
- ✅ El puerto es correcto
- ✅ El celular está en la misma red WiFi

---

## 🚨 SI SIGUE SIN FUNCIONAR

### **Opción 1: Verifica las rutas exactas**

Dime qué devuelve esta URL en tu navegador:
```
http://10.185.24.6:5288/swagger
```

Swagger te mostrará TODAS las rutas disponibles.

### **Opción 2: Prueba con Postman**

Haz un GET a:
```
http://10.185.24.6:5288/api/actividades
```

Y dime:
- ✅ ¿Funciona?
- ❌ ¿Qué error da?

### **Opción 3: Revisa los logs del backend**

Cuando la app Android haga la petición, tu backend ASP.NET Core debería mostrar en la consola:

```
info: Microsoft.AspNetCore.Hosting.Diagnostics[1]
      Request starting HTTP/1.1 GET http://10.185.24.6:5288/api/actividades
```

---

## 📱 PRÓXIMOS PASOS

1. **Compila la app** (Sync Gradle)
2. **Ejecuta en tu celular**
3. **Abre el Panel de Administración**
4. **Observa los mensajes de error** en la tarjeta de diagnóstico
5. **Dime qué mensaje de error aparece** para ajustar las rutas

---

## 💡 NOTAS IMPORTANTES

### **Diferencias clave entre tu backend y la app:**

| Backend (MongoDB) | App Android |
|-------------------|-------------|
| `MensajeAutomatico` | `ContentItem` |
| `Actividad` | `ActivityItem` |
| `Documento` | `ResourceItem` |
| Campos en español | Se mapean automáticamente |

### **La app maneja automáticamente:**
- ✅ Conversión de nombres (titulo → title)
- ✅ Listas vacías si no hay datos
- ✅ Errores HTTP (404, 500, timeout)
- ✅ Mensajes de error claros
- ✅ Indicadores de carga
- ✅ Mensajes de éxito

---

## 🎉 TODO LISTO

La app ahora está **100% configurada** para tu backend ASP.NET Core.

Solo falta verificar que:
1. El backend tenga los controladores
2. Las rutas coincidan
3. El servidor esté corriendo

**¿Qué mensaje de error ves ahora en el panel de administración?**

