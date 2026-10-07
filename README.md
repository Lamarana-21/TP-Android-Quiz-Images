# TP Android - Quiz en Images (Formes Géométriques)

## 📌 Présentation du Projet
Cette application Android est un jeu de quiz illustré composé de 5 questions sur le thème des formes géométriques (*Triangle, Carré, Cercle, Étoile, Rectangle*). Pour chaque question, l'utilisateur observe une image vectorielle, choisit une réponse parmi trois propositions, et reçoit immédiatement un retour visuel.

### ✨ Fonctionnalités & Spécifications
- **Architecture & Frameworks** : Développé 100% en Kotlin et XML avec `ViewBinding`, sans Jetpack Compose ni fragments, au sein d'une seule activité (`MainActivity`).
- **Images Vectorielles (XML)** : 5 ressources vectorielles personnalisées dans `res/drawable` (`triangle.xml`, `carre.xml`, `cercle.xml`, `etoile.xml`, `rectangle.xml`).
- **Score et Progression** :
  - Un score actualisé en haut à droite (ex: `Score : 3 / 5`).
  - Une `ProgressBar` horizontale qui mesure le nombre de questions répondues.
- **Interactions Sécurisées** :
  - Désactivation immédiate des boutons après la validation d'un choix pour éviter les réponses multiples.
  - Activation du bouton de navigation uniquement après avoir répondu.
  - Message de correction clair (texte + couleur contrastée).
- **Fin de Partie & Rejouabilité** : Affichage d'un écran de bilan final avec appréciation personnalisée et bouton "Rejouer" réinitialisant la partie.
- **Support des Petits Écrans & Rotation** : `ScrollView` pour la réactivité de l'affichage et gestion du cycle de vie via `onSaveInstanceState`.

---

## 🚀 Comment Ouvrir et Lancer le Projet
1. Ouvrir **Android Studio** (Version Hedgehog / Jellyfish / Ladybug ou supérieure).
2. Sélectionner **File > Open...** et choisir le dossier racine du projet `MyApplicationjeu`.
3. Laisser Gradle synchroniser les dépendances (`Sync Project with Gradle Files`).
4. Choisir un émulateur ou un appareil physique Android connecté (API 24+).
5. Cliquer sur **Run 'app'** (bouton vert ▶) ou exécuter la commande Gradle :
   ```bash
   ./gradlew assembleDebug
   ```

---

## 📝 Compte Rendu : Réponses aux Questions

### 1. Comment représentez-vous une question et sa bonne réponse ?
Chaque question est représentée par une instance de la `data class` Kotlin `Question` définie dans `Question.kt` :
```kotlin
data class Question(
    val questionText: String,
    @DrawableRes val imageResId: Int,
    val choices: List<String>,
    val correctAnswerIndex: Int,
    val imageDescription: String
)
```
La bonne réponse n'est pas stockée sous forme de chaîne brute, mais sous la forme d'un indice entier `correctAnswerIndex` (valeur `0`, `1` ou `2`) qui pointe directement vers la position de la bonne proposition dans la liste `choices`. Les 5 questions sont ensuite regroupées dans un tableau statique `arrayOf<Question>(...)` dans `MainActivity.kt`.

### 2. Comment le clic sur un bouton permet-il de savoir quel choix a été sélectionné ?
Dans `MainActivity.kt`, chaque bouton de choix (`btn_choix1`, `btn_choix2`, `btn_choix3`) est associé à un `OnClickListener` transmettant un identifiant numérique correspondant à son indice :
```kotlin
binding.btnChoix1.setOnClickListener { onChoiceSelected(0) }
binding.btnChoix2.setOnClickListener { onChoiceSelected(1) }
binding.btnChoix3.setOnClickListener { onChoiceSelected(2) }
```
Dans la méthode `onChoiceSelected(choiceIndex: Int)`, la valeur transmise `choiceIndex` est comparée à `question.correctAnswerIndex`.

### 3. Comment empêchez-vous l’attribution de plusieurs points pour une question ?
L'attribution multiple de points est empêchée au niveau de la logique et de l'interface utilisateur :
1. **Drapeau d'état boolean** : Une variable boolean `hasAnswered` est positionnée à `true` dès le premier clic. Si l'utilisateur reclique, la condition `if (hasAnswered) return` bloque immédiatement tout traitement supplémentaire.
2. **Désactivation UI** : Les 3 boutons de choix sont désactivés physiquement au niveau de la vue via `isEnabled = false` dès qu'un choix est validé.

### 4. Quelle différence faites-vous entre le score et la progression ?
- **Le Score** mesure la **performance** du joueur. Il augmente de 1 point uniquement si la réponse choisie est correcte (`choiceIndex == question.correctAnswerIndex`). Il reste inchangé en cas de mauvaise réponse.
- **La Progression** (représentée par la `ProgressBar`) mesure l'**avancement** dans le quiz. Elle augmente de 1 unité à chaque question répondue, que la réponse soit bonne ou mauvaise (`progress = currentQuestionIndex + 1`).

### 5. Comment détectez-vous la dernière question et affichez-vous le résultat ?
La détection de la dernière question s'effectue en comparant l'indice courant `currentQuestionIndex` avec la taille du tableau de questions (`questions.size - 1`, soit `4` pour 5 questions) :
- Lors de la réponse à la question 5 (`index == 4`), le libellé du bouton de navigation bascule de `"Question suivante"` à `"Voir le résultat"`.
- Lors du clic sur `"Voir le résultat"`, la fonction `displayFinalResult()` masquela vue de la question (`binding.layoutQuestionContainer.visibility = View.GONE`) et affiche le conteneur de résultat final (`binding.layoutResult.visibility = View.VISIBLE`).
