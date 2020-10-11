package com.phinnovation.quizplayer.framework.database

import androidx.room.*
import com.phinnovation.core.domain.QuestionType

@Entity(tableName = "question")
@TypeConverters(QuestionConverters::class)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "quizId") val quizId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type: QuestionType,
    @ColumnInfo(name = "choiceTitle1") val choiceTitle1: String,
    @ColumnInfo(name = "choiceTitle2") val choiceTitle2: String,
    @ColumnInfo(name = "choiceTitle3") val choiceTitle3: String,
    @ColumnInfo(name = "choiceTitle4") val choiceTitle4: String,
    @ColumnInfo(name = "correctAnswer") val correctAnswer: String,
)

class QuestionConverters {
    @TypeConverter
    fun fromInteger(value: Int): QuestionType {
        return when (value) {
            0 -> QuestionType.SINGLE_CHOICE
            else -> QuestionType.MULTI_CHOICE
        }
    }

    @TypeConverter
    fun statusToInteger(status: QuestionType): Int {
        return status.code
    }
}