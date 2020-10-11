package com.phinnovation.quizplayer.presentation.admin.questioneditor

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.phinnovation.core.domain.QuestionType
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.presentation.utils.RadioGroupCheckListener
import kotlinx.android.synthetic.main.fragment_question_editor.*

class QuestionEditorFragment : Fragment () {

    private lateinit var viewModel: QuestionEditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_question_editor, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true) ;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, QuizPlayerViewModelFactory)
            .get(QuestionEditorViewModel::class.java)

        viewModel.question.observe(viewLifecycleOwner, Observer { question ->

            RadioGroupCheckListener.makeGroup(radio_1, radio_2, radio_3, radio_4);

            //question is here setup
            questionTitle.setText(question.title)
            questionDescription.setText(question.description)

            answer_1.setText(question.choiceTitle1)
            answer_2.setText(question.choiceTitle2)
            answer_3.setText(question.choiceTitle3)
            answer_4.setText(question.choiceTitle4)

            if (question.type == QuestionType.SINGLE_CHOICE) {
                answerTypeRadioGroup.check(R.id.radio_radios)

                //check for correct answer:
                var selectedRadio = 0;
                var strings = question.correctAnswer.split(",")

                if (strings.size == 1) {
                    selectedRadio = strings[0].toInt()
                }

                when (selectedRadio) {
                    0 -> radio_1.isChecked = true
                    1 -> radio_2.isChecked = true
                    2 -> radio_3.isChecked = true
                    else -> radio_4.isChecked = true
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
        })

        answerTypeRadioGroup.setOnCheckedChangeListener { radioGroup, index ->
            enableRadiosForTrueOrCheckboxes(index == R.id.radio_radios) //true if single (radio_radios) was selected
        }

        viewModel.questionDeleted.observe(viewLifecycleOwner, Observer {
            this.findNavController().navigateUp()
        })

        updateQuestionButton.setOnClickListener {

            var questionType:QuestionType ;
            var correctAnswer:String

            if (answerTypeRadioGroup.checkedRadioButtonId == R.id.radio_radios) {
                questionType = QuestionType.SINGLE_CHOICE

                var selectedIndex = -1

                if (radio_1.isChecked)
                    selectedIndex = 0

                if (radio_2.isChecked)
                    selectedIndex = 1

                if (radio_3.isChecked)
                    selectedIndex = 2

                if (radio_4.isChecked)
                    selectedIndex = 3

                if (selectedIndex == -1) //nothing selected
                {
                    showErrorSelectionDialog()
                    return@setOnClickListener
                }

                correctAnswer = "$selectedIndex"
            } else {
                questionType = QuestionType.MULTI_CHOICE

                correctAnswer = "" ;

                correctAnswer = if (checkbox_1.isChecked) {
                    "1,"
                } else {
                    "0,"
                }

                correctAnswer += if (checkbox_2.isChecked) {
                    "1,"
                } else {
                    "0,"
                }

                correctAnswer += if (checkbox_3.isChecked) {
                    "1,"
                } else {
                    "0,"
                }

                correctAnswer += if (checkbox_4.isChecked) {
                    "1"
                } else {
                    "0"
                }

                if (correctAnswer == "0,0,0,0") //nothing selected
                {
                    showErrorSelectionDialog()
                    return@setOnClickListener
                }

            }

            viewModel.questionUpdated.observe(viewLifecycleOwner, Observer {
                this.findNavController().navigateUp()
                viewModel.questionUpdated.removeObservers(viewLifecycleOwner)
            })

            viewModel.updateQuestion(
                questionTitle.text.toString(),
                questionDescription.text.toString(),
                questionType,
                answer_1.text.toString(),
                answer_2.text.toString(),
                answer_3.text.toString(),
                answer_4.text.toString(),
                correctAnswer
            )

        }

        viewModel.getOpenQuestion()

    }

    private fun showErrorSelectionDialog() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage("At least one radio button or check box must be selected to save a question!")
            .setPositiveButton(android.R.string.ok,null)
            .create().show()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_question_editor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_delete_question -> {
                viewModel.deleteQuestion()
                return true;
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
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