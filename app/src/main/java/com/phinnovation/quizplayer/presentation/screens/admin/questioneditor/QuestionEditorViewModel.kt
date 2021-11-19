package com.phinnovation.quizplayer.presentation.screens.admin.questioneditor

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.launch

class QuestionEditorViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    private val _navigateUp = MutableLiveData<Event<Boolean>>()
    private val _question: MutableLiveData<Question> = MutableLiveData()

    val navigateUpEvent: LiveData<Event<Boolean>>
        get() = _navigateUp

    val question:LiveData<Question>
        get() = _question

    fun getOpenQuestion() {
        _question.value = interactors.getOpenQuestion()
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

            viewModelScope.launch {
                interactors.updateQuestion(quiz,itQuestion)
                _navigateUp.value = Event(true)
            }
        }
    }

    fun deleteQuestionAndNavigateUp() {
        val quiz = interactors.getOpenQuiz()

        question.value?.let { itQuestion ->
            viewModelScope.launch {
                interactors.removeQuestion(quiz,itQuestion)
                _navigateUp.value = Event(true)
            }
        }
    }
}