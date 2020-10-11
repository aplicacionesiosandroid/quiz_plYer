package com.phinnovation.core.interactors

import com.phinnovation.core.data.QuizRepository

class GetQuizes(private val quizRepository: QuizRepository) {
    suspend operator fun invoke() =
        quizRepository.getQuizes()
}