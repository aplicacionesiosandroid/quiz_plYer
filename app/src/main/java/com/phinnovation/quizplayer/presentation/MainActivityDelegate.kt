package com.phinnovation.quizplayer.presentation

import com.phinnovation.core.domain.Question

interface MainActivityDelegate {

    fun openQuizForEditing()
    fun openQuestionForEditing(question: Question)
    fun openQuizForPlayIntro()
    fun openQuizForPlayTest()
    fun continueQuizShowNextQuestion()

    fun continueWithBack()
}