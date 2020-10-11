package com.phinnovation.quizplayer.framework

import android.app.Application
import com.phinnovation.core.data.QuestionRepository
import com.phinnovation.core.data.QuizRepository
import com.phinnovation.core.interactors.*

class QuizPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val questionRepository = QuestionRepository(
            RoomQuestionDataSource(this),
            InMemoryOpenQuestionDataSource()
        )

        val quizRepository = QuizRepository(
            RoomQuizDataSource(this),
            InMemoryOpenQuizDataSource()
        )

        QuizPlayerViewModelFactory.inject(
            this,
            Interactors(
                AddQuiz(quizRepository),
                RemoveQuiz(quizRepository),
                GetQuizes(quizRepository),
                AddQuestion(questionRepository),
                RemoveQuestion(questionRepository),
                GetQuestions(questionRepository),
                GetOpenQuiz(quizRepository),
                SetOpenQuiz(quizRepository),
                UpdateQuiz(quizRepository),
                GetOpenQuestion(questionRepository),
                SetOpenQuestion(questionRepository),
                UpdateQuestion(questionRepository)
            )
        )
    }
}