package com.example.chatbot_diseo.presentation.admin.page

import androidx.lifecycle.ViewModel
import com.example.chatbot_diseo.data.admin.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminPanelViewModel : ViewModel() {

    private val repository = AdminRepository()

    // ------------ CONTENIDOS ------------
    private val _contents = MutableStateFlow<List<ContentItem>>(repository.contents.toList())
    val contents: StateFlow<List<ContentItem>> = _contents

    fun addContent(title: String, type: String, description: String) {
        repository.addContent(title, type, description)
        _contents.value = repository.contents.toList()
    }

    fun updateContent(id: Int, title: String, type: String, description: String) {
        repository.updateContent(id, title, type, description)
        _contents.value = repository.contents.toList()
    }

    fun deleteContent(id: Int) {
        repository.deleteContent(id)
        _contents.value = repository.contents.toList()
    }

    // ------------ ACTIVIDADES ------------
    private val _activities = MutableStateFlow<List<ActivityItem>>(repository.activities.toList())
    val activities: StateFlow<List<ActivityItem>> = _activities

    fun addActivity(title: String, date: String, modality: String) {
        repository.addActivity(title, date, modality)
        _activities.value = repository.activities.toList()
    }

    fun updateActivity(id: Int, title: String, date: String, modality: String) {
        repository.updateActivity(id, title, date, modality)
        _activities.value = repository.activities.toList()
    }

    fun deleteActivity(id: Int) {
        repository.deleteActivity(id)
        _activities.value = repository.activities.toList()
    }

    // ------------ RECURSOS ------------
    private val _resources = MutableStateFlow<List<ResourceItem>>(repository.resources.toList())
    val resources: StateFlow<List<ResourceItem>> = _resources

    fun addResource(title: String, category: String, url: String) {
        repository.addResource(title, category, url)
        _resources.value = repository.resources.toList()
    }

    fun updateResource(id: Int, title: String, category: String, url: String) {
        repository.updateResource(id, title, category, url)
        _resources.value = repository.resources.toList()
    }

    fun deleteResource(id: Int) {
        repository.deleteResource(id)
        _resources.value = repository.resources.toList()
    }

    // ------------ MÉTRICAS (para dashboard y pestaña métricas) ------------
    fun getTotalContents() = repository.totalContents()
    fun getTotalActivities() = repository.totalActivities()
    fun getTotalResources() = repository.totalResources()
    fun getCompletionRate() = repository.completionRate()
    fun getAverageSatisfaction() = repository.averageSatisfaction()
    fun getAverageTimeDays() = repository.averageTimeDays()
}
