package com.phinnovation.quizplayer.presentation.screens.tester.player

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.launch

class TesterPlayerViewModel(application: Application, interactors: Interactors) :
    QuizPlayerViewModel(application, interactors) {

    private val _answerIsCorrectOrNot = MutableLiveData<Boolean>()
    private val _showNextOrFinishEvent = MutableLiveData<Event<Boolean>>()
    private val _answerCheckedHasMoreQuestions = MutableLiveData<Boolean>()
    private val _currentQuestionItsIndexAndMaxQuestionsTriple:MutableLiveData<Triple<Question,Int,Int>> = MutableLiveData()

    val answerCheckedHasMoreQuestions: LiveData<Boolean>
        get() = _answerCheckedHasMoreQuestions

    val answerIsCorrect: LiveData<Boolean>
        get() = _answerIsCorrectOrNot

    val showNextOrFinishEvent: LiveData<Event<Boolean>>
        get() = _showNextOrFinishEvent

    val currentQuestionItsIndexAndMaxQuestionsTriple: LiveData<Triple<Question,Int,Int>>
        get() = _currentQuestionItsIndexAndMaxQuestionsTriple


    fun getCurrentQuestionToPlay() {
        val quiz = interactors.getOpenQuiz()

        viewModelScope.launch {
            val questions = interactors.getQuestions(quiz)
            _currentQuestionItsIndexAndMaxQuestionsTriple.value = Triple<Question,Int,Int>(questions[quiz.lastSeenQuestion],quiz.lastSeenQuestion+1,questions.size)
        }
    }

    fun checkQuestionAndShowNextOrFinish(userAnswer:String) {
        val quiz = interactors.getOpenQuiz()

        viewModelScope.launch {
            val questions = interactors.getQuestions(quiz)

            _answerIsCorrectOrNot.value = userAnswer == questions[quiz.lastSeenQuestion].correctAnswer

            var hasMoreQuestions = true

            if (quiz.lastSeenQuestion +1 >= questions.size) { //last question update state to finished
                hasMoreQuestions = false
            }

            _answerCheckedHasMoreQuestions.value = hasMoreQuestions
        }
    }

    fun updateQuizAnsweredQuestionAndContinue() {
        val quiz = interactors.getOpenQuiz()

        viewModelScope.launch {
            val questions = interactors.getQuestions(quiz)

            quiz.lastSeenQuestion++

            var showNext = true

            if (quiz.lastSeenQuestion >= questions.size) { //last question update state to finished
                quiz.state = QuizState.FINISHED
                showNext = false
            }

            interactors.updateQuiz(quiz)

            _showNextOrFinishEvent.value = Event(showNext)
        }
    }

    fun updateQuizAnsweredQuestionAndNavigateUp() {
        val quiz = interactors.getOpenQuiz()

        viewModelScope.launch {
            val questions = interactors.getQuestions(quiz)

            quiz.lastSeenQuestion++

            if (quiz.lastSeenQuestion >= questions.size) { //last question update state to finished
                quiz.state = QuizState.FINISHED
            }

            interactors.updateQuiz(quiz)

            _showNextOrFinishEvent.value = Event(false)
        }
    }
}