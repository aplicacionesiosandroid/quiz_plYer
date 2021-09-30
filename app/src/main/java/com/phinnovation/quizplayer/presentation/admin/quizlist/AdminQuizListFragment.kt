package com.phinnovation.quizplayer.presentation.admin.quizlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.presentation.application.QuizPlayerApplication
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import kotlinx.android.synthetic.main.fragment_admin_list.*
import javax.inject.Inject

class AdminQuizListFragment : Fragment() {

    private lateinit var mainActivityDelegate: MainActivityDelegate

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: AdminQuizListViewModel

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

        return inflater.inflate(R.layout.fragment_admin_list, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, factory)[AdminQuizListViewModel::class.java]

        val adapter = AdminQuizListAdapter() {
            //Adapter click handler
            viewModel.setOpenQuizAndNavigate(it)
        }
        quizRecyclerView.adapter = adapter

        viewModel.quizzes.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                emptyQuizesLabel.visibility = View.GONE
            } else {
                emptyQuizesLabel.visibility = View.VISIBLE
            }
            adapter.update(it)
        })

        viewModel.navigateToQuizEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
                mainActivityDelegate.openQuizForEditing()
            }
        })

        fab.setOnClickListener {
            viewModel.addQuiz()
        }

        viewModel.loadQuizzes()

    }

}