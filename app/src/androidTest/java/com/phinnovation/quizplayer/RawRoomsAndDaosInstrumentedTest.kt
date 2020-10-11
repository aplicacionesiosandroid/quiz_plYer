package com.phinnovation.quizplayer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.database.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RawRoomsAndDaosInstrumentedTest {

    private lateinit var quizDao: QuizDao
    private lateinit var questionDao: QuestionDao
    private lateinit var db: QuizPlayerDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, QuizPlayerDatabase::class.java
        ).build()
        quizDao = db.quizDao()
        questionDao = db.questionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun rawRoomsTestWriteQuizReadCheckAddQuestionReadCheckRemoveBothCheck() {
        //Make a Quiz Entity
        val quizEnity: QuizEntity = QuizEntity(
            id = 1,
            title = "test",
            description = "test",
            state = QuizState.NOT_STARTED,
            lastSeenQuestion = 0
        )

        //Add it to the db
        quizDao.addQuiz(quizEnity)
        var quizes = quizDao.getQuizes()

        //Test title and state
        assertEquals(quizes[0].title, "test")
        assertEquals(quizes[0].state, QuizState.NOT_STARTED)

        print("There are ${quizes.size} quizes in the DB!") ;

        //Test Equal to expected object
        MatcherAssert.assertThat(quizes[0], CoreMatchers.equalTo(quizEnity))

        //Make a Question Entity
        val questionEntity = QuestionEntity(
            id = 1,
            quizId = quizes[0].id,
            title = "Question 1",
            description = "Desc 1",
            type = QuestionType.SINGLE_CHOICE,
            choiceTitle1 = "Answer 1",
            choiceTitle2 = "Answer 2",
            choiceTitle3 = "Answer 3",
            choiceTitle4 = "Answer 4",
            correctAnswer = "0"
        )

        questionDao.addQuestion(questionEntity)

        var questions = questionDao.getQuestions(quizes[0].id)

        //Test title and state
        assertEquals(questions[0].title, "Question 1")
        assertEquals(questions[0].type, QuestionType.SINGLE_CHOICE)

        //Test Equal to expected object
        MatcherAssert.assertThat(questions[0], CoreMatchers.equalTo(questionEntity))

        questionDao.removeQuestion(questionEntity)
        questions = questionDao.getQuestions(quizes[0].id)
        assertEquals(questions.size, 0)

        quizDao.removeQuiz(quizEnity)
        quizes = quizDao.getQuizes()
        assertEquals(quizes.size, 0)

    }

}