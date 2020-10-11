package com.phinnovation.quizplayer.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [QuestionEntity::class, QuizEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuizPlayerDatabase : RoomDatabase() {

    enum class DbType {
        DISK,
        IN_MEMORY
    }

    companion object {

        private const val DATABASE_NAME = "quiz_player.db"

        private var instance: QuizPlayerDatabase? = null
        private var inMemoryInstance: QuizPlayerDatabase? = null

        private fun create(context: Context, dbType:DbType = DbType.DISK): QuizPlayerDatabase =
            Room.databaseBuilder(context, QuizPlayerDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

//        private fun create(context: Context, dbType:DbType = DbType.DISK): QuizPlayerDatabase {
//            if (dbType == DbType.DISK) {
//                return Room.databaseBuilder(context, QuizPlayerDatabase::class.java, DATABASE_NAME)
//                    .fallbackToDestructiveMigration()
//                    .build()
//            } else {
//                return Room.inMemoryDatabaseBuilder(
//                    context, QuizPlayerDatabase::class.java
//                ).fallbackToDestructiveMigration().build()
//
//            }
//        }

        fun getInstance(context: Context, dbType:DbType = DbType.DISK): QuizPlayerDatabase =
            (instance ?: create(context,dbType)).also { instance = it }

//        fun getInstance(context: Context, dbType:DbType = DbType.DISK): QuizPlayerDatabase {
//            if (dbType == DbType.DISK) {
//                if (instance == null) {
//                    instance = create(context)
//                }
//
//                return instance as QuizPlayerDatabase
//            } else {
//                if (inMemoryInstance == null) {
//                    inMemoryInstance = create(context,DbType.IN_MEMORY)
//                }
//
//                return inMemoryInstance as QuizPlayerDatabase
//            }
//        }


    }

    abstract fun questionDao(): QuestionDao

    abstract fun quizDao(): QuizDao

}