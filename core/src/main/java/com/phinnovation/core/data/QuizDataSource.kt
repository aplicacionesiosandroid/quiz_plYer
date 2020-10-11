package com.phinnovation.core.data

import com.phinnovation.core.domain.Quiz

interface QuizDataSource {

    suspend fun add(quiz: Quiz)

    suspend fun readAll(): List<Quiz>

    suspend fun remove(quiz: Quiz)

    suspend fun update(quiz: Quiz)
}