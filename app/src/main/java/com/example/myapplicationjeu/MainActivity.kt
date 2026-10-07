package com.example.myapplicationjeu

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationjeu.databinding.ActivityMainBinding

/**
 * Activité principale gérant le déroulement du Quiz en Images.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Tableau statique de 5 questions illustrées
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

    // Variables d'état
    private var currentQuestionIndex = 0
    private var score = 0
    private var hasAnswered = false
    private var selectedChoiceIndex = -1

    companion object {
        private const val KEY_CURRENT_INDEX = "KEY_CURRENT_INDEX"
        private const val KEY_SCORE = "KEY_SCORE"
        private const val KEY_HAS_ANSWERED = "KEY_HAS_ANSWERED"
        private const val KEY_SELECTED_CHOICE = "KEY_SELECTED_CHOICE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restauration de l'état si rotation de l'écran
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0)
            score = savedInstanceState.getInt(KEY_SCORE, 0)
            hasAnswered = savedInstanceState.getBoolean(KEY_HAS_ANSWERED, false)
            selectedChoiceIndex = savedInstanceState.getInt(KEY_SELECTED_CHOICE, -1)
        }

        setupListeners()

        if (currentQuestionIndex >= questions.size) {
            displayFinalResult()
        } else {
            displayCurrentQuestion()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, currentQuestionIndex)
        outState.putInt(KEY_SCORE, score)
        outState.putBoolean(KEY_HAS_ANSWERED, hasAnswered)
        outState.putInt(KEY_SELECTED_CHOICE, selectedChoiceIndex)
    }

    /**
     * Associe les gestionnaires de clic aux différents boutons de l'interface.
     */
    private fun setupListeners() {
        binding.btnChoix1.setOnClickListener { onChoiceSelected(0) }
        binding.btnChoix2.setOnClickListener { onChoiceSelected(1) }
        binding.btnChoix3.setOnClickListener { onChoiceSelected(2) }

        binding.btnNext.setOnClickListener {
            goToNextQuestion()
        }

        binding.btnReplay.setOnClickListener {
            resetQuiz()
        }
    }

    /**
     * Affiche la question courante et initialise l'état des composants UI.
     */
    private fun displayCurrentQuestion() {
        val question = questions[currentQuestionIndex]

        // Masquer les résultats et afficher le conteneur de question
        binding.layoutResult.visibility = View.GONE
        binding.layoutQuestionContainer.visibility = View.VISIBLE

        // Mettre à jour le score et la progression
        updateScoreAndProgressDisplay()

        // Numéro de question
        binding.txtQuestionNumber.text = getString(
            R.string.question_number_format,
            currentQuestionIndex + 1,
            questions.size
        )

        // Image & description d'accessibilité
        binding.imgQuestion.setImageResource(question.imageResId)
        binding.imgQuestion.contentDescription = question.imageDescription

        // Libellé de la question
        binding.txtQuestion.text = question.questionText

        // Choix
        binding.btnChoix1.text = question.choices[0]
        binding.btnChoix2.text = question.choices[1]
        binding.btnChoix3.text = question.choices[2]

        // Réinitialiser les boutons de choix
        resetChoiceButtonsStyle()

        if (hasAnswered && selectedChoiceIndex != -1) {
            // Si la question a déjà été répondue (ex: après rotation)
            applyAnsweredState(selectedChoiceIndex)
        } else {
            // État initial non répondu
            enableChoiceButtons(true)
            binding.txtFeedback.visibility = View.GONE
            binding.btnNext.isEnabled = false
            updateNextButtonText()
        }
    }

    /**
     * Gère la sélection d'une réponse par l'utilisateur.
     */
    private fun onChoiceSelected(choiceIndex: Int) {
        if (hasAnswered) return // Empêche de répondre plusieurs fois

        hasAnswered = true
        selectedChoiceIndex = choiceIndex

        val question = questions[currentQuestionIndex]
        val isCorrect = (choiceIndex == question.correctAnswerIndex)

        if (isCorrect) {
            score++
        }

        // Mettre à jour le score affiché et la progression
        updateScoreAndProgressDisplay()

        // Appliquer l'état visuel après réponse
        applyAnsweredState(choiceIndex)
    }

    /**
     * Applique la désactivation des choix, l'affichage de la correction et l'activation de la navigation.
     */
    private fun applyAnsweredState(choiceIndex: Int) {
        val question = questions[currentQuestionIndex]
        val isCorrect = (choiceIndex == question.correctAnswerIndex)

        // Désactiver les 3 boutons de choix
        enableChoiceButtons(false)

        // Styliser le bouton cliqué
        val choiceButtons = listOf(binding.btnChoix1, binding.btnChoix2, binding.btnChoix3)
        
        if (isCorrect) {
            choiceButtons[choiceIndex].setBackgroundColor(getColor(R.color.correct_green))
            choiceButtons[choiceIndex].setTextColor(Color.WHITE)

            binding.txtFeedback.text = getString(R.string.feedback_correct)
            binding.txtFeedback.setTextColor(getColor(R.color.correct_green))
            binding.txtFeedback.setBackgroundColor(getColor(R.color.feedback_bg_correct))
        } else {
            choiceButtons[choiceIndex].setBackgroundColor(getColor(R.color.wrong_red))
            choiceButtons[choiceIndex].setTextColor(Color.WHITE)

            // Mettre en évidence la bonne réponse
            choiceButtons[question.correctAnswerIndex].setBackgroundColor(getColor(R.color.correct_green))
            choiceButtons[question.correctAnswerIndex].setTextColor(Color.WHITE)

            binding.txtFeedback.text = getString(
                R.string.feedback_wrong_format,
                question.choices[question.correctAnswerIndex]
            )
            binding.txtFeedback.setTextColor(getColor(R.color.wrong_red))
            binding.txtFeedback.setBackgroundColor(getColor(R.color.feedback_bg_wrong))
        }

        binding.txtFeedback.visibility = View.VISIBLE

        // Activer le bouton suivant
        binding.btnNext.isEnabled = true
        updateNextButtonText()
    }

    /**
     * Met à jour le texte du bouton de navigation ("Question suivante" ou "Voir le résultat").
     */
    private fun updateNextButtonText() {
        if (currentQuestionIndex == questions.size - 1) {
            binding.btnNext.text = getString(R.string.btn_see_result)
        } else {
            binding.btnNext.text = getString(R.string.btn_next_question)
        }
    }

    /**
     * Active ou désactive les 3 boutons de choix.
     */
    private fun enableChoiceButtons(enabled: Boolean) {
        binding.btnChoix1.isEnabled = enabled
        binding.btnChoix2.isEnabled = enabled
        binding.btnChoix3.isEnabled = enabled
    }

    /**
     * Réinitialise le style par défaut des boutons de choix.
     */
    private fun resetChoiceButtonsStyle() {
        val choiceButtons = listOf(binding.btnChoix1, binding.btnChoix2, binding.btnChoix3)
        for (btn in choiceButtons) {
            btn.setBackgroundColor(Color.TRANSPARENT)
            btn.setTextColor(getColor(R.color.primary))
        }
    }

    /**
     * Met à jour le bandeau de score et la barre de progression.
     */
    private fun updateScoreAndProgressDisplay() {
        binding.txtScore.text = getString(R.string.score_format, score, questions.size)

        // Progression = nombre de réponses données (0 à 5)
        val progress = if (hasAnswered) currentQuestionIndex + 1 else currentQuestionIndex
        binding.progressQuiz.progress = progress
    }

    /**
     * Passe à la question suivante ou affiche l'écran de fin.
     */
    private fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            hasAnswered = false
            selectedChoiceIndex = -1
            displayCurrentQuestion()
        } else {
            displayFinalResult()
        }
    }

    /**
     * Affiche l'écran de résultat final.
     */
    private fun displayFinalResult() {
        binding.layoutQuestionContainer.visibility = View.GONE
        binding.layoutResult.visibility = View.VISIBLE

        binding.txtScore.text = getString(R.string.score_format, score, questions.size)
        binding.progressQuiz.progress = questions.size

        binding.txtFinalScore.text = getString(R.string.final_score_format, score, questions.size)

        val appreciation = when (score) {
            5 -> getString(R.string.result_excellent)
            3, 4 -> getString(R.string.result_good)
            1, 2 -> getString(R.string.result_average)
            else -> getString(R.string.result_poor)
        }
        binding.txtFinalAppreciation.text = appreciation
    }

    /**
     * Réinitialise entièrement le quiz.
     */
    private fun resetQuiz() {
        currentQuestionIndex = 0
        score = 0
        hasAnswered = false
        selectedChoiceIndex = -1
        displayCurrentQuestion()
    }
}