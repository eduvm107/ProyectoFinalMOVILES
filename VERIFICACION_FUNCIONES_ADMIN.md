# 🧪 VERIFICACIÓN COMPLETA DE FUNCIONES DEL ADMINISTRADOR

**Fecha:** 2025-11-25
**Estado:** LISTO PARA PRUEBAS

---

## 📋 ÍNDICE

1. [Pre-requisitos](#pre-requisitos)
2. [Pruebas de API (Backend)](#pruebas-de-api-backend)
3. [Pruebas de App Android](#pruebas-de-app-android)
4. [Checklist de Verificación](#checklist-de-verificación)
5. [Problemas Comunes](#problemas-comunes)

---

## 🔧 PRE-REQUISITOS

### 1. MongoDB debe estar corriendo
```bash
# Verificar en Windows Services
services.msc
# Buscar "MongoDB" y verificar que esté "Running"

# O usando mongosh
mongosh
use ChatbotTCS
db.usuarios.countDocuments()
```

### 2. API ASP.NET Core debe estar corriendo
```bash
cd C:\C#\ChatbotTCS.AdminAPI\ChatbotTCS.AdminAPI
dotnet run
```

**Deberías ver:**
```
Building...
info: Microsoft.Hosting.Lifetime[14]
      Now listening on: http://localhost:5288
info: Microsoft.Hosting.Lifetime[0]
      Application started.
```

### 3. Verificar Swagger
Abrir en navegador: http://localhost:5288/swagger

Deberías ver la documentación interactiva de la API.

---

## 🧪 PRUEBAS DE API (BACKEND)

### Opción 1: Script Automático PowerShell

He creado un script que prueba TODOS los endpoints automáticamente:

```powershell
cd C:\C#\ChatbotTCS.AdminAPI
.\TEST_API_ENDPOINTS.ps1
```

**Qué hace el script:**
- ✅ Verifica que la API esté corriendo
- ✅ Prueba CRUD completo de Mensajes Automáticos
- ✅ Prueba CRUD completo de Actividades
- ✅ Prueba CRUD completo de Documentos/Recursos
- ✅ Prueba endpoint de Métricas
- ✅ Muestra resultados con colores (verde = OK, rojo = ERROR)

---

### Opción 2: Pruebas Manuales con Swagger

#### A. Mensajes Automáticos

1. **Listar todos los mensajes**
   - Abrir: http://localhost:5288/swagger
   - Expandir: `GET /api/mensajeautomatico`
   - Click en "Try it out"
   - Click en "Execute"
   - **Resultado esperado:** Lista de mensajes (puede estar vacía)

2. **Crear nuevo mensaje**
   - Expandir: `POST /api/mensajeautomatico`
   - Click en "Try it out"
   - Pegar este JSON:
   ```json
   {
     "titulo": "Mensaje de Bienvenida",
     "contenido": "Bienvenido a TCS",
     "tipo": "bienvenida",
     "prioridad": "alta",
     "canal": ["chatbot"],
     "activo": true,
     "segmento": "todos",
     "horaEnvio": "09:00",
     "creadoPor": "Admin"
   }
   ```
   - Click en "Execute"
   - **Resultado esperado:** Código 201, objeto con ID generado

3. **Obtener mensaje por ID**
   - Copiar el ID del paso anterior
   - Expandir: `GET /api/mensajeautomatico/{id}`
   - Pegar el ID
   - Click en "Execute"
   - **Resultado esperado:** Código 200, el mensaje creado

4. **Actualizar mensaje**
   - Expandir: `PUT /api/mensajeautomatico/{id}`
   - Usar el mismo ID
   - Modificar el JSON (cambiar título, contenido, etc.)
   - Click en "Execute"
   - **Resultado esperado:** Código 204 No Content

5. **Eliminar mensaje**
   - Expandir: `DELETE /api/mensajeautomatico/{id}`
   - Usar el mismo ID
   - Click en "Execute"
   - **Resultado esperado:** Código 204 No Content

---

#### B. Actividades

1. **Listar todas**
   - `GET /api/actividad`
   - **Esperado:** Lista de actividades

2. **Crear nueva actividad**
   - `POST /api/actividad`
   - JSON:
   ```json
   {
     "titulo": "Inducción General",
     "descripcion": "Presentación de la empresa",
     "dia": 1,
     "duracionHoras": 3,
     "horaInicio": "09:00",
     "horaFin": "12:00",
     "lugar": "Auditorio Principal",
     "modalidad": "presencial",
     "tipo": "induccion",
     "categoria": "Onboarding",
     "responsable": "RRHH",
     "capacidadMaxima": 50,
     "obligatorio": true,
     "estado": "activo"
   }
   ```
   - **Esperado:** Código 201

3. **Obtener por ID**
   - `GET /api/actividad/{id}`
   - **Esperado:** Código 200

4. **Actualizar**
   - `PUT /api/actividad/{id}`
   - Modificar el JSON
   - **Esperado:** Código 204

5. **Eliminar**
   - `DELETE /api/actividad/{id}`
   - **Esperado:** Código 204

---

#### C. Documentos/Recursos

1. **Listar todos**
   - `GET /api/documento`
   - **Esperado:** Lista de documentos

2. **Crear nuevo documento**
   - `POST /api/documento`
   - JSON:
   ```json
   {
     "titulo": "Manual del Empleado",
     "descripcion": "Manual completo para nuevos empleados",
     "url": "https://ejemplo.com/manual.pdf",
     "tipo": "PDF",
     "categoria": "Manuales",
     "subcategoria": "Onboarding",
     "tags": ["manual", "empleado", "onboarding"],
     "icono": "📄",
     "idioma": "Español",
     "version": "1.0",
     "publico": "Nuevos empleados",
     "obligatorio": true,
     "autor": "RRHH",
     "valoracion": 0
   }
   ```
   - **Esperado:** Código 201

3. **Obtener por ID**
   - `GET /api/documento/{id}`
   - **Esperado:** Código 200

4. **Actualizar**
   - `PUT /api/documento/{id}`
   - **Esperado:** Código 204

5. **Eliminar**
   - `DELETE /api/documento/{id}`
   - **Esperado:** Código 204

---

#### D. Métricas

1. **Obtener métricas generales**
   - `GET /api/metrics`
   - **Esperado:** Código 200
   - **Campos en respuesta:**
     ```json
     {
       "totalContents": 0,
       "totalActivities": 0,
       "totalResources": 0,
       "completionRate": 87,
       "averageSatisfaction": 4.5,
       "averageTimeDays": 14,
       "activeUsers": 0,
       "totalInteractions": 0
     }
     ```

---

## 📱 PRUEBAS DE APP ANDROID

### Pre-requisitos

1. **API corriendo** en http://localhost:5288
2. **MongoDB corriendo**
3. **Usuario admin creado** en MongoDB (ver `CREAR_USUARIO_ADMIN.md`)
4. **App Android** ejecutándose en emulador o dispositivo

### Pruebas de Integración

#### 1. Login como Administrador

**Pasos:**
1. Abrir la app Android
2. Ir a la pantalla de login
3. Ingresar:
   - Email: `alias.rodriguez@tcs.com`
   - Contraseña: `yarasa`
4. Click en "Iniciar Sesión"

**Resultado esperado:**
- ✅ Login exitoso
- ✅ Redirección a Home o Panel Admin

---

#### 2. Panel Administrativo - Dashboard

**Pasos:**
1. Desde el Home, navegar a "Panel Administrativo"
2. Observar el Dashboard Header (4 tarjetas superiores)

**Verificar:**
- ✅ Tarjeta "Total Contenidos" muestra número correcto
- ✅ Tarjeta "Total Actividades" muestra número correcto
- ✅ Tarjeta "Total Recursos" muestra número correcto
- ✅ Tarjeta "Completitud %" muestra porcentaje

**Logs a verificar (Logcat):**
```
D/OkHttp: --> GET http://10.185.24.6:5288/api/mensajeautomatico
D/OkHttp: <-- 200 http://10.185.24.6:5288/api/mensajeautomatico
D/OkHttp: --> GET http://10.185.24.6:5288/api/actividad
D/OkHttp: <-- 200 http://10.185.24.6:5288/api/actividad
D/OkHttp: --> GET http://10.185.24.6:5288/api/documento
D/OkHttp: <-- 200 http://10.185.24.6:5288/api/documento
D/OkHttp: --> GET http://10.185.24.6:5288/api/metrics
D/OkHttp: <-- 200 http://10.185.24.6:5288/api/metrics
```

---

#### 3. Tab 1: Mensajes Automáticos

##### 3.1 Ver Lista
**Pasos:**
1. Estar en Tab "Mensajes"
2. Observar la lista

**Verificar:**
- ✅ Se muestran tarjetas con mensajes
- ✅ Cada tarjeta muestra: Título, Tipo, Descripción
- ✅ Si la lista está vacía, se muestra mensaje apropiado

##### 3.2 Crear Mensaje
**Pasos:**
1. Click en botón FAB (+) flotante
2. Llenar formulario:
   - Título: "Bienvenida Android"
   - Tipo: "bienvenida"
   - Descripción: "Mensaje de bienvenida desde Android"
3. Click en "Crear"

**Verificar:**
- ✅ Muestra indicador de carga
- ✅ Muestra mensaje de éxito (banner verde)
- ✅ El nuevo mensaje aparece en la lista
- ✅ El dashboard actualiza el contador

**Logs esperados:**
```
D/OkHttp: --> POST http://10.185.24.6:5288/api/mensajeautomatico
D/OkHttp: <-- 201 http://10.185.24.6:5288/api/mensajeautomatico
```

##### 3.3 Editar Mensaje
**Pasos:**
1. Click en icono de lápiz (editar) en una tarjeta
2. Modificar título o descripción
3. Click en "Actualizar"

**Verificar:**
- ✅ Muestra indicador de carga
- ✅ Muestra mensaje de éxito
- ✅ Los cambios se reflejan en la tarjeta

**Logs esperados:**
```
D/OkHttp: --> PUT http://10.185.24.6:5288/api/mensajeautomatico/{id}
D/OkHttp: <-- 204 http://10.185.24.6:5288/api/mensajeautomatico/{id}
```

##### 3.4 Eliminar Mensaje
**Pasos:**
1. Click en icono de basura (eliminar) en una tarjeta
2. Confirmar eliminación (si hay diálogo)

**Verificar:**
- ✅ Muestra indicador de carga
- ✅ Muestra mensaje de éxito
- ✅ La tarjeta desaparece de la lista
- ✅ El dashboard actualiza el contador

**Logs esperados:**
```
D/OkHttp: --> DELETE http://10.185.24.6:5288/api/mensajeautomatico/{id}
D/OkHttp: <-- 204 http://10.185.24.6:5288/api/mensajeautomatico/{id}
```

---

#### 4. Tab 2: Actividades

##### 4.1 Ver Lista
- ✅ Se muestran tarjetas con actividades
- ✅ Cada tarjeta muestra: Título, Fecha, Modalidad

##### 4.2 Crear Actividad
**Formulario:**
- Título: "Inducción Android"
- Fecha/Día: "Día 1 - 09:00"
- Modalidad: "presencial"

**Verificar:**
- ✅ POST a /api/actividad exitoso
- ✅ Aparece en la lista
- ✅ Dashboard se actualiza

##### 4.3 Editar Actividad
- ✅ PUT a /api/actividad/{id} exitoso
- ✅ Cambios se reflejan

##### 4.4 Eliminar Actividad
- ✅ DELETE a /api/actividad/{id} exitoso
- ✅ Desaparece de la lista

---

#### 5. Tab 3: Recursos

##### 5.1 Ver Lista
- ✅ Se muestran tarjetas con recursos
- ✅ Cada tarjeta muestra: Título, Categoría, URL

##### 5.2 Crear Recurso
**Formulario:**
- Título: "Manual Android"
- Categoría: "Manuales"
- URL: "https://ejemplo.com/manual.pdf"

**Verificar:**
- ✅ POST a /api/documento exitoso
- ✅ Aparece en la lista

##### 5.3 Editar Recurso
- ✅ PUT a /api/documento/{id} exitoso
- ✅ Cambios se reflejan

##### 5.4 Eliminar Recurso
- ✅ DELETE a /api/documento/{id} exitoso
- ✅ Desaparece de la lista

---

#### 6. Tab 4: Métricas

**Pasos:**
1. Navegar al Tab "Métricas"
2. Observar las 3 tarjetas de métricas

**Verificar:**
- ✅ Tarjeta "Tasa de Completitud" muestra porcentaje y barra de progreso
- ✅ Tarjeta "Satisfacción Promedio" muestra valor sobre 5 y barra
- ✅ Tarjeta "Tiempo Promedio" muestra días y barra
- ✅ Los valores vienen de GET /api/metrics

**Logs esperados:**
```
D/OkHttp: --> GET http://10.185.24.6:5288/api/metrics
D/OkHttp: <-- 200 http://10.185.24.6:5288/api/metrics
```

---

## ✅ CHECKLIST DE VERIFICACIÓN

### Backend (API)

#### Mensajes Automáticos
- [ ] GET /api/mensajeautomatico - Listar todos
- [ ] GET /api/mensajeautomatico/{id} - Obtener por ID
- [ ] POST /api/mensajeautomatico - Crear nuevo
- [ ] PUT /api/mensajeautomatico/{id} - Actualizar
- [ ] DELETE /api/mensajeautomatico/{id} - Eliminar

#### Actividades
- [ ] GET /api/actividad - Listar todas
- [ ] GET /api/actividad/{id} - Obtener por ID
- [ ] POST /api/actividad - Crear nueva
- [ ] PUT /api/actividad/{id} - Actualizar
- [ ] DELETE /api/actividad/{id} - Eliminar

#### Documentos
- [ ] GET /api/documento - Listar todos
- [ ] GET /api/documento/{id} - Obtener por ID
- [ ] POST /api/documento - Crear nuevo
- [ ] PUT /api/documento/{id} - Actualizar
- [ ] DELETE /api/documento/{id} - Eliminar

#### Métricas
- [ ] GET /api/metrics - Obtener métricas generales

---

### Frontend (Android App)

#### Login
- [ ] Login con credenciales correctas funciona
- [ ] Login con credenciales incorrectas muestra error
- [ ] Redirección a panel admin después del login

#### Dashboard Header
- [ ] Total Contenidos se carga desde API
- [ ] Total Actividades se carga desde API
- [ ] Total Recursos se carga desde API
- [ ] Completitud % se carga desde API

#### Mensajes Automáticos (Tab 1)
- [ ] Lista se carga al entrar al tab
- [ ] Botón FAB (+) abre diálogo de creación
- [ ] Crear mensaje funciona y actualiza lista
- [ ] Editar mensaje funciona y actualiza tarjeta
- [ ] Eliminar mensaje funciona y actualiza lista
- [ ] Mensajes de éxito/error se muestran correctamente

#### Actividades (Tab 2)
- [ ] Lista se carga al entrar al tab
- [ ] Crear actividad funciona
- [ ] Editar actividad funciona
- [ ] Eliminar actividad funciona
- [ ] Validación de campos funciona

#### Recursos (Tab 3)
- [ ] Lista se carga al entrar al tab
- [ ] Crear recurso funciona
- [ ] Editar recurso funciona
- [ ] Eliminar recurso funciona
- [ ] URL del recurso se valida

#### Métricas (Tab 4)
- [ ] Métricas se cargan desde API
- [ ] Tasa de Completitud muestra valor correcto
- [ ] Satisfacción Promedio muestra valor correcto
- [ ] Tiempo Promedio muestra valor correcto
- [ ] Barras de progreso se visualizan correctamente

---

## 🐛 PROBLEMAS COMUNES

### Problema 1: "Unable to resolve host"

**Síntoma:** La app no puede conectarse a la API

**Solución:**
1. Verificar que la API esté corriendo:
   ```bash
   curl http://localhost:5288/api/mensajeautomatico
   ```

2. Verificar la IP en `RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://10.185.24.6:5288/api/"
   ```

3. Para emulador, usar `10.0.2.2`:
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:5288/api/"
   ```

4. Para dispositivo físico, usar IP de tu PC:
   ```bash
   ipconfig  # En Windows
   # Usar la IPv4 de tu red local
   ```

---

### Problema 2: Error 500 en Login

**Síntoma:** Al hacer login aparece error 500

**Causa:** El usuario no existe en MongoDB

**Solución:**
Ver archivo `CREAR_USUARIO_ADMIN.md` para crear el usuario administrador.

---

### Problema 3: Error 404 en /api/metrics

**Síntoma:** Endpoint no encontrado

**Causa:** MetricsController no está compilado en la API

**Solución:**
1. Verificar que existe el archivo `MetricsController.cs`
2. Rebuild la API:
   ```bash
   cd C:\C#\ChatbotTCS.AdminAPI\ChatbotTCS.AdminAPI
   dotnet clean
   dotnet build
   dotnet run
   ```

---

### Problema 4: Las listas aparecen vacías

**Síntoma:** Los tabs muestran listas vacías pero no hay error

**Causa:** MongoDB no tiene datos

**Solución:**
1. Usar Swagger para crear datos de prueba
2. O usar el script de pruebas PowerShell
3. O insertar datos directamente en MongoDB:
   ```javascript
   mongosh
   use ChatbotTCS
   db.mensajesautomaticos.insertOne({
     titulo: "Bienvenida",
     contenido: "Mensaje de bienvenida",
     tipo: "bienvenida",
     activo: true
   })
   ```

---

### Problema 5: "No se pudo conectar al servidor"

**Síntoma:** Error de conexión desde Android

**Causa:** Firewall bloqueando el puerto 5288

**Solución:**
```powershell
# En PowerShell como Administrador
New-NetFirewallRule -DisplayName "ASP.NET API 5288" -Direction Inbound -Protocol TCP -LocalPort 5288 -Action Allow
```

---

## 📊 LOGS ESPERADOS

### Android (Logcat)

**Filtrar por:** `OkHttp`

**Logs exitosos:**
```
D/OkHttp: --> GET http://10.185.24.6:5288/api/mensajeautomatico
D/OkHttp: <-- 200 http://10.185.24.6:5288/api/mensajeautomatico (123ms, 1245 bytes)

D/OkHttp: --> POST http://10.185.24.6:5288/api/mensajeautomatico
D/OkHttp: <-- 201 http://10.185.24.6:5288/api/mensajeautomatico (234ms)

D/OkHttp: --> PUT http://10.185.24.6:5288/api/mensajeautomatico/abc123
D/OkHttp: <-- 204 http://10.185.24.6:5288/api/mensajeautomatico/abc123 (156ms)

D/OkHttp: --> DELETE http://10.185.24.6:5288/api/mensajeautomatico/abc123
D/OkHttp: <-- 204 http://10.185.24.6:5288/api/mensajeautomatico/abc123 (89ms)
```

---

### API (Consola .NET)

**Logs exitosos:**
```
info: ChatbotTCS.AdminAPI.Controllers.MensajeAutomaticoController[0]
      Intento de obtener todos los mensajes automáticos

info: ChatbotTCS.AdminAPI.Services.MensajeAutomaticoService[0]
      Obteniendo todos los mensajes automáticos

info: ChatbotTCS.AdminAPI.Controllers.MensajeAutomaticoController[0]
      Creando nuevo mensaje automático

info: ChatbotTCS.AdminAPI.Services.MensajeAutomaticoService[0]
      Mensaje automático creado con ID: 674548d9f8a3c2e4b1234567
```

---

## ✅ CONCLUSIÓN

Una vez completadas todas las pruebas en este checklist, habrás verificado que:

1. ✅ La API backend funciona correctamente
2. ✅ MongoDB almacena y recupera datos correctamente
3. ✅ La app Android se conecta a la API
4. ✅ Todas las operaciones CRUD funcionan
5. ✅ Las métricas se calculan y muestran correctamente
6. ✅ El manejo de errores funciona apropiadamente

**El sistema estará 100% funcional y listo para producción.**

---

**Última actualización:** 2025-11-25
**Autor:** Claude Code
