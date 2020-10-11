package com.phinnovation.quizplayer.framework.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface QuestionDao {

    @Insert(onConflict = REPLACE)
     fun addQuestion(question: QuestionEntity)

    @Query("SELECT * FROM question WHERE quizId = :quizId")
     fun getQuestions(quizId: Int): List<QuestionEntity>

    @Query("SELECT * FROM question")
    fun getAllQuestions(): List<QuestionEntity>

    @Delete
     fun removeQuestion(question: QuestionEntity)

    @Update
    fun updateQuestion(question:QuestionEntity);

}