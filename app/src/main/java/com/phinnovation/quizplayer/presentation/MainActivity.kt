package com.phinnovation.quizplayer.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.phinnovation.core.domain.Question
import com.phinnovation.quizplayer.R
import com.phinnovation.quizplayer.presentation.utils.FragmentReceiveOnBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityDelegate {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = this.findNavController(R.id.navHostFragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.adminListFragment,
                R.id.testerListFragment
            ),//, R.id.rulesFragment, R.id.aboutFragment ADD MORE WHEN READY
            drawer_layout
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(nav_view, navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

    }

    override fun onBackPressed() {
        if (!checkFragmentsForHasReceiver()) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!checkFragmentsForHasReceiver()) {
            return NavigationUI.navigateUp(navController, appBarConfiguration)
        } else {
            return false
        }

    }

    override fun continueWithBack() {
        NavigationUI.navigateUp(navController, appBarConfiguration)
    }


    override fun openQuizForEditing() {
        navController.navigate(R.id.action_adminListFragment_to_quizEditorFragment)
    }

    override fun openQuestionForEditing(question: Question) {

        Log.d("MAin activity", "openQuizForEditing called: $question")

        navController.navigate(R.id.action_quizEditorFragment_to_questionEditorFragment)
    }

    override fun openQuizForPlayIntro() {
        Log.d("MAin activity", "openQuizForPlayIntro called")

        navController.navigate(R.id.action_testerListFragment_to_testerQuizIntroFragment)
    }

    override fun openQuizForPlayTest() {
        Log.d("MAin activity", "openQuizForPlayerTest called")

        navController.navigate(R.id.action_testerQuizIntroFragment_to_testerPlayerFragment)
    }

    override fun continueQuizShowNextQuestion() {
        Log.d("MAin activity", "continueQuizShowNextQuestion called")

        navController.navigate(R.id.action_testerPlayerFragment_self)
    }

    private fun checkFragmentsForHasReceiver() :Boolean {
        var fragments: List<Fragment> = supportFragmentManager.fragments

        if (fragments.size ==1 && fragments[0] is NavHostFragment) {
            fragments = (fragments[0] as NavHostFragment).childFragmentManager.fragments
        }

        for (f in fragments) {
            if (f != null && f is FragmentReceiveOnBack) {
                (f as FragmentReceiveOnBack).onBackPressed()
                return true ;
            }
        }

        return false ;
    }

}

//override fun onNavigationItemSelected(item: MenuItem): Boolean {
//    Log.d("MainAtivity", "onNavigationItemSelected: $item")
//    return false
//}

/*
        val quizDataSource = RoomQuizDataSource(applicationContext)

        var job: Job = Job()
        var scopeIO = CoroutineScope(Dispatchers.IO + job)

        scopeIO.launch {

            var quizes = quizDataSource.readAll()

//            for (quiz in quizes) {
//                quizDataSource.remove(quiz)
//            }
//            quizDataSource.add(Quiz(0,"test","desc",QuizState.NOT_STARTED,0))
//            quizDataSource.add(Quiz(0,"test","desc",QuizState.NOT_STARTED,0))
//            quizDataSource.add(Quiz(0,"test","desc",QuizState.NOT_STARTED,0))
        }
* */