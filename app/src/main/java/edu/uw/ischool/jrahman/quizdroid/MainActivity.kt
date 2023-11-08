package edu.uw.ischool.jrahman.quizdroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    data class Question(val text: String, val options: List<String>, val correctAnswer: Int)
    data class Topic(val name: String, val description: String, val questions: List<Question>)

    private lateinit var topics: List<Topic>

    private var currentTopicIndex: Int = 0
    private var currentQuestionIndex: Int = 0
    private var score: Int = 0

    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicDescription: TextView
    private lateinit var beginButton: Button
    private lateinit var questionText: TextView
    private lateinit var answerOptions: RadioGroup
    private lateinit var submitAnswerButton: Button
    private lateinit var yourAnswerText: TextView
    private lateinit var correctAnswerText: TextView
    private lateinit var scoreText: TextView
    private lateinit var nextOrFinishButton: Button
    private lateinit var topicOverviewLayout: View
    private lateinit var questionPageLayout: View
    private lateinit var answerPageLayout: View

    private lateinit var topicRepository: TopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topicRepository = QuizApp.getInstance().getTopicRepository()
        topics = topicRepository.getTopics()

        topicRecyclerView = findViewById(R.id.topicRecyclerView)
        topicDescription = findViewById(R.id.topicDescription)
        beginButton = findViewById(R.id.beginButton)
        questionText = findViewById(R.id.questionText)
        answerOptions = findViewById(R.id.answerOptions)
        submitAnswerButton = findViewById(R.id.submitAnswerButton)
        yourAnswerText = findViewById(R.id.yourAnswerText)
        correctAnswerText = findViewById(R.id.correctAnswerText)
        scoreText = findViewById(R.id.scoreText)
        nextOrFinishButton = findViewById(R.id.nextOrFinishButton)
        topicOverviewLayout = findViewById(R.id.topicOverviewLayout)
        questionPageLayout = findViewById(R.id.questionPageLayout)
        answerPageLayout = findViewById(R.id.answerPageLayout)
        submitAnswerButton.visibility = View.GONE

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            showTopicSelection()
        }

        topicRecyclerView.layoutManager = LinearLayoutManager(this)
        topicRecyclerView.adapter = TopicAdapter(topics) { topicIndex ->
            currentTopicIndex = topicIndex
            showTopicOverview(topicIndex)
        }

        beginButton.setOnClickListener { showQuestionPage(0) }
        submitAnswerButton.setOnClickListener { evaluateAnswer() }
        answerOptions.setOnCheckedChangeListener { _, checkedId ->
            submitAnswerButton.visibility = if (checkedId != -1) View.VISIBLE else View.GONE
        }
    }

    private fun showTopicSelection() {
        topicRecyclerView.visibility = View.VISIBLE
        topicOverviewLayout.visibility = View.GONE
        questionPageLayout.visibility = View.GONE
        answerPageLayout.visibility = View.GONE
    }

    private fun showTopicOverview(topicIndex: Int) {
        topicDescription.text = topics[topicIndex].description

        topicOverviewLayout.visibility = View.VISIBLE
        questionPageLayout.visibility = View.GONE
        answerPageLayout.visibility = View.GONE
        topicRecyclerView.visibility = View.GONE
    }

    private fun showQuestionPage(questionIndex: Int) {
        currentQuestionIndex = questionIndex

        val question = topics[currentTopicIndex].questions[questionIndex]
        questionText.text = question.text
        answerOptions.removeAllViews()
        question.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this)
            radioButton.text = option
            radioButton.id = index
            answerOptions.addView(radioButton)
        }

        answerOptions.clearCheck()
        submitAnswerButton.visibility = View.GONE

        topicOverviewLayout.visibility = View.GONE
        questionPageLayout.visibility = View.VISIBLE
        answerPageLayout.visibility = View.GONE
    }

    private fun evaluateAnswer() {
        val selectedOptionId = answerOptions.checkedRadioButtonId
        if (selectedOptionId != -1) {
            val correctAnswer = topics[currentTopicIndex].questions[currentQuestionIndex].correctAnswer

            if (selectedOptionId == correctAnswer) {
                score++
            }

            showAnswerPage(selectedOptionId, correctAnswer)
        }
    }

    private fun showAnswerPage(selectedOption: Int, correctAnswer: Int) {
        yourAnswerText.text = "Selected Answer: ${topics[currentTopicIndex].questions[currentQuestionIndex].options[selectedOption]}"
        correctAnswerText.text = "Correct Answer: ${topics[currentTopicIndex].questions[currentQuestionIndex].options[correctAnswer]}"
        scoreText.text = "Credit: $score/${topics[currentTopicIndex].questions.size}"

        nextOrFinishButton.text = if (currentQuestionIndex < topics[currentTopicIndex].questions.size - 1) "Next" else "Finish"
        nextOrFinishButton.setOnClickListener {
            if (currentQuestionIndex < topics[currentTopicIndex].questions.size - 1) {
                showQuestionPage(currentQuestionIndex + 1)
            } else {
                resetQuiz()
            }
        }

        topicOverviewLayout.visibility = View.GONE
        questionPageLayout.visibility = View.GONE
        answerPageLayout.visibility = View.VISIBLE
    }

    private fun resetQuiz() {
        currentTopicIndex = 0
        currentQuestionIndex = 0
        score = 0

        topicOverviewLayout.visibility = View.GONE
        questionPageLayout.visibility = View.GONE
        answerPageLayout.visibility = View.GONE
        topicRecyclerView.visibility = View.VISIBLE
    }

    inner class TopicAdapter(
        private val topics: List<Topic>,
        private val itemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val topicName: TextView = view.findViewById(R.id.topicName)

            init {
                itemView.setOnClickListener {
                    itemClick(adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_topic, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = topics.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val topic = topics[position]
            holder.topicName.text = topic.name
        }
    }
}