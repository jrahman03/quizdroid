package edu.uw.ischool.jrahman.quizdroid


import org.json.JSONArray
import org.json.JSONObject

class InMemoryTopicRepository : TopicRepository {
    private var topics: List<MainActivity.Topic> = listOf()

    override fun getTopics(): List<MainActivity.Topic> = topics

    override fun loadTopicsFromJson(jsonString: String) {
        val jsonArray = JSONArray(jsonString)
        topics = List(jsonArray.length()) { i ->
            parseTopic(jsonArray.getJSONObject(i))
        }
    }

    private fun parseTopic(jsonObject: JSONObject): MainActivity.Topic {
        val title = jsonObject.getString("title")
        val desc = jsonObject.getString("desc")
        val questionsArray = jsonObject.getJSONArray("questions")
        val questions = parseQuestions(questionsArray)
        return MainActivity.Topic(title, desc, questions)
    }

    private fun parseQuestions(jsonArray: JSONArray): List<MainActivity.Question> {
        return List(jsonArray.length()) { i ->
            parseQuestion(jsonArray.getJSONObject(i))
        }
    }

    private fun parseQuestion(jsonObject: JSONObject): MainActivity.Question {
        val text = jsonObject.getString("text")
        val answerIndex = jsonObject.getInt("answer") - 1
        val answersArray = jsonObject.getJSONArray("answers")
        val answerOptions = parseAnswerOptions(answersArray)
        return MainActivity.Question(text, answerOptions, answerIndex)
    }


    private fun parseAnswerOptions(jsonArray: JSONArray): List<String> {
        return List(jsonArray.length()) { i ->
            jsonArray.getString(i)
        }
    }
}


