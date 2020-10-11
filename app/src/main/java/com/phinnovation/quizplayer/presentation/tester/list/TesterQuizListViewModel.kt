package com.phinnovation.quizplayer.presentation.tester.list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TesterQuizListViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    val quizzes: MutableLiveData<List<Quiz>> = MutableLiveData()

    val hasQuestionsCanBeOpened: MutableLiveData<Boolean> = MutableLiveData()

    fun loadQuizzes() {
        GlobalScope.launch {
            quizzes.postValue(interactors.getQuizes())
        }
    }

    fun checkIfQuizHasQuestionsAndThusCanBeOpened(quiz:Quiz) {
        GlobalScope.launch {
            val questions = interactors.getQuestions(quiz)

            hasQuestionsCanBeOpened.postValue(questions.isNotEmpty())
        }
    }

    fun setOpenQuiz(quiz: Quiz) {
        interactors.setOpenQuiz(quiz)
    }
}