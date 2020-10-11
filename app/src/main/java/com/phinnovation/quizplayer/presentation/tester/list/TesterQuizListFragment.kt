package com.phinnovation.quizplayer.presentation.tester.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import kotlinx.android.synthetic.main.fragment_tester_list.*

class TesterQuizListFragment : Fragment () {

    private lateinit var mainActivityDelegate: MainActivityDelegate

    private lateinit var viewModel: TesterQuizListViewModel

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

        viewModel = ViewModelProviders.of(this, QuizPlayerViewModelFactory)
            .get(TesterQuizListViewModel::class.java)

        val adapter = TesterQuizListAdapter() {

            //Adapter click handler
            viewModel.setOpenQuiz(it)

            if (it.state == QuizState.FINISHED) {
                Toast.makeText(context,"You cannot retake a finished quiz!",Toast.LENGTH_SHORT).show()
            } else {
                viewModel.checkIfQuizHasQuestionsAndThusCanBeOpened(it)
            }
        }

        viewModel.hasQuestionsCanBeOpened.observe(viewLifecycleOwner, Observer {
            if (it==null) {
                Log.d("TesterQuizFragment", "!!!!!!!!!!!!!!!!!!!Skipping null")
                return@Observer
            }

            if (it) {
                viewModel.hasQuestionsCanBeOpened.value = null
                mainActivityDelegate.openQuizForPlayIntro()
            } else {
                showErrorMisconfiguredQuiz()
            }
        })

        quizRecyclerView.adapter = adapter


        viewModel.quizzes.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                emptyQuizesLabel.visibility = View.GONE
            }
            adapter.update(it) }
        )
        viewModel.loadQuizzes()

    }

    private fun showErrorMisconfiguredQuiz() {
        return AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage("At least one question must be set before testing the Quiz!")
            .setPositiveButton(android.R.string.ok,null)
            .create().show()
    }

}