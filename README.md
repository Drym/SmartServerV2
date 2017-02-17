# SmartServer

## Environnement de développement

- IntelliJ IDEA
- Maven
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

## Services proposés

- http://ip:7777/checkpoint permet d'envoyer un trajet avec sa succession de checkpoints identifiés par leurs coordonnées gps.
  retour: Le découpage du trajet réalisé par l'api GoogleRoadMap
- http://ip:7777/record envoie un trajet finie au serveur et enregistre le résultat dans la base de donnée, met à jour la svm.
  retour: "ok"
- http://ip:7777/predict envoie l'état courant du trajet non finie avec heure de départ
  retour: la prediction du nombre total de seconde du trajet
- http://ip:7777/stats aucune donnée demandée
  retour: l'ensemble des stats par jour de la semaine (moyenne, min, max ...)
- http://ip:7777/model aucune donnée en entrée
  retour: le fichier model généré par la svm, permet l'incorparation sur d'autres plateformes utilisant la même librairie
