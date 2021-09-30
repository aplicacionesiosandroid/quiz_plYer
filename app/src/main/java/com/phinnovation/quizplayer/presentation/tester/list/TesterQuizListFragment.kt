package com.phinnovation.quizplayer.presentation.tester.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.presentation.application.QuizPlayerApplication
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import kotlinx.android.synthetic.main.fragment_tester_list.*
import javax.inject.Inject

class TesterQuizListFragment : Fragment() {

    private lateinit var mainActivityDelegate: MainActivityDelegate

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: TesterQuizListViewModel

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
        return inflater.inflate(R.layout.fragment_tester_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, factory)[TesterQuizListViewModel::class.java]

        val adapter = TesterQuizListAdapter() {
            //Adapter click handler
            viewModel.checkQuizCanBeOpened(it)
        }

        quizRecyclerView.adapter = adapter

        viewModel.navigateToQuizEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                mainActivityDelegate.openQuizForPlayIntro()
            }
        })

        viewModel.showEmptyQuestionsErrorEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                showErrorMisconfiguredQuiz()
            }
        })

        viewModel.showQuizFinishedErrorEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                Toast.makeText(context, "You cannot retake a finished quiz!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.quizzes.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                emptyQuizesLabel.visibility = View.GONE
            }
            adapter.update(it)
        })

        viewModel.loadQuizzes()
    }

    private fun showErrorMisconfiguredQuiz() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage("At least one question must be set before testing the Quiz!")
            .setPositiveButton(android.R.string.ok, null)
            .create().show()
    }

}