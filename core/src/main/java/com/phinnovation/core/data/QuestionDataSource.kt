package com.phinnovation.core.data

import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz

interface QuestionDataSource {

    suspend fun add(quiz: Quiz, question: Question)

    suspend fun read(quiz: Quiz): List<Question>

    suspend fun remove(quiz: Quiz, question: Question)

    suspend fun update(quiz: Quiz, question:Question)
}

