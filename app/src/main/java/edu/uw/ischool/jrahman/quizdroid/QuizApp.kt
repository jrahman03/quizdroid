package edu.uw.ischool.jrahman.quizdroid

import android.app.Application
import android.content.Context
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
        private const val DEFAULT_JSON_URL = "http://tednewardsandbox.site44.com/questions.json"
    }

    lateinit var topicRepository: TopicRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        topicRepository = InMemoryTopicRepository()
    }

    fun downloadAndUpdateTopics(onComplete: (List<MainActivity.Topic>) -> Unit) {
        val sharedPreferences = getSharedPreferences("QuizAppPreferences", Context.MODE_PRIVATE)
        val finalUrl = sharedPreferences.getString("URL", DEFAULT_JSON_URL) ?: DEFAULT_JSON_URL

        val executor = Executors.newSingleThreadExecutor()
        val mainHandler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                val urlConnection = URL(finalUrl).openConnection() as HttpURLConnection
                val inputStream = urlConnection.inputStream
                val reader = InputStreamReader(inputStream)
                val jsonString = reader.readText()
                reader.close()
                topicRepository.loadTopicsFromJson(jsonString)
                mainHandler.post {
                    Log.d("QuizApp", "Data fetched successfully")
                    onComplete(topicRepository.getTopics())
                    (applicationContext as? MainActivity)?.refreshTopics()
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






