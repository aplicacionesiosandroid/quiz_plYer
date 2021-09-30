package com.phinnovation.quizplayer.framework.dagger

import com.phinnovation.quizplayer.presentation.admin.questioneditor.QuestionEditorFragment
import com.phinnovation.quizplayer.presentation.admin.quizeditor.QuizEditorFragment
import com.phinnovation.quizplayer.presentation.admin.quizlist.AdminQuizListFragment
import com.phinnovation.quizplayer.presentation.tester.intro.TesterQuizIntroFragment
import com.phinnovation.quizplayer.presentation.tester.list.TesterQuizListFragment
import com.phinnovation.quizplayer.presentation.tester.player.TesterPlayerFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelFactoryModule::class,
    InteractorsModule::class,
    RepositoriesModule::class,
])

interface AppComponent {
    fun inject(target: AdminQuizListFragment)
    fun inject(target: QuizEditorFragment)
    fun inject(target: QuestionEditorFragment)
    fun inject(target: TesterQuizIntroFragment)
    fun inject(target: TesterQuizListFragment)
    fun inject(target: TesterPlayerFragment)
}