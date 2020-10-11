package com.phinnovation.quizplayer.presentation.admin.quizlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminQuizListViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

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

    fun setOpenQuiz(quiz: Quiz) {
        interactors.setOpenQuiz(quiz)
    }
}