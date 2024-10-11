# **SPRINT FRAMEWORK - ETU002476**

## Sprint 0

**Objectif:** Créer un servlet qui réceptionnera toutes les requêtes clients et qui les traitera

Coté Framework:

* Creation d'un servlet FrontController dont la methode processRequest affichera l'url dans lequel on se trouve

## Sprint 1

### **Modification dans mon framework :**

1. Creation d'une classe annotation du nom de Controller
2. Annoter mes controleurs avec Controller
3. Mettre mes controleurs dans le meme package

### **Modification dans FrontController :**

(Prendre le nom du package où se trouvent mes controleurs)

1. Tester si j'ai déjà scanner mes controleurs

* Si oui, afficher la liste des noms de mes controleurs
* Sinon scanner, puis afficher la liste des noms de mes controleurs

## Sprint 2

### **Objectif :**

 Récupérer la classe et la méthode associées à une URL donnée

### **Étapes :**

Creation d'une annotation GET pour annoter les méthodes dans les contrôleurs

Creation d'une classe Mapping qui aura pour attributs :

* String className
* String methodName

Dans FrontController :

- Enlever l'attribut boolean
- Créer un HashMap (String url, Mapping)
- init :

  - Faire les scans pour avoir les contrôleurs

  * Pour chaque contrôleur, prendre toutes les méthodes et voir s'il y a l'annotation GET
  * S'il y en a, créer un nouveau Mapping : (controller.name, method.name)
  * HashMap.associer(annotation.value, Mapping)
- ProcessRequest :

  - Prendre le Mapping associé au chemin URL de la requête
  - Si on trouve le Mapping associé, afficher le chemin URL et le Mapping
  - Sinon, afficher qu'il n'y a pas de méthode associée à ce chemin

## Sprint 3

### **Objectif :**

Exécuter la méthode de la classe associée à une URL donnée

### **Étapes :**

* Dans le FrontController ( ProcessRequest ): Si on trouve le Mapping associé à l'URL ,

  * Récupérer la classe par son nom

  - Récupérer la méthode par son nom
  - Invoquer la méthode sur l'instance de la classe
  - Afficher la valeur retournée par la méhode

## Sprint 4

### **Objectif:**

Envoyer des données du controller vers view

### **Etapes:**

* Côté Framework
  * Création d'une classe ModelView qui aura pour attributs:
    * String url : url de destination après l'exécution de la méthode
    * HashMap<String : nom de la variable, Object: sa valeur> data : donnée à envoyer vers cette view
      * Création d'une methode"addData" qui a comme type de retour void pour pouvoir mettre les données dans HashMap
  * Dans FrontController
    * Dans la methode processRequest, récupération des données issues de la méthode annotée Get
      * si les data sont de type string, retour la valeur directement
      * si les données sont de type ModelView, récupération le url et dispatcher les données vers cet url
        * Boucle de data pour mettre y faire request.setAttribute et Dispatcher dans l'url (view)
      * si autre, retourner "methode de retour non reconnu"

## Sprint 5

### Objectif:

Gestion d'exception

### Etapes:

* **Building**
  * Exception si une annotation est dupliquee c'est a dire que plusieurs methodes ont la meme annotations
  * Exception si le package des controllers est vide ou n'existe pas
* **Process**
  * Exception "Error  404 not found" si l'url n'existe pas alors on ecrit sur la page
  * Exception si le type de retour du methode de la classe controller n'est pas un String ou un ModelView

## Sprint 6

### Objectif:

Envoyer des donnees du view vers controller

### Etapes:

* Creation d'une annotation RequestParam pour annoter les attrubuts d'une methode dans un controller
* Dans FrontController
  * Comparaison si le nom du parametre du formulaire correspond a l'anotation du parametre de methode correspondant a l'url
    * Si oui, assignation de la valeur du parametre du formulaire dans le parametre de la methode
      * Si la method retourne un String alors excecution la methode
      * Si c'est ModelView alors dispatch des donnees dans le view correspondant a l'url designe par ModelView
      * Sinon type de retour invalide
    * Si nombre de parametre du formulaire different du nombre de parametre du methode alors Exception de parametre insuffisant
  * Si jamais l'utilisateur du framework n'avais pas annoté ses paramètres d'utilisé le nom des paramètres en question.
    * Pour recuperer le nom des parametres, on utilisera une librairie externe paranamer de Throughwork: [paranamer-2.8](https://mvnrepository.com/.../com.../paranamer/2.8)
    * Et ajouter **-g** comme option a la compilation du projet

## Sprint 7

**Objectif:**

Permettre de mettre en paramètre d'une fonction de mapping un objet et de setup ses attributs.

**Etapes:**

1. Créer une annotation pour l'objet en paramètre
2. Créer un process qui va s'effectuer automatiquement lors que le programme détecte l'annotation créée plus tôt
   * Ce process va bouclé tous les attributs de l'objet pour obtenir leurs valeurs attribuées dans request.getParameter
   * Créer une nouvelle annotation de type ElementType.FIELD pour donner le choix aux utilisateurs du framework le choix entre utilisé le même nom dans sa classe et son formulaire ou annoté l'attribut avec le nom présent dans son formulaire sans devoir à utilisé le même nom

## Sprint 8

**Objectif :**

Gestion et utilisation de session

**Etapes :**

* Étape 1:

  * Créer une classe MySession ayant comme seul attribut HttpSession session

  - Ajouter fonctions get(String key), add(String key, Object objet), delete(String key)
* Étape 2 :

  * A l'appel des méthodes des controllers de l’utilisateur, pendant la génération des arguments, vérifier si le paramètre est de type MySession et dans ce cas, créer un MySession avec req.getSession()
