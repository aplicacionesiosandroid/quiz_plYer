package com.phinnovation.core.data

import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class QuestionRepository (
    private val dataSource: QuestionDataSource,
    private val openQuestionDataSource: OpenQuestionDataSource,
    private val ioContext: CoroutineContext) {

    suspend fun addQuestion(quiz: Quiz, question: Question) {
        return withContext(ioContext) {
            dataSource.add(quiz, question)
        }
    }

    suspend fun getQuestions(quiz: Quiz):List<Question> {
        return withContext(ioContext) {
            dataSource.read(quiz)
        }
    }

    suspend fun removeQuestion(quiz: Quiz, question: Question) {
        return withContext(ioContext) {
            dataSource.remove(quiz, question)
        }
    }

    suspend fun updateQuestion(quiz: Quiz, question: Question) {
        return withContext(ioContext) {
            dataSource.update(quiz,question)
        }
    }

    fun setOpenQuestion(question: Question) = openQuestionDataSource.setOpenQuestion(question)

    fun getOpenQuestion(): Question = openQuestionDataSource.getOpenQuestion()

}