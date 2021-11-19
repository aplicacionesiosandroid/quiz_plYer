package com.phinnovation.quizplayer.presentation.screens.tester.player

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.presentation.application.QuizPlayerApplication
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import com.phinnovation.quizplayer.presentation.utils.FragmentReceiveOnBack
import com.phinnovation.quizplayer.presentation.utils.RadioGroupCheckListener
import kotlinx.android.synthetic.main.fragment_tester_player.*
import javax.inject.Inject

class TesterPlayerFragment : Fragment(), FragmentReceiveOnBack {

    private lateinit var viewModel: TesterPlayerViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var mainActivityDelegate: MainActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as QuizPlayerApplication).quizPlayerComponent.inject(this)
    }

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

        RadioGroupCheckListener.makeGroup(radio_1, radio_2, radio_3, radio_4)

        viewModel = ViewModelProvider(this, factory)[TesterPlayerViewModel::class.java]

        viewModel.currentQuestionItsIndexAndMaxQuestionsTriple.observe(viewLifecycleOwner, {

            val question = it.first
            val currentQuestionHumanReadable = it.second
            val maxQuestions = it.third

            quizQuestionNumber.text =
                getString(R.string.question_num_out_of, currentQuestionHumanReadable, maxQuestions)
            quizTitle.text = question.title
            quizDescription.text = question.description

            answer_1.text = question.choiceTitle1
            answer_2.text = question.choiceTitle2
            answer_3.text = question.choiceTitle3
            answer_4.text = question.choiceTitle4

            enableRadiosForTrueOrCheckboxes(question.type == QuestionType.SINGLE_CHOICE)
        })

        viewModel.getCurrentQuestionToPlay()

        viewModel.answerCheckedHasMoreQuestions.observe(viewLifecycleOwner, { hasMore ->
            if (hasMore) {
                nextQuestionButton.text = getString(R.string.next_question)
            } else {
                nextQuestionButton.text = getString(R.string.test_ended)
            }
        })

        viewModel.answerIsCorrect.observe(viewLifecycleOwner, { wasCorrect ->
            answerText.visibility = View.VISIBLE

            if (wasCorrect) {
                answerText.setTextColor(Color.GREEN)
                answerText.text = getString(R.string.answer_is_correct)
            } else {
                answerText.setTextColor(Color.RED)
                answerText.text = getString(R.string.answer_is_wrong)
            }
        })

        viewModel.showNextOrFinishEvent.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { hasMore ->
                if (hasMore) {
                    mainActivityDelegate.continueQuizShowNextQuestion()
                } else {
                    mainActivityDelegate.continueWithBack()
                }
            }
        })

        nextQuestionButton.setOnClickListener {

            viewModel.answerCheckedHasMoreQuestions.value?.let {

                //if the answer is already checked, move on
                viewModel.updateQuizAnsweredQuestionAndContinue()

            } ?: run {

                //else send the answer for checking

                var questionType = QuestionType.MULTI_CHOICE

                //we could get this from the model, or the observable vars but is as simple as this:
                if (radio_1.isVisible) {
                    questionType = QuestionType.SINGLE_CHOICE
                }

                val answer = getUserAnswerFromUI(questionType)

                if (answer == "-1" || answer == "0,0,0,0") { //not selected answer
                    showErrorSelectionDialog()
                } else {
                    //increment the quiz.lastSeenQuestion and show continue or end button
                    viewModel.checkQuestionAndShowNextOrFinish(answer)
                }
            }
        }
    }

    override fun onBackPressed() {
        showCancelTestAlert()
    }

    private fun getUserAnswerFromUI(questionType: QuestionType): String {

        var result: String

        if (questionType == QuestionType.SINGLE_CHOICE) {

            var selectedIndex = -1

            if (radio_1.isChecked)
                selectedIndex = 0

            if (radio_2.isChecked)
                selectedIndex = 1

            if (radio_3.isChecked)
                selectedIndex = 2

            if (radio_4.isChecked)
                selectedIndex = 3

            result = "$selectedIndex"
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


    private fun showCancelTestAlert() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Warning!") 
            .setMessage("Are you sure you want to exit your test?\n\n(You will be able to continue from this question)")
            .setPositiveButton(android.R.string.ok) { _, _ ->

                viewModel.answerIsCorrect.value?.let {
                    viewModel.updateQuizAnsweredQuestionAndNavigateUp()
                } ?: run {
                    mainActivityDelegate.continueWithBack()
                }
                //check if the user has answered the question, if yes save and return

//                else //elsejust return
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create().show()

    }


    private fun showErrorSelectionDialog() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage("At least one radio button or check box must be selected to save a question!")
            .setPositiveButton(android.R.string.ok, null)
            .create().show()
    }

}