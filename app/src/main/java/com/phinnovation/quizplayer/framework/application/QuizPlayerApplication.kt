package com.phinnovation.quizplayer.framework.application

import android.app.Application
import com.phinnovation.quizplayer.framework.dagger.AppComponent
import com.phinnovation.quizplayer.framework.dagger.AppModule
import com.phinnovation.quizplayer.framework.dagger.DaggerAppComponent

class QuizPlayerApplication : Application() {

    lateinit var quizPlayerComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        quizPlayerComponent = initDagger(this)
    }

    private fun initDagger(app: QuizPlayerApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

}