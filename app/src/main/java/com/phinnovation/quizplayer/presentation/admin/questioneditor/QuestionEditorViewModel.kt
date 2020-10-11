package com.phinnovation.quizplayer.presentation.admin.questioneditor

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionEditorViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    var questionUpdated: MutableLiveData<Boolean> = MutableLiveData()

    var quiz: MutableLiveData<Quiz> = MutableLiveData()
    var question: MutableLiveData<Question> = MutableLiveData()

    fun getOpenQuestion() {
        quiz.postValue(interactors.getOpenQuiz())
        question.postValue(interactors.getOpenQuestion())
    }


    fun updateQuestion(title: String, desc:String, type:QuestionType, answer1:String,answer2:String,answer3:String,answer4:String,correctAnswers:String) {

        quiz.value?.let { itQuiz ->
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
                        interactors.updateQuestion(itQuiz,itQuestion)
                        questionUpdated.postValue(true)
                    }
                }
            }
        }
    }

    fun deleteQuestion() {
        quiz.value?.let { itQuiz ->
            question.value?.let { itQuestion ->
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        interactors.removeQuestion(itQuiz,itQuestion)
                    }
                }
            }
        }
    }
}