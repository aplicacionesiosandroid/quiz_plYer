package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuizRepository
import com.phinnovation.core.domain.Quiz

class SetOpenQuiz(private val quizRepository: QuizRepository) {
    operator fun invoke(quiz: Quiz) =
        quizRepository.setOpenQuiz(quiz)
}