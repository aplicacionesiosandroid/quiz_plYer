package com.phinnovation.quizplayer.presentation.admin.quizeditor

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import kotlinx.android.synthetic.main.fragment_quiz_editor.*

class QuizEditorFragment : Fragment() {

//    companion object {
//
//        fun newInstance(quiz: Quiz) = QuizEditorFragment().apply {
////            arguments = ReaderViewModel.createArguments(document)
//        }
//    }

    private lateinit var mainActivityDelegate: MainActivityDelegate

    private lateinit var viewModel: QuizEditorViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_editor, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, QuizPlayerViewModelFactory)
            .get(QuizEditorViewModel::class.java)

        val adapter = QuizEditorQuestionListAdapter() {
            //Adapter click handler
            viewModel.updateQuizPrepareToOpenQuestion(quizTitle.text.toString(), quizDescription.text.toString(), it)
        }
        questionsRecyclerView.adapter = adapter

        viewModel.quiz.observe(viewLifecycleOwner, Observer {
            quizTitle.setText(it.title)
            quizDescription.setText(it.description)
        })

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                emptyQuestionsLabel.visibility = View.GONE
            } else {
                emptyQuestionsLabel.visibility = View.VISIBLE
            }

            adapter.update(it)
        })

        updateButton.setOnClickListener {
            viewModel.updateQuizAndClose(quizTitle.text.toString(), quizDescription.text.toString())
        }

        addQuestion.setOnClickListener {
            viewModel.addQuestion();
        }

        viewModel.navigateUpEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                mainActivityDelegate.continueWithBack()
            }
        })

        viewModel.navigateToQuestionEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                mainActivityDelegate.openQuestionForEditing()
            }
        })

        viewModel.getOpenQuizAndLoadQuestions()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_quiz_editor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_delete_quiz -> {
                viewModel.deleteQuizAndClose()
                true;
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


}