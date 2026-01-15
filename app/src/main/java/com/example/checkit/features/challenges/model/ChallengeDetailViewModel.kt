package com.example.checkit.features.challenges.model

data class ChallengeDetailState(
    val name: String = "",
    val author: String = "",
    val creationDate: String = "",
    val description: String = "",
    val completedByCount: Int = 0,
    val tasksInProgress: List<TaskDetail> = emptyList(),
    val tasksCompleted: List<TaskDetail> = emptyList(),
    val isSaved: Boolean = false
)

data class TaskDetail(
    val id: Long,
    val name: String,
    val description: String,
    val isLocked: Boolean = false,
    val isCompleted: Boolean = false
)