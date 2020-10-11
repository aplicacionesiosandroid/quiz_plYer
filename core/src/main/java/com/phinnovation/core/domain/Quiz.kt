package com.phinnovation.core.domain

enum class QuizState (val code:Int){
    NOT_STARTED(0),
    STARTED(1),
    FINISHED(2)
}

data class Quiz (
    var id:Int,
    var title: String,
    var description:String,
    var state: QuizState,
    var lastSeenQuestion: Int
    ) {
    companion object {
        val EMPTY = Quiz(0, "", description = "", QuizState.NOT_STARTED, 0)
    }
}
