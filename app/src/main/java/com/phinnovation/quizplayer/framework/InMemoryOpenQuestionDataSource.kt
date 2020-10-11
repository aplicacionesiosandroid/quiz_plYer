package com.phinnovation.quizplayer.framework

import com.phinnovation.core.data.OpenQuestionDataSource
import com.phinnovation.core.domain.Question

class InMemoryOpenQuestionDataSource : OpenQuestionDataSource {

    private var openQuestion: Question = Question.EMPTY

    override fun setOpenQuestion(question: Question) {
        openQuestion = question
    }

    override fun getOpenQuestion(): Question {
        return openQuestion ;
    }
}