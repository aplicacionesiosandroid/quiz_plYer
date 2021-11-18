package com.phinnovation.quizplayer.presentation.admin.quizeditor

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.presentation.utils.Event
import kotlinx.coroutines.launch

class QuizEditorViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    private val _navigateToQuestion = MutableLiveData<Event<Boolean>>()
    private val _navigateUp = MutableLiveData<Event<Boolean>>()

    val navigateToQuestionEvent: LiveData<Event<Boolean>>
        get() = _navigateToQuestion

    val navigateUpEvent: LiveData<Event<Boolean>>
        get() = _navigateUp

    var quiz: MutableLiveData<Quiz> = MutableLiveData()

    val questions: MutableLiveData<List<Question>> = MutableLiveData()

    fun getOpenQuizAndLoadQuestions() {
        val newQuiz = interactors.getOpenQuiz()

        quiz.value = newQuiz
        loadQuizQuestions(newQuiz)
    }

    fun updateQuizPrepareToOpenQuestion(title: String, desc:String, question:Question) {
        quiz.value?.let {
            it.title = title ;
            it.description = desc ;

            viewModelScope.launch {
                interactors.updateQuiz(it)
                setOpenQuestion(question)
                _navigateToQuestion.postValue(Event(true))
            }
        }
    }

    fun updateQuizAndClose(title: String, desc:String) {
        quiz.value?.let {
            it.title = title ;
            it.description = desc ;

            viewModelScope.launch {
                interactors.updateQuiz(it)
                _navigateUp.postValue(Event(true))
            }
        }
    }

    fun deleteQuizAndClose() {
        quiz.value?.let {
            viewModelScope.launch {
                for (question in questions.value!!) {
                    interactors.removeQuestion(it,question)
                }

                interactors.removeQuiz(it)
                _navigateUp.postValue(Event(true))
            }
        }
    }

    fun addQuestion() {
        viewModelScope.launch {
            quiz.value?.let {
                interactors.addQuestion(it, Question(title = "New Question - Click to setup"))
                loadQuizQuestions(it)
            }
        }
    }

    private fun setOpenQuestion(question: Question) {
        interactors.setOpenQuestion (question)
    }

    private fun loadQuizQuestions(quiz:Quiz) {
        viewModelScope.launch {
            questions.postValue(interactors.getQuestions(quiz))
        }
    }

}