package com.example.chatbot_diseo.config

/**
 * Configuración centralizada del entorno del backend.
 *
 * Cambia aquí la URL base de la API y todo el proyecto
 * usará el mismo valor.
 *
 * Para backend local en la misma máquina:
 * - Navegador / Postman: http://localhost:5000
 * - Emulador Android:   http://10.0.2.2:5000
 */
object ApiEnvironment {

    /**
     * URL base que debe usar SIEMPRE la app para llamar al backend.
     *
     * Ajusta el puerto y el path `/api/` según tu backend.
     *
     *  http://localhost:5000
     */
    const val BASE_URL: String = "http://api.jehu.online/api/"
        //"http://10.0.2.2:5288/api/"


}

