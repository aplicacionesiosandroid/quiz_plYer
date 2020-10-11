package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuestionRepository
import com.phinnovation.core.domain.Quiz

class GetQuestions(private val questionRepository: QuestionRepository) {
    suspend operator fun invoke(quiz: Quiz) =
        questionRepository.getQuestions(quiz)
}

