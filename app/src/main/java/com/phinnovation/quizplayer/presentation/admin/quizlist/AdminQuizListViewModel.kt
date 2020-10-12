package com.phinnovation.quizplayer.presentation.admin.quizlist

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

class AdminQuizListViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    private val _navigateToQuiz = MutableLiveData<Event<Boolean>>()

    val navigateToQuizEvent: LiveData<Event<Boolean>>
        get() = _navigateToQuiz

    val quizzes: MutableLiveData<List<Quiz>> = MutableLiveData()

    fun loadQuizzes() {
        GlobalScope.launch {
            quizzes.postValue(interactors.getQuizes())
        }
    }

    fun addQuiz() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.addQuiz(Quiz(id = 0,title = "New Quiz - Click to setup",description = "desc",state = QuizState.NOT_STARTED,0))
            }
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