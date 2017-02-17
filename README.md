# SmartServer

## Environnement de développement

- IntelliJ IDEA
- Git
- Sparkjava (web serveur, facile d'utilisation, très proche de flask)
- libsvm (Pour l'algorithme SVM)
- sqlite-jbdc (Librairie pour SQL)

## Installation

- Clonez le repository de l'application avec la commande :
`git clone https://github.com/Drym/SmartServerV2.git`
- Ouvrez IntelliJ, allez dans `File`, `New`, `Project from Existing Sources`. Sélectionnez le repertoire cloné. 
- Lors de l'importation du projet, sélectionnez `Import project from external model`, puis sélectionnez `Maven`.
- Allez ensuite dans `File`, `Project Structure` et ajoutez deux librairies à l'aide du bouton +. Les deux librairies à ajouter sont dans le dossier libs de notre projet.
- Enfin, lancez le serveur avec le fichier Main.java (src/main/java/Main.java).

## Utilisation

- Une fois le serveur lancé, vous n'avez plus rien à faire.
- Vous pouvez toute fois ajouter des données dans le serveur en les rentrants dans le fichier test_road.json (src/ressources/test_road.json)
