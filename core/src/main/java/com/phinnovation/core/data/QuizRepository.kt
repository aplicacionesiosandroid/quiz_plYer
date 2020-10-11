package com.phinnovation.core.data

import com.phinnovation.core.domain.Quiz

class QuizRepository (
    private val quizDataSource: QuizDataSource,
    private val openQuizDataSource: OpenQuizDataSource) {

        suspend fun addQuiz(quiz: Quiz) = quizDataSource.add(quiz)

        suspend fun getQuizes() = quizDataSource.readAll()

        suspend fun removeQuiz(quiz: Quiz) = quizDataSource.remove(quiz)

        suspend fun updateQuiz(quiz: Quiz) = quizDataSource.update(quiz)

        fun setOpenQuiz(quiz: Quiz) = openQuizDataSource.setOpenQuiz(quiz)

        fun getOpenQuiz() = openQuizDataSource.getOpenQuiz()
    }