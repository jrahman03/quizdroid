package edu.uw.ischool.jrahman.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {
    companion object {
        private lateinit var instance: QuizApp
        fun getInstance(): QuizApp = instance
    }

    private lateinit var topicRepository: TopicRepository

    override fun onCreate() {
        super.onCreate()
        Log.d("QuizDroid ", "Up and Running")
        instance = this
        topicRepository = InMemoryTopicRepository()
    }

    fun getTopicRepository(): TopicRepository = topicRepository
}

