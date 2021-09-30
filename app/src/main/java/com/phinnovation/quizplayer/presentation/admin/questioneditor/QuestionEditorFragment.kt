package com.phinnovation.quizplayer.presentation.admin.questioneditor

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.phinnovation.core.domain.Question
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModel
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.framework.application.QuizPlayerApplication
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import com.phinnovation.quizplayer.presentation.utils.RadioGroupCheckListener
import kotlinx.android.synthetic.main.fragment_question_editor.*
import javax.inject.Inject

class QuestionEditorFragment : Fragment () {

    private lateinit var mainActivityDelegate: MainActivityDelegate

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: QuestionEditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        return inflater.inflate(R.layout.fragment_question_editor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        RadioGroupCheckListener.makeGroup(radio_1, radio_2, radio_3, radio_4)

        viewModel = ViewModelProvider(this, factory)[QuestionEditorViewModel::class.java]

        viewModel.question.observe(viewLifecycleOwner, { question ->

            //set the texts
            questionTitle.setText(question.title)
            questionDescription.setText(question.description)

            answer_1.setText(question.choiceTitle1)
            answer_2.setText(question.choiceTitle2)
            answer_3.setText(question.choiceTitle3)
            answer_4.setText(question.choiceTitle4)

            //continue setting up the radio/check lists
            setupRadiosOrCheckBoxes(question)
        })

        answerTypeRadioGroup.setOnCheckedChangeListener { _, index ->
            enableRadiosForTrueOrCheckboxes(index == R.id.radio_radios) //true if single (radio_radios) was selected
        }

        updateQuestionButton.setOnClickListener {

            var questionType = QuestionType.MULTI_CHOICE

            if (answerTypeRadioGroup.checkedRadioButtonId == R.id.radio_radios) {
                questionType = QuestionType.SINGLE_CHOICE
            }

            val correctAnswer = getCorrectAnswerOrNotSet(questionType)

            if (correctAnswer == "-1" || correctAnswer == "0,0,0,0") {
                showErrorSelectionDialog()
            } else {


                viewModel.updateQuestionAndNavigateUp(
                    questionTitle.text.toString(),
                    questionDescription.text.toString(),
                    questionType,
                    answer_1.text.toString(),
                    answer_2.text.toString(),
                    answer_3.text.toString(),
                    answer_4.text.toString(),
                    correctAnswer)
            }
        }

        viewModel.navigateUpEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                mainActivityDelegate.continueWithBack() ;
            }
        })

        viewModel.getOpenQuestion()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_question_editor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_delete_question -> {
                viewModel.deleteQuestionAndNavigateUp()
                return true;
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupRadiosOrCheckBoxes(question: Question) {
        if (question.type == QuestionType.SINGLE_CHOICE) {
            answerTypeRadioGroup.check(R.id.radio_radios)

            //check for correct answer:
            var selectedRadio = 0
            var strings = question.correctAnswer.split(",")

            if (strings.size == 1) {
                selectedRadio = try {
                    strings[0].toInt()
                } catch ( e:NumberFormatException) {
                    -1
                }
            }

            when (selectedRadio) {
                0 -> radio_1.isChecked = true
                1 -> radio_2.isChecked = true
                2 -> radio_3.isChecked = true
                3 -> radio_4.isChecked = true
            }

        } else {
            answerTypeRadioGroup.check(R.id.radio_checkboxes)

            var check1 = false
            var check2 = false
            var check3 = false
            var check4 = false

            var strings = question.correctAnswer.split(",")

            if (strings.size == 4) {
                check1 = strings[0] == "1"
                check2 = strings[1] == "1"
                check3 = strings[2] == "1"
                check4 = strings[3] == "1"
            }

            checkbox_1.isChecked = check1
            checkbox_2.isChecked = check2
            checkbox_3.isChecked = check3
            checkbox_4.isChecked = check4
        }
    }

    /**
     * Returns either:
     * 1.   radio selection case: a string with a positive (including zero) int value or -1 if not set
     * 2.   check selection case: a coma separated string containing the 4 checks or 0,0,0,0 if not set
     * */
    private fun getCorrectAnswerOrNotSet(questionType:QuestionType): String {

        var result:String = "" ;

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

    private fun showErrorSelectionDialog() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage("At least one radio button or check box must be selected to save a question!")
            .setPositiveButton(android.R.string.ok,null)
            .create().show()

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

}