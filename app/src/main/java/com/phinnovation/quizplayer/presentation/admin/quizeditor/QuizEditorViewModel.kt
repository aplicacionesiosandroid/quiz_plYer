package com.phinnovation.quizplayer.presentation.admin.quizeditor

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.framework.Interactors
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizEditorViewModel (application: Application, interactors: Interactors):
        QuizPlayerViewModel(application,interactors) {

    var quiz: MutableLiveData<Quiz> = MutableLiveData()

    val questions: MutableLiveData<List<Question>> = MutableLiveData()

    var quizUpdated: MutableLiveData<Boolean> = MutableLiveData()
    //val quizAndQuestionsMediatedPair: MediatorLiveData<Pair<Quiz,List<Question>>> = MediatorLiveData()

//    init {
//        quizAndQuestionsMediatedPair.addSource(quiz) { _ ->
//            quizAndQuestionsMediatedPair.value = combineQuizAndQuestions(quiz,questions)
//        }
//
//        quizAndQuestionsMediatedPair.addSource(questions) { _ ->
//            quizAndQuestionsMediatedPair.value = combineQuizAndQuestions(quiz,questions)
//        }
//
//    }

//    private fun combineQuizAndQuestions(quiz: MutableLiveData<Quiz>, questions: MutableLiveData<List<Question>>): Pair<Quiz, List<Question>>? {
//        val qz = quiz.value
//        val qs = questions.value
//
//        if (qz == null || qs == null) {
//            if (qz != null) {
//                loadQuizQuestions(qz)
//            }
//            return null
//        }
//
//        return Pair(qz,qs)
//    }

    fun getOpenQuizAndQuestions() {
        quiz.postValue(interactors.getOpenQuiz())
    }

    fun loadQuizQuestions(quiz:Quiz) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                questions.postValue(interactors.getQuestions(quiz))
            }
        }
    }

    fun updateQuiz(title: String, desc:String) {
        quiz.value?.let {
            it.title = title ;
            it.description = desc ;

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    interactors.updateQuiz(it)
                    quizUpdated.postValue(true)
                }
            }
        }
    }

    fun deleteQuiz() {
        quiz.value?.let {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {

                    for (question in questions.value!!) {
                        interactors.removeQuestion(it,question)
                    }

                    interactors.removeQuiz(it)
                }
            }
        }
    }

    fun addQuestion() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                quiz.value?.let { interactors.addQuestion(it,
                    Question(title = "New Question - Click to setup"))
                }
            }
            quiz.value?.let { loadQuizQuestions(it) }
        }
    }

    fun setOpenQuestion(question: Question) {
        interactors.setOpenQuestion (question)
    }
}