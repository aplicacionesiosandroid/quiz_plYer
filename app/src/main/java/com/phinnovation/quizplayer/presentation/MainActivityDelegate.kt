package com.phinnovation.quizplayer.presentation

import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz

interface MainActivityDelegate {

    fun openQuizForEditing(quiz: Quiz)
    fun openQuestionForEditing(question: Question)
    fun openQuizForPlayIntro()
    fun openQuizForPlayTest()
    fun continueQuizShowNextQuestion()

    fun continueWithBack()
}