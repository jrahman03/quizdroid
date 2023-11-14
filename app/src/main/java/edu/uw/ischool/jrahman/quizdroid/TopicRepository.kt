package edu.uw.ischool.jrahman.quizdroid

interface TopicRepository {
    fun getTopics(): List<MainActivity.Topic>
    fun loadTopicsFromJson(jsonString: String)
}
