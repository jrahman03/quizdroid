package edu.uw.ischool.jrahman.quizdroid

import android.app.Application
import android.os.Handler
import android.os.Looper
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import android.util.Log

class QuizApp : Application() {
    companion object {
        lateinit var instance: QuizApp
            private set
    }

    lateinit var topicRepository: TopicRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        topicRepository = InMemoryTopicRepository()
    }

    fun downloadAndUpdateTopics(url: String, onComplete: (List<MainActivity.Topic>) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        val mainHandler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                val inputStream = urlConnection.inputStream
                val reader = InputStreamReader(inputStream)
                val jsonString = reader.readText()
                reader.close()
                topicRepository.loadTopicsFromJson(jsonString)
                mainHandler.post {
                    Log.d("QuizApp", "Data fetched successfully")
                    onComplete(topicRepository.getTopics())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mainHandler.post {
                    Log.e("QuizApp", "Error fetching data: ${e.message}")
                    onComplete(emptyList())
                }
            }
        }
    }
}






