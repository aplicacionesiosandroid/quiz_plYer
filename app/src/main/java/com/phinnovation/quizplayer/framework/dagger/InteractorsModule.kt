package com.phinnovation.quizplayer.framework.dagger

import com.phinnovation.core.data.QuestionRepository
import com.phinnovation.core.data.QuizRepository
import com.phinnovation.core.interactors.*
import com.phinnovation.quizplayer.framework.Interactors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorsModule {

    @Provides
    @Singleton
    fun provideInteractors(quizRepository: QuizRepository, questionRepository: QuestionRepository) = Interactors(
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
}