package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuizRepository

class GetOpenQuiz(private val quizRepository: QuizRepository) {
    operator fun invoke() =
        quizRepository.getOpenQuiz()
}