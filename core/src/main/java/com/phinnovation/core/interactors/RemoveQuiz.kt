package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuizRepository
import com.phinnovation.core.domain.Quiz

class RemoveQuiz(private val quizRepository: QuizRepository) {
    suspend operator fun invoke(quiz: Quiz) =
        quizRepository.removeQuiz(quiz)
}