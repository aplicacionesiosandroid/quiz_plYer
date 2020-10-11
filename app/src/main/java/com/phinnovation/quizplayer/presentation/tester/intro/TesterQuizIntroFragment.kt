package com.phinnovation.quizplayer.presentation.tester.intro

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.phinnovation.core.domain.QuizState
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.framework.QuizPlayerViewModelFactory
import com.phinnovation.quizplayer.presentation.MainActivityDelegate
import kotlinx.android.synthetic.main.fragment_tester_intro.*

class TesterQuizIntroFragment : Fragment() {

    private lateinit var mainActivityDelegate: MainActivityDelegate

    private lateinit var viewModel: TesterQuizIntroViewModel

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
        return inflater.inflate(R.layout.fragment_tester_intro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, QuizPlayerViewModelFactory)
            .get(TesterQuizIntroViewModel::class.java)

        viewModel.quiz.observe(viewLifecycleOwner, Observer {
            quizTitle.setText(it.title)
            quizDescription.setText(it.description)

            if (it.state != QuizState.NOT_STARTED) {
                startQuizButton.setText(R.string.continue_quiz)
                continueQuizText.visibility = View.VISIBLE
                continueQuizText.setText(getString(R.string.continue_quiz_from,it.lastSeenQuestion+1))
            }
        })

        viewModel.getOpenQuiz()

        startQuizButton.setOnClickListener {
            viewModel.updateQuizStatusToStarted()
        }

        viewModel.quizUpdated.observe(viewLifecycleOwner, Observer {
            mainActivityDelegate.openQuizForPlayTest()
        })

    }
}