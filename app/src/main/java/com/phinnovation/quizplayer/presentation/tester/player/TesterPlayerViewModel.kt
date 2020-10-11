package com.phinnovation.quizplayer.presentation.tester.player

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TesterPlayerViewModel(application: Application, interactors: Interactors) :
    QuizPlayerViewModel(application, interactors) {

    private var quiz: MutableLiveData<Quiz> = MutableLiveData()
    private val questions: MutableLiveData<List<Question>> = MutableLiveData()

    var answerChecked: MutableLiveData<Boolean> = MutableLiveData()
    var answerCorrect: MutableLiveData<Boolean> = MutableLiveData()

    var showEndButton: MutableLiveData<Boolean> = MutableLiveData()

    val quizAndQuestionsMediatedPair: MediatorLiveData<Pair<Quiz, List<Question>>> =
        MediatorLiveData()

    init {
        quizAndQuestionsMediatedPair.addSource(quiz) { _ ->
            quizAndQuestionsMediatedPair.value = combineQuizAndQuestions(quiz, questions)
        }

        quizAndQuestionsMediatedPair.addSource(questions) { _ ->
            quizAndQuestionsMediatedPair.value = combineQuizAndQuestions(quiz, questions)
        }

    }

    fun getOpenQuizAndQuestions() {
        quiz.postValue(interactors.getOpenQuiz())
    }

//    fun setQuestionChecked(correct: Boolean , hasMore:Boolean) {
//        answerChecked.value = true
//
//        if (hasMore) {
//            answerCorrect.value = correct
//        } else {
//            showEndButton.value = true
//        }
//    }


    private fun combineQuizAndQuestions(
        quiz: MutableLiveData<Quiz>,
        questions: MutableLiveData<List<Question>>
    ): Pair<Quiz, List<Question>>? {
        val qz = quiz.value
        val qs = questions.value

        if (qz == null || qs == null) {
            if (qz != null) {
                loadQuizQuestions(qz)
            }
            return null
        }

        return Pair(qz, qs)
    }

    private fun loadQuizQuestions(quiz: Quiz) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                questions.postValue(interactors.getQuestions(quiz))
            }
        }
    }


    fun updateQuizAnsweredQuestionAndMoveToNextOrFinish(quiz:Quiz, correctAnswer:Boolean, hasMore:Boolean) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                quiz.lastSeenQuestion++

                if (!hasMore) { //last question update state to finished
                    quiz.state = QuizState.FINISHED
                }

                interactors.updateQuiz(quiz)

                answerCorrect.postValue(correctAnswer)

                if (hasMore) {
                    answerChecked.postValue(true)
                } else {
                    showEndButton.postValue(true)
                }
            }
        }
    }


//    fun setOpenQuestion(question: Question) {
//        interactors.setOpenQuestion (question)
//    }
}