package com.example.myapplicationjeu

import org.junit.Assert.*
import org.junit.Test

class QuizUnitTest {

    private val questions = arrayOf(
        Question(
            questionText = "Quelle est la forme géométrique affichée ci-dessous ?",
            imageResId = R.drawable.triangle,
            choices = listOf("Triangle", "Carré", "Cercle"),
            correctAnswerIndex = 0,
            imageDescription = "Un triangle bleu illustré sur fond clair"
        ),
        Question(
            questionText = "Identifiez la forme géométrique représentée sur cette image.",
            imageResId = R.drawable.carre,
            choices = listOf("Cercle", "Carré", "Étoile"),
            correctAnswerIndex = 1,
            imageDescription = "Un carré vert illustré sur fond clair"
        ),
        Question(
            questionText = "Quelle forme géométrique est montrée dans cette illustration ?",
            imageResId = R.drawable.cercle,
            choices = listOf("Étoile", "Rectangle", "Cercle"),
            correctAnswerIndex = 2,
            imageDescription = "Un cercle orange illustré sur fond clair"
        ),
        Question(
            questionText = "Parmi les propositions, quelle est cette forme géométrique ?",
            imageResId = R.drawable.etoile,
            choices = listOf("Étoile", "Triangle", "Rectangle"),
            correctAnswerIndex = 0,
            imageDescription = "Une étoile jaune illustrée sur fond clair"
        ),
        Question(
            questionText = "Observez l'image : quelle est cette forme géométrique ?",
            imageResId = R.drawable.rectangle,
            choices = listOf("Carré", "Rectangle", "Triangle"),
            correctAnswerIndex = 1,
            imageDescription = "Un rectangle turquoise illustré sur fond clair"
        )
    )

    @Test
    fun testQuestionsCount() {
        assertEquals(5, questions.size)
    }

    @Test
    fun testQuestionsValidity() {
        for (q in questions) {
            assertTrue(q.questionText.isNotBlank())
            assertEquals(3, q.choices.size)
            assertTrue(q.correctAnswerIndex in 0..2)
            assertTrue(q.imageDescription.isNotBlank())
        }
    }

    @Test
    fun testScoreCalculationPerfectScore() {
        var score = 0
        val userAnswers = listOf(0, 1, 2, 0, 1) // All correct
        for (i in questions.indices) {
            if (userAnswers[i] == questions[i].correctAnswerIndex) {
                score++
            }
        }
        assertEquals(5, score)
    }

    @Test
    fun testScoreCalculationMixedScore() {
        var score = 0
        val userAnswers = listOf(0, 0, 2, 1, 1) // Correct for indices 0, 2, 4 (3 points)
        for (i in questions.indices) {
            if (userAnswers[i] == questions[i].correctAnswerIndex) {
                score++
            }
        }
        assertEquals(3, score)
    }
}