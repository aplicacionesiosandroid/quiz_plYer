package com.phinnovation.quizplayer.presentation.screens.tester.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.launch

class TesterQuizListViewModel(application: Application, interactors: Interactors) :
    QuizPlayerViewModel(application, interactors) {

    private val _navigateToQuiz = MutableLiveData<Event<Boolean>>()
    private val _showEmptyQuestionsError = MutableLiveData<Event<Boolean>>()
    private val _showQuizFinishedError = MutableLiveData<Event<Boolean>>()
    private val _quizzes: MutableLiveData<List<Quiz>> = MutableLiveData()

    val navigateToQuizEvent: LiveData<Event<Boolean>>
        get() = _navigateToQuiz

    val showEmptyQuestionsErrorEvent: LiveData<Event<Boolean>>
        get() = _showEmptyQuestionsError

    val showQuizFinishedErrorEvent: LiveData<Event<Boolean>>
        get() = _showQuizFinishedError

    val quizzes: LiveData<List<Quiz>>
        get() = _quizzes

    fun loadQuizzes() {
        viewModelScope.launch {
            _quizzes.value = interactors.getQuizes()
        }
    }

    fun checkQuizCanBeOpened(quiz: Quiz) {

        if (quiz.state == QuizState.FINISHED) {
            _showQuizFinishedError.value = Event(true)
        } else {
            viewModelScope.launch {
                val questions = interactors.getQuestions(quiz)

                if (questions.isNotEmpty()) {
                    //we will continue to the next screen
                    //set openQuiz and post navigateToQuiz
                    setOpenQuiz(quiz)
                    _navigateToQuiz.value = Event(true)
                } else {
                    //no questions, quiz is not setup yet, post error
                    _showEmptyQuestionsError.value = Event(true)
                }
            }
        }
    }

    private fun setOpenQuiz(quiz: Quiz) {
        interactors.setOpenQuiz(quiz)
    }
}