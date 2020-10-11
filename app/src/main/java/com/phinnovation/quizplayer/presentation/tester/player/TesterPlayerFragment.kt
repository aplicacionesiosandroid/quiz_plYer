package com.phinnovation.quizplayer.presentation.tester.player

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.core.domain.Quiz
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import com.phinnovation.quizplayer.presentation.utils.FragmentReceiveOnBack
import com.phinnovation.quizplayer.presentation.utils.RadioGroupCheckListener
import kotlinx.android.synthetic.main.fragment_tester_player.*

class TesterPlayerFragment : Fragment (), FragmentReceiveOnBack  {

    private lateinit var viewModel: TesterPlayerViewModel

    private lateinit var mainActivityDelegate: MainActivityDelegate

    private lateinit var quiz:Quiz
    private lateinit var questions:List<Question>

    private lateinit var currentQuestion:Question

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tester_player, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, QuizPlayerViewModelFactory)
            .get(TesterPlayerViewModel::class.java)

        viewModel.quizAndQuestionsMediatedPair.observe(viewLifecycleOwner, Observer {

            if (it == null) { //first call is null, questions not ready
                return@Observer
            }

            RadioGroupCheckListener.makeGroup(radio_1, radio_2, radio_3, radio_4);

            quiz = it.first
            questions = it.second

            //sanity check if current question > questions.size exit early
            if (quiz.lastSeenQuestion >= questions.size) {
                return@Observer
            }

            currentQuestion = questions[quiz.lastSeenQuestion]

            quizQuestionNumber.text = getString(R.string.question_num_out_of,quiz.lastSeenQuestion+1,questions.size)
            quizTitle.text = questions[quiz.lastSeenQuestion].title
            quizDescription.text = questions[quiz.lastSeenQuestion].description

            answer_1.text = currentQuestion.choiceTitle1
            answer_2.text = currentQuestion.choiceTitle2
            answer_3.text = currentQuestion.choiceTitle3
            answer_4.text = currentQuestion.choiceTitle4

            enableRadiosForTrueOrCheckboxes(currentQuestion.type == QuestionType.SINGLE_CHOICE)

        })

        viewModel.getOpenQuizAndQuestions()

        viewModel.answerChecked.observe(viewLifecycleOwner, Observer {
            nextQuestionButton.text = getString(R.string.next_question)
        })

        viewModel.showEndButton.observe(viewLifecycleOwner, Observer {
            nextQuestionButton.text = getString(R.string.test_ended)
        })

        viewModel.answerCorrect.observe(viewLifecycleOwner, Observer {
            answerText.visibility = View.VISIBLE

            if (it) {
                answerText.setTextColor(Color.GREEN)
                answerText.text = "You are correct!"
            } else {
                answerText.setTextColor(Color.RED)
                answerText.text = "Wrong Answer"
            }
        })


        nextQuestionButton.setOnClickListener {

            if (viewModel.showEndButton.value == true) {
                mainActivityDelegate.continueWithBack()
            } else {
                if (viewModel.answerChecked.value == true) {
                    mainActivityDelegate.continueQuizShowNextQuestion()
                } else {
                    //check that an answer exist
                    val answer = findAnswerFromUI()

                    if (answer == "" || answer == "0,0,0,0") { //not selected answer
                        showErrorSelectionDialog()
                    } else {

                        //increment the quiz.lastSeenQuestion and show continue or end button
                        viewModel.updateQuizAnsweredQuestionAndMoveToNextOrFinish(quiz,answer == currentQuestion.correctAnswer,(quiz.lastSeenQuestion + 1) < questions.size)
                    }
                }
            }
        }
    }

    private fun findAnswerFromUI(): String {

        var result:String = "" ;

        if (currentQuestion.type == QuestionType.SINGLE_CHOICE) {

            var selectedIndex = -1

            if (radio_1.isChecked)
                selectedIndex = 0

            if (radio_2.isChecked)
                selectedIndex = 1

            if (radio_3.isChecked)
                selectedIndex = 2

            if (radio_4.isChecked)
                selectedIndex = 3

            if (selectedIndex != -1) //nothing selected
            {
                result = "$selectedIndex"
            }
        } else {

            result = if (checkbox_1.isChecked) {
                "1,"
            } else {
                "0,"
            }

            result += if (checkbox_2.isChecked) {
                "1,"
            } else {
                "0,"
            }

            result += if (checkbox_3.isChecked) {
                "1,"
            } else {
                "0,"
            }

            result += if (checkbox_4.isChecked) {
                "1"
            } else {
                "0"
            }
        }

        return result
    }

    private fun enableRadiosForTrueOrCheckboxes(enableRadios: Boolean) {
        if (enableRadios) {
            radio_1.visibility = View.VISIBLE
            radio_2.visibility = View.VISIBLE
            radio_3.visibility = View.VISIBLE
            radio_4.visibility = View.VISIBLE
            checkbox_1.visibility = View.GONE
            checkbox_2.visibility = View.GONE
            checkbox_3.visibility = View.GONE
            checkbox_4.visibility = View.GONE
        } else {
            radio_1.visibility = View.GONE
            radio_2.visibility = View.GONE
            radio_3.visibility = View.GONE
            radio_4.visibility = View.GONE
            checkbox_1.visibility = View.VISIBLE
            checkbox_2.visibility = View.VISIBLE
            checkbox_3.visibility = View.VISIBLE
            checkbox_4.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        showCancelTestAlert()
    }

    private fun showCancelTestAlert() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Warning!")
            .setMessage("Are you sure you want to exit your test?\n\n(You will be able to continue from this question)")
            .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialogInterface, id ->
                mainActivityDelegate.continueWithBack()
            })
            .setNegativeButton(android.R.string.cancel,null)
            .create().show()

    }


    private fun showErrorSelectionDialog() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage("At least one radio button or check box must be selected to save a question!")
            .setPositiveButton(android.R.string.ok,null)
            .create().show()
    }

}