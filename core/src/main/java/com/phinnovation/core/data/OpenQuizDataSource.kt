package com.phinnovation.core.data

import com.phinnovation.core.domain.Quiz

interface OpenQuizDataSource {

    fun setOpenQuiz(quiz: Quiz)

    fun getOpenQuiz(): Quiz
}