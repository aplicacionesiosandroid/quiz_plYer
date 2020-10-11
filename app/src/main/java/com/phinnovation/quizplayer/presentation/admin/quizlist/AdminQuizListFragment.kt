package com.phinnovation.quizplayer.presentation.admin.quizlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import kotlinx.android.synthetic.main.fragment_admin_list.*

class AdminQuizListFragment : Fragment () {

    private lateinit var mainActivityDelegate: MainActivityDelegate

    private lateinit var viewModel: AdminQuizListViewModel

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

        viewModel = ViewModelProviders.of(this, QuizPlayerViewModelFactory)
            .get(AdminQuizListViewModel::class.java)


        val adapter = AdminQuizListAdapter() {

            //Adapter click handler
            viewModel.setOpenQuiz(it)
            mainActivityDelegate.openQuizForEditing(it)
        }
        quizRecyclerView.adapter = adapter


        viewModel.quizzes.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                emptyQuizesLabel.visibility = View.GONE
            }
            adapter.update(it) }
        )
        viewModel.loadQuizzes()

        fab.setOnClickListener {
            viewModel.addQuiz()
//            startActivityForResult(createOpenIntent(), READ_REQUEST_CODE)
        }
    }

}