package edu.uw.ischool.jrahman.quizdroid


class InMemoryTopicRepository : TopicRepository {
    private val topics = listOf(
        MainActivity.Topic("Math", "A quiz about mathematical concepts", listOf(
            MainActivity.Question("What's 2+2?", listOf("3", "4", "5", "6"), 1),
            MainActivity.Question("What's 20/2?", listOf("35", "32", "10", "61"), 2),
            MainActivity.Question("What's 10-8?", listOf("2", "90", "6", "8"), 0),
        )),
        MainActivity.Topic("Physics", "A quiz about Newton's Laws", listOf(
            MainActivity.Question("What's Newton's 3rd Law?", listOf("Freedom of Speech", "Right to Remain Silent", "For every action (force) in nature there is an equal and opposite reaction", "IDK"), 2),
            MainActivity.Question("Kepler’s First Law of Planetary Motion is known as?", listOf("Laws of areas", "Law of elliptical orbits", "Harmonic laws", "Hmm.. Couldn't Tell You"), 1),
            MainActivity.Question("A properly cut diamond appears bright because of", listOf("Total volume", "Refraction", "Its natural color", "Total reflection"), 3),
        )),
        MainActivity.Topic("Marvel Superheros", "A quiz about Marvel Superhero's", listOf(
            MainActivity.Question("Which actor plays Iron Man in the MCU?", listOf("Christan Bale", "Ben Afleck ", "Robert Downey Jr", "Dwayne Johnson"), 2),
            MainActivity.Question("What comes with Great Power?", listOf("Great Responsibility", "Money", "Good Grades", "Unlimited McChickens"), 0),
            MainActivity.Question("What is Captain America's Shield Made of?", listOf("Titanium", "Iron ", "Vibranium", "The Time Stone"), 2),
        )),
    )

    override fun getTopics(): List<MainActivity.Topic> {
        return topics
    }
}
