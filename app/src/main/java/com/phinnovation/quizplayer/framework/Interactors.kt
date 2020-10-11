package com.phinnovation.quizplayer.framework

import com.phinnovation.core.interactors.*

data class Interactors(
    val addQuiz: AddQuiz,
    val removeQuiz: RemoveQuiz,
    val getQuizes: GetQuizes,
    val addQuestion: AddQuestion,
    val removeQuestion: RemoveQuestion,
    val getQuestions: GetQuestions,
    val getOpenQuiz: GetOpenQuiz,
    val setOpenQuiz: SetOpenQuiz,
    val updateQuiz: UpdateQuiz,
    val getOpenQuestion: GetOpenQuestion,
    val setOpenQuestion: SetOpenQuestion,
    val updateQuestion: UpdateQuestion
)