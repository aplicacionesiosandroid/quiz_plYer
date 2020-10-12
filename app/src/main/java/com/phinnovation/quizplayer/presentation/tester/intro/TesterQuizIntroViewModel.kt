package com.phinnovation.quizplayer.presentation.tester.intro

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TesterQuizIntroViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    private val _navigateToQuizPlayScreen = MutableLiveData<Event<Boolean>>()

    val navigateToQuizPlayScreenEvent: LiveData<Event<Boolean>>
        get() = _navigateToQuizPlayScreen

    var quiz: MutableLiveData<Quiz> = MutableLiveData()

    fun getOpenQuiz() {
        quiz.postValue(interactors.getOpenQuiz())
    }

    fun updateQuizStatusToStarted() {
        quiz.value?.let {
            it.state = QuizState.STARTED ;

            val job = GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.updateQuiz(it)
                    _navigateToQuizPlayScreen.postValue(Event(true))
                }
            }
        }
    }


//    fun setOpenQuestion(question: Question) {
//        interactors.setOpenQuestion (question)
//    }
}