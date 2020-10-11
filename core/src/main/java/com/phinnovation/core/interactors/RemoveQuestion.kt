package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuestionRepository
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz

class RemoveQuestion(private val questionRepository: QuestionRepository) {
    suspend operator fun invoke(quiz: Quiz, question:Question) =
        questionRepository.removeQuestion(quiz,question)
}