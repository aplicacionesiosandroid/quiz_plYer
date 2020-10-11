package com.phinnovation.quizplayer.framework

import android.content.Context
import com.phinnovation.core.data.QuizDataSource
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.framework.database.QuizDao
import com.phinnovation.quizplayer.framework.database.QuizEntity
import com.phinnovation.quizplayer.framework.database.QuizPlayerDatabase

class RoomQuizDataSource(val context: Context, quizDao: QuizDao? = null) : QuizDataSource {

//    private val quizDao = QuizPlayerDatabase.getInstance(context).quizDao()

    private val quizDao: QuizDao = {
        if (quizDao != null) {
            quizDao
        } else {
            QuizPlayerDatabase.getInstance(context).quizDao()
        }
    }()

    override suspend fun add(quiz: Quiz) {
        quizDao.addQuiz(
            QuizEntity(
                title = quiz.title,
                description = quiz.description,
                state = quiz.state,
                lastSeenQuestion = quiz.lastSeenQuestion
            )
        )
    }

    override suspend fun readAll(): List<Quiz> {
        return quizDao.getQuizes().map {
            Quiz(
                it.id,
                it.title,
                it.description,
                it.state,
                it.lastSeenQuestion
            )
        }
    }

    override suspend fun remove(quiz: Quiz) {
        quizDao.removeQuiz(
            QuizEntity(
                quiz.id,
                quiz.title,
                quiz.description,
                quiz.state,
                quiz.lastSeenQuestion
            )
        )
    }

    override suspend fun update(quiz: Quiz) {
        quizDao.updateQuiz(
            QuizEntity(
                quiz.id,
                quiz.title,
                quiz.description,
                quiz.state,
                quiz.lastSeenQuestion
            )
        )
    }

}