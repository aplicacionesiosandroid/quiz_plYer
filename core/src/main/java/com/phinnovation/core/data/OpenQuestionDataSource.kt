package com.phinnovation.core.data

import com.phinnovation.core.domain.Question

interface OpenQuestionDataSource {

    fun setOpenQuestion(question: Question)

    fun getOpenQuestion(): Question
}