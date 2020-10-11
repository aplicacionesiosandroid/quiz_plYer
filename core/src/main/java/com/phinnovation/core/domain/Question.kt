package com.phinnovation.core.domain

enum class QuestionType (val code:Int) {
    SINGLE_CHOICE(0),
    MULTI_CHOICE(1)
}

data class Question(
    var id:Int = 0,
    var title: String = "",
    var description: String = "",
    var type: QuestionType = QuestionType.SINGLE_CHOICE,
    var choiceTitle1:String = "",
    var choiceTitle2:String = "",
    var choiceTitle3:String = "",
    var choiceTitle4:String = "",
    var correctAnswer:String = "0"//Coma sep answers SINGLE_CHOICE has 1 correct, MULTI_CHOICE has multiple correct
) {
    companion object {
        val EMPTY = Question(0)
    }
}