package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuestionRepository
import com.phinnovation.core.domain.Question

class SetOpenQuestion(private val questionRepository: QuestionRepository) {
    operator fun invoke(question: Question) =
        questionRepository.setOpenQuestion(question)
}