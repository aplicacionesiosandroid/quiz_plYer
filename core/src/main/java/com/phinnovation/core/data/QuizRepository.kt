package com.phinnovation.core.data

import com.phinnovation.core.domain.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class QuizRepository (
    private val quizDataSource: QuizDataSource,
    private val openQuizDataSource: OpenQuizDataSource,
    private val ioContext:CoroutineContext) {

        suspend fun addQuiz(quiz: Quiz) {
            return withContext(ioContext) {
                quizDataSource.add(quiz)
            }
        }

        suspend fun getQuizes(): List<Quiz> {
            return withContext(ioContext) {
                quizDataSource.readAll()
            }
        }

        suspend fun removeQuiz(quiz: Quiz) {
            return withContext(ioContext) {
                quizDataSource.remove(quiz)
            }
        }

        suspend fun updateQuiz(quiz: Quiz) {
            return withContext(ioContext) {
                quizDataSource.update(quiz)
            }
        }

        fun setOpenQuiz(quiz: Quiz) = openQuizDataSource.setOpenQuiz(quiz)

        fun getOpenQuiz() = openQuizDataSource.getOpenQuiz()
    }