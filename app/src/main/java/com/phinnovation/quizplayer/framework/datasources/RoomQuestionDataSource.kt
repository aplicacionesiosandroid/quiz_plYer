package com.phinnovation.quizplayer.framework

import android.content.Context
import com.phinnovation.core.data.QuestionDataSource
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.framework.database.QuestionDao
import com.phinnovation.quizplayer.framework.database.QuestionEntity
import com.phinnovation.quizplayer.framework.database.QuizPlayerDatabase

class RoomQuestionDataSource(val context: Context, qDao: QuestionDao? = null) : QuestionDataSource {

    private val questionDao: QuestionDao =
        qDao ?: QuizPlayerDatabase.getInstance(context).questionDao()

    override suspend fun add(quiz: Quiz, question: Question) {
        questionDao.addQuestion(
            QuestionEntity(
                quizId = quiz.id,
                title = question.title,
                description = question.description,
                type = question.type,
                choiceTitle1 = question.choiceTitle1,
                choiceTitle2 = question.choiceTitle2,
                choiceTitle3 = question.choiceTitle3,
                choiceTitle4 = question.choiceTitle4,
                correctAnswer = question.correctAnswer
            )
        )
    }

    override suspend fun read(quiz: Quiz): List<Question> {
        return questionDao.getQuestions(quiz.id).map {
            Question(
                id = it.id,
                title = it.title,
                description = it.description,
                type = it.type,
                choiceTitle1 = it.choiceTitle1,
                choiceTitle2 = it.choiceTitle2,
                choiceTitle3 = it.choiceTitle3,
                choiceTitle4 = it.choiceTitle4,
                correctAnswer = it.correctAnswer,
            )
        }
    }

    override suspend fun remove(quiz: Quiz, question: Question) {
        questionDao.removeQuestion(
            QuestionEntity(
                id = question.id,
                title = question.title,
                quizId = quiz.id,
                description = question.description,
                type = question.type,
                choiceTitle1 = question.choiceTitle1,
                choiceTitle2 = question.choiceTitle2,
                choiceTitle3 = question.choiceTitle3,
                choiceTitle4 = question.choiceTitle4,
                correctAnswer = question.correctAnswer,
            )
        )
    }

    override suspend fun update(quiz: Quiz, question: Question) {
        questionDao.updateQuestion(
            QuestionEntity(
                id = question.id,
                title = question.title,
                quizId = quiz.id,
                description = question.description,
                type = question.type,
                choiceTitle1 = question.choiceTitle1,
                choiceTitle2 = question.choiceTitle2,
                choiceTitle3 = question.choiceTitle3,
                choiceTitle4 = question.choiceTitle4,
                correctAnswer = question.correctAnswer,
            )
        )
    }


    suspend fun readAll(): List<Question> {
        return questionDao.getAllQuestions().map {
            Question(
                id = it.id,
                title = it.title,
                description = it.description,
                type = it.type,
                choiceTitle1 = it.choiceTitle1,
                choiceTitle2 = it.choiceTitle2,
                choiceTitle3 = it.choiceTitle3,
                choiceTitle4 = it.choiceTitle4,
                correctAnswer = it.correctAnswer,
            )
        }
    }



}
