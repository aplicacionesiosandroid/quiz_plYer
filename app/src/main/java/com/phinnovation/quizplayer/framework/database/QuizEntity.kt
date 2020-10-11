package com.phinnovation.quizplayer.framework.database

import androidx.room.*
import com.phinnovation.core.domain.QuizState

@Entity(tableName = "quiz")
@TypeConverters(QuizConverters::class)

data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "state") val state: QuizState,
    @ColumnInfo(name = "last_seen_question") val lastSeenQuestion: Int
)

class QuizConverters {
    @TypeConverter
    fun fromInteger(value: Int): QuizState {
        return when (value) {
            0 -> QuizState.NOT_STARTED
            1 -> QuizState.STARTED
            else -> QuizState.FINISHED
        }
    }

    @TypeConverter
    fun stateToInteger(state: QuizState): Int {
        return state.code
    }
}