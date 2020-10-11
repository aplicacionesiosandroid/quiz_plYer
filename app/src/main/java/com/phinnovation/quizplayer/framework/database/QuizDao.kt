package com.phinnovation.quizplayer.framework.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface QuizDao {

    @Insert(onConflict = REPLACE)
     fun addQuiz(quiz: QuizEntity)

    @Query("SELECT * FROM quiz")
     fun getQuizes(): List<QuizEntity>

    @Delete
     fun removeQuiz(quiz: QuizEntity)

    @Update
     fun updateQuiz(quiz:QuizEntity)
}