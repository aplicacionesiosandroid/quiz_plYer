package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuestionRepository

class GetOpenQuestion(private val questionRepository: QuestionRepository) {
    operator fun invoke() =
        questionRepository.getOpenQuestion()
}