package com.example.myapplicationjeu

import androidx.annotation.DrawableRes

/**
 * Data class représentant une question du quiz en images.
 *
 * @property questionText Texte de la question
 * @property imageResId Identifiant de la ressource drawable pour l'image
 * @property choices Liste des 3 choix de réponses possibles
 * @property correctAnswerIndex Indice (0, 1 ou 2) du choix correct
 * @property imageDescription Description textuelle de l'image pour l'accessibilité
 */
data class Question(
    val questionText: String,
    @DrawableRes val imageResId: Int,
    val choices: List<String>,
    val correctAnswerIndex: Int,
    val imageDescription: String
)