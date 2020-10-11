package com.phinnovation.core.data

import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz

class QuestionRepository (
    private val dataSource: QuestionDataSource,
    private val openQuestionDataSource: OpenQuestionDataSource) {

    suspend fun addQuestion(quiz: Quiz, question: Question) =
        dataSource.add(quiz, question)

    suspend fun getQuestions(quiz: Quiz) = dataSource.read(quiz)

    suspend fun removeQuestion(quiz: Quiz, question: Question) =
        dataSource.remove(quiz, question)

    suspend fun updateQuestion(quiz: Quiz, question: Question) = dataSource.update(quiz,question)

    fun setOpenQuestion(question: Question) = openQuestionDataSource.setOpenQuestion(question)

    fun getOpenQuestion(): Question = openQuestionDataSource.getOpenQuestion()

}