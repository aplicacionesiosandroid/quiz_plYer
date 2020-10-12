package com.phinnovation.quizplayer.presentation

interface MainActivityDelegate {

    fun openQuizForEditing()
    fun openQuestionForEditing()
    fun openQuizForPlayIntro()
    fun openQuizForPlayTest()
    fun continueQuizShowNextQuestion()

    fun continueWithBack()
}