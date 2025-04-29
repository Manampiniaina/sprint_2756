# **SPRINT FRAMEWORK - ETU002476**

## 🏁 Sprint 0

**🎯 Objectif :**  
Créer un servlet qui réceptionne toutes les requêtes clients et les traite.

**✅ Côté Framework :**
- Création d'un servlet `FrontController` dont la méthode `processRequest` affiche l'URL courante.

---

## 🚀 Sprint 1

### **🛠 Modifications dans le Framework :**
1. Création d'une annotation `@Controller`
2. Annotation des classes contrôleurs avec `@Controller`
3. Regroupement des contrôleurs dans un même package

### **🔁 Modifications dans `FrontController` :**
- Récupération du nom du package contenant les contrôleurs
- Vérification si les contrôleurs sont déjà scannés :
  - ✅ Oui : Affichage de la liste des contrôleurs
  - ❌ Non : Scan et affichage de la liste des contrôleurs

---

## 🔗 Sprint 2

**🎯 Objectif :**  
Associer une URL à une classe et une méthode spécifique.

### **Étapes :**
- Création d'une annotation `@GET` pour les méthodes dans les contrôleurs
- Création de la classe `Mapping` avec :
  - `String className`
  - `String methodName`
- Dans `FrontController` :
  - Suppression de l'attribut `boolean`
  - Création d’un `HashMap<String, Mapping>`
  - Lors de l'initialisation :
    - Scan des contrôleurs
    - Pour chaque méthode annotée `@GET` :
      - Création d’un `Mapping`
      - Association dans la `HashMap`
  - `processRequest` :
    - Recherche du `Mapping` pour l'URL
    - Affichage du `Mapping` si trouvé, sinon message d'erreur

---

## ⚙️ Sprint 3

**🎯 Objectif :**  
Exécuter la méthode correspondant à une URL.

### **Étapes :**
- Dans `processRequest` :
  - Récupérer la classe et la méthode via `Mapping`
  - Instancier la classe
  - Invoquer la méthode
  - Afficher le résultat retourné

---

## 🔄 Sprint 4

**🎯 Objectif :**  
Envoyer des données du contrôleur vers la vue.

### **Étapes :**
- Création de la classe `ModelView` :
  - `String url`
  - `HashMap<String, Object> data`
  - Méthode `addData(String key, Object value)`
- Dans `processRequest` :
  - Si retour = `String` → rediriger
  - Si retour = `ModelView` :
    - Boucler sur `data` et faire `request.setAttribute`
    - Rediriger vers l’`url` spécifiée
  - Autre type → message d'erreur

---

## 🧱 Sprint 5

**🎯 Objectif :**  
Gérer les exceptions du framework.

### **Étapes :**

#### 🧪 Build time :
- Erreur si :
  - Annotation dupliquée
  - Package de contrôleurs vide/inexistant

#### 💥 Runtime :
- Erreur 404 si URL inconnue
- Erreur si type de retour non reconnu (`ni String ni ModelView`)

---

## 📩 Sprint 6

**🎯 Objectif :**  
Envoyer des données depuis la vue vers le contrôleur.

### **Étapes :**
- Création de l’annotation `@RequestParam`
- Dans `processRequest` :
  - Associer les paramètres du formulaire aux paramètres de la méthode
  - Gérer :
    - Appel correct selon le type de retour (`String` ou `ModelView`)
    - Erreur si nombre de paramètres insuffisant
- Utilisation de la librairie [Paranamer 2.8](https://mvnrepository.com/artifact/com.thoughtworks.paranamer/paranamer/2.8) pour récupérer les noms des paramètres si non annotés
- Compilation avec l’option `-g` pour inclure les noms de paramètres

---

## 🧍 Sprint 7

**🎯 Objectif :**  
Accepter des objets en paramètre de méthode et initialiser leurs attributs automatiquement.

### **Étapes :**
1. Créer une annotation pour l'objet paramètre
2. Implémenter un process automatique :
   - Boucle sur les attributs
   - Récupération des valeurs via `request.getParameter`
3. Création d’une annotation `@ParamName` sur les champs (si noms différents entre classe et formulaire)

---

## 🗂 Sprint 8

**🎯 Objectif :**  
Gérer et utiliser les sessions.

### **Étapes :**

#### 1. Création de `MySession` :
- Attribut : `HttpSession session`
- Méthodes :
  - `get(String key)`
  - `add(String key, Object object)`
  - `delete(String key)`

#### 2. Injection automatique :
- Lors de l'appel des méthodes du contrôleur :
  - Si un paramètre est de type `MySession`, instancier avec `req.getSession()`

---
