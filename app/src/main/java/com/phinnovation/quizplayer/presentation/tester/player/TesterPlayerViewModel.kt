package com.phinnovation.quizplayer.presentation.tester.player

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TesterPlayerViewModel(application: Application, interactors: Interactors) :
    QuizPlayerViewModel(application, interactors) {

    private val _answerIsCorrectOrNot = MutableLiveData<Boolean>()
    private var _showNextOrFinishEvent = MutableLiveData<Event<Boolean>>()
    private val _answerCheckedHasMoreQuestions = MutableLiveData<Boolean>()

    val answerCheckedHasMoreQuestionsEvent: LiveData<Boolean>
        get() = _answerCheckedHasMoreQuestions

    val answerIsCorrectEvent: LiveData<Boolean>
        get() = _answerIsCorrectOrNot

    val showNextOrFinishEvent: LiveData<Event<Boolean>>
        get() = _showNextOrFinishEvent


    var currentQuestionItsIndexAndMaxQuestionsTriple:MutableLiveData<Triple<Question,Int,Int>> = MutableLiveData()

    fun getCurrentQuestionToPlay() {
        val quiz = interactors.getOpenQuiz()

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val questions = interactors.getQuestions(quiz)
                currentQuestionItsIndexAndMaxQuestionsTriple.postValue(Triple<Question,Int,Int>(questions[quiz.lastSeenQuestion],quiz.lastSeenQuestion+1,questions.size))
            }
        }
    }

    fun checkQuestionAndShowNextOrFinish(userAnswer:String) {
        val quiz = interactors.getOpenQuiz()

        GlobalScope.launch {
            withContext(Dispatchers.IO) {

                val questions = interactors.getQuestions(quiz)

                if (userAnswer == questions[quiz.lastSeenQuestion].correctAnswer) {
                    _answerIsCorrectOrNot.postValue(true)
                } else {
                    _answerIsCorrectOrNot.postValue(false)
                }

                var hasMoreQuestions = true

                if (quiz.lastSeenQuestion +1 >= questions.size) { //last question update state to finished
                    hasMoreQuestions = false
                }

                _answerCheckedHasMoreQuestions.postValue(hasMoreQuestions)
            }
        }
    }

    fun updateQuizAnsweredQuestionAndContinue() {
        val quiz = interactors.getOpenQuiz()

        GlobalScope.launch {
            withContext(Dispatchers.IO) {

                val questions = interactors.getQuestions(quiz)

                quiz.lastSeenQuestion++

                var showNext = true

                if (quiz.lastSeenQuestion >= questions.size) { //last question update state to finished
                    quiz.state = QuizState.FINISHED
                    showNext = false
                }

                interactors.updateQuiz(quiz)

                _showNextOrFinishEvent.postValue(Event(showNext))
            }
        }
    }


}