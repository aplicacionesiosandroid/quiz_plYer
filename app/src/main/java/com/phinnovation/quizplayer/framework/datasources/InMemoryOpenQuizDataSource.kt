package com.phinnovation.quizplayer.framework

import com.phinnovation.core.data.OpenQuizDataSource
import com.phinnovation.core.domain.Quiz

class InMemoryOpenQuizDataSource : OpenQuizDataSource {

    private var openQuiz: Quiz = Quiz.EMPTY

    override fun setOpenQuiz(quiz: Quiz) {
        openQuiz = quiz
    }

    override fun getOpenQuiz(): Quiz {
        return openQuiz ;
    }
}