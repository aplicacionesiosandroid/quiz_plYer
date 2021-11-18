package com.phinnovation.quizplayer.presentation.admin.quizlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.*

class AdminQuizListViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    private val _navigateToQuiz = MutableLiveData<Event<Boolean>>()
    private val _quizzes: MutableLiveData<List<Quiz>> = MutableLiveData()

    val navigateToQuizEvent: LiveData<Event<Boolean>>
        get() = _navigateToQuiz

    val quizzes:LiveData<List<Quiz>>
        get() = _quizzes

    fun loadQuizzes() {
        viewModelScope.launch {
            _quizzes.value = interactors.getQuizes()
        }
    }

    fun addQuiz() {
        viewModelScope.launch {
            interactors.addQuiz(Quiz(id = 0,title = "New Quiz - Click to setup",description = "desc",state = QuizState.NOT_STARTED,0))
            loadQuizzes()
        }
    }

    fun setOpenQuizAndNavigate(quiz:Quiz) {
        setOpenQuiz(quiz)
        _navigateToQuiz.value = Event(true)
    }

    private fun setOpenQuiz(quiz: Quiz) {
        interactors.setOpenQuiz(quiz)
    }
}