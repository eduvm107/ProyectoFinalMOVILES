package com.example.chatbot_diseo.data.admin

class AdminRepository {

    private var contentIdCounter = 3
    private var activityIdCounter = 2
    private var resourceIdCounter = 2

    // ---------------- CONTENIDOS ----------------
    val contents = mutableListOf(
        ContentItem(1, "Horario de soporte", "FAQ", "El horario es de 9am a 6pm."),
        ContentItem(2, "Mensaje de bienvenida", "Mensaje", "Bienvenido a TCS"),
        ContentItem(3, "Portal de RRHH", "Enlace", "https://rrhh.tcs.com")
    )

    fun addContent(title: String, type: String, desc: String) {
        contents.add(ContentItem(++contentIdCounter, title, type, desc))
    }

    fun updateContent(id: Int, title: String, type: String, desc: String) {
        val index = contents.indexOfFirst { it.id == id }
        if (index != -1) {
            contents[index] = ContentItem(id, title, type, desc)
        }
    }

    fun deleteContent(id: Int) {
        contents.removeAll { it.id == id }
    }

    // ---------------- ACTIVIDADES ----------------
    val activities = mutableListOf(
        ActivityItem(1, "Inducción cultura TCS", "10 nov", "Presencial"),
        ActivityItem(2, "Ética y seguridad digital", "12 nov", "Virtual")
    )

    fun addActivity(title: String, date: String, modality: String) {
        activities.add(ActivityItem(++activityIdCounter, title, date, modality))
    }

    fun updateActivity(id: Int, title: String, date: String, modality: String) {
        val index = activities.indexOfFirst { it.id == id }
        if (index != -1) {
            activities[index] = ActivityItem(id, title, date, modality)
        }
    }

    fun deleteActivity(id: Int) {
        activities.removeAll { it.id == id }
    }

    // ---------------- RECURSOS ----------------
    val resources = mutableListOf(
        ResourceItem(1, "Manual de bienvenida", "Manual", "https://example.com/manual"),
        ResourceItem(2, "Beneficios y convenios", "Beneficios", "https://example.com/beneficios")
    )

    fun addResource(title: String, category: String, url: String) {
        resources.add(ResourceItem(++resourceIdCounter, title, category, url))
    }

    fun updateResource(id: Int, title: String, category: String, url: String) {
        val index = resources.indexOfFirst { it.id == id }
        if (index != -1) {
            resources[index] = ResourceItem(id, title, category, url)
        }
    }

    fun deleteResource(id: Int) {
        resources.removeAll { it.id == id }
    }

    // ---------------- MÉTRICAS ----------------
    fun totalContents() = contents.size
    fun totalActivities() = activities.size
    fun totalResources() = resources.size

    fun completionRate(): Int = 87
    fun averageSatisfaction(): Double = 4.5
    fun averageTimeDays(): Int = 14
}
