package com.phinnovation.quizplayer.presentation.admin.questioneditor

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionEditorViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    private val _navigateUp = MutableLiveData<Event<Boolean>>()

    val navigateUpEvent: LiveData<Event<Boolean>>
        get() = _navigateUp

    var question: MutableLiveData<Question> = MutableLiveData()

    fun getOpenQuestion() {
        question.value = interactors.getOpenQuestion()
    }

    fun updateQuestionAndNavigateUp(title: String, desc:String, type:QuestionType, answer1:String,answer2:String,answer3:String,answer4:String,correctAnswers:String) {

        val quiz = interactors.getOpenQuiz()

        question.value?.let { itQuestion ->

            itQuestion.title = title
            itQuestion.description = desc
            itQuestion.type = type
            itQuestion.choiceTitle1 = answer1
            itQuestion.choiceTitle2 = answer2
            itQuestion.choiceTitle3 = answer3
            itQuestion.choiceTitle4 = answer4
            itQuestion.correctAnswer = correctAnswers

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.updateQuestion(quiz,itQuestion)
                    _navigateUp.postValue(Event(true))
                }
            }
        }
    }

    fun deleteQuestionAndNavigateUp() {

        val quiz = interactors.getOpenQuiz()

        question.value?.let { itQuestion ->
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.removeQuestion(quiz,itQuestion)
                    _navigateUp.postValue(Event(true))
                }
            }
        }
    }
}