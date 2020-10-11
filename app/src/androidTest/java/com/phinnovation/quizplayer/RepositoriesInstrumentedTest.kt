package com.phinnovation.quizplayer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.phinnovation.core.data.QuestionRepository
import com.phinnovation.core.data.QuizRepository
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.core.domain.Quiz
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.framework.InMemoryOpenQuestionDataSource
import com.phinnovation.quizplayer.framework.InMemoryOpenQuizDataSource
import com.phinnovation.quizplayer.framework.RoomQuestionDataSource
import com.phinnovation.quizplayer.framework.RoomQuizDataSource
import com.phinnovation.quizplayer.framework.database.QuestionDao
import com.phinnovation.quizplayer.framework.database.QuizDao
import com.phinnovation.quizplayer.framework.database.QuizPlayerDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoriesInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.phinnovation.quizplayer", appContext.packageName)
    }

    private lateinit var quizDao: QuizDao
    private lateinit var questionDao: QuestionDao
    private lateinit var db: QuizPlayerDatabase

    private lateinit var questionRepository: QuestionRepository
    private lateinit var quizRepository: QuizRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, QuizPlayerDatabase::class.java
        ).build()
        quizDao = db.quizDao()
        questionDao = db.questionDao()

        questionRepository = QuestionRepository(
            RoomQuestionDataSource(InstrumentationRegistry.getInstrumentation().targetContext,questionDao),
            InMemoryOpenQuestionDataSource()
        )

        quizRepository = QuizRepository(
            RoomQuizDataSource(InstrumentationRegistry.getInstrumentation().targetContext,quizDao),
            InMemoryOpenQuizDataSource()
        )

    }

    @Test
    @Throws(Exception::class)
    fun repositoriesTestWriteQuizReadCheckAddQuestionReadCheckRemoveBothCheck() = runBlocking {

        //Make a Quiz Entity
        val quiz: Quiz = Quiz(
            id = 1,
            title = "test",
            description = "test",
            state = QuizState.NOT_STARTED,
            lastSeenQuestion = 0
        )

        //Add it to the db
        quizRepository.addQuiz(quiz)
        var quizes = quizRepository.getQuizes()

        //Test that we got back a single quiz
        assertEquals(quizes.size, 1)

        //Test title and state
        assertEquals(quizes[0].title, "test")
        assertEquals(quizes[0].state, QuizState.NOT_STARTED)


        //Make a Question Entity
        val question = Question(
            id = 1,
            title = "Question 1",
            description = "Desc 1",
            type = QuestionType.SINGLE_CHOICE,
            choiceTitle1 = "Answer 1",
            choiceTitle2 = "Answer 2",
            choiceTitle3 = "Answer 3",
            choiceTitle4 = "Answer 4",
            correctAnswer = "0"
        )

        questionRepository.addQuestion(quiz,question)

        var questions = questionRepository.getQuestions(quiz)

        //Test that we got back a single question
        assertEquals(questions.size, 1)

        //Test title and statequestions
        assertEquals(questions[0].title, "Question 1")
        assertEquals(questions[0].type, QuestionType.SINGLE_CHOICE)

        questionRepository.removeQuestion(quiz,question)
        questions = questionRepository.getQuestions(quiz)
        assertEquals(questions.size, 0)

        quizRepository.removeQuiz(quiz)
        quizes = quizRepository.getQuizes()
        assertEquals(quizes.size, 0)

    }

}