# Guide Complet : Prosit 1 (Eureka Server) & Prosit 2 (Spring Boot Actuator)
**Architecture Web Distribuée (AWD) — ESPRIT 2026-2027**  
**Équipe Smart Campus — 4DAS11, Groupe 2**

---

## 1. Vue d'Ensemble de l'Architecture

Tous les microservices s’enregistrent auprès du serveur de registre **Netflix Eureka** et exposent leur état de santé et leurs métriques via **Spring Boot Actuator** (ou des routes équivalentes en Node.js et Python).

| Microservice | Technologie | Port par Défaut | Nom dans Eureka | Base de données | Endpoints clés |
|---|---|---|---|---|---|
| **Eureka Server** | Spring Boot | `8761` | `EUREKA` | — | `http://localhost:8761` |
| **Candidat** | Spring Boot | `8081` (ou `8091`) | `CANDIDAT` | H2 (in-memory) | `/candidats`, `/actuator/health`, `/actuator/info`, `/actuator/metrics` |
| **Job** | Spring Boot | `8082` | `JOB` | MySQL (`job_db`) | `/jobs`, `/actuator/health`, `/actuator/info` |
| **ms-evenements** *(Smart Campus - Dhia)* | Spring Boot | `8082` | `MS-EVENEMENTS` | MySQL (`campus_evenements`) | `/evenements`, `/actuator/health`, `/actuator/info` |
| **Meeting** | Node.js / Express | `8083` | `MEETING` | — | `/api/meetings/hello`, `/health`, `/info` |
| **Notification** | Python / FastAPI | `8084` | `NOTIFICATION` | — | `/api/notifications/hello`, `/health`, `/info` |
| **Candidature** | Spring Boot | `8085` | `CANDIDATURE` | H2 / MySQL | `/candidatures/hello`, `/actuator/health`, `/actuator/info` |

---

## 2. Réponses aux Questions & Concepts Clés des Prosits

### Prosit 1 : Découverte de services avec Netflix Eureka
1. **Pourquoi Eureka est-il indispensable ?**
   - Dans une architecture distribuée, les adresses IP et les ports des microservices changent fréquemment (mise à l'échelle dynamique, conteneurs, redémarrages).
   - Eureka sert d'**annuaire centralisé (Service Registry)** : chaque service s'y enregistre sous un nom logique (`spring.application.name`) et envoie un heartbeat toutes les 30 secondes. Les services peuvent ainsi s'appeler via leur nom logique sans coder d'adresse IP en dur.
2. **Support polyglotte (Java, Node.js, Python) :**
   - Comme Eureka expose une **API REST standard**, les services écrits en Node.js (via `eureka-js-client` ou appels REST HTTP) et Python (via `py-eureka-client`) s'enregistrent dans le même annuaire que les services Spring Boot.
3. **Multi-instanciation & Montée en charge :**
   - On peut lancer une deuxième instance d'un service (ex. Candidat ou Meeting) sur un autre port :
     - En ligne de commande : `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8091`
     - En Node.js : `set PORT=8093 && node index.js`
     - En Python : `set PORT=8094 && python main.py`
   - Le dashboard Eureka affichera alors automatiquement `(2)` instances enregistrées pour ce service.

---

### Prosit 2 : Observabilité avec Spring Boot Actuator

#### Question 1 : Pourquoi combiner Actuator et Eureka (`eureka.client.healthcheck.enabled=true`) ?
- Par défaut, Eureka ne vérifie que le **heartbeat** (le fait que le processus Java tourne toujours). Si la base de données MySQL du service Job tombe en panne, le processus Java tourne encore, donc Eureka croirait à tort que l'instance est `UP`.
- Avec `eureka.client.healthcheck.enabled=true`, Eureka consulte l'endpoint `/actuator/health`. Si la base MySQL tombe, le composant `db` passe `DOWN`, le statut global passe `DOWN`, et Eureka reflète immédiatement `DOWN` dans son dashboard, évitant ainsi d'acheminer des requêtes vers une instance défaillante.

#### Question 2 : Les différents statuts de santé
| Statut | Signification | Code HTTP |
|---|---|---|
| **UP** | Le service et l'ensemble de ses composants (base, espace disque, ping) fonctionnent parfaitement. | `200 OK` |
| **DOWN** | Le service ou l'un de ses composants critiques est en panne. | `503 Service Unavailable` |
| **OUT_OF_SERVICE** | Le service a été retiré volontairement du trafic (maintenance, préchauffage). | `503 Service Unavailable` |
| **UNKNOWN** | L'état de santé du composant n'a pas pu être déterminé avec certitude. | `200 OK` |

#### Question 3 : Réponses aux questions sur les métriques Actuator (Page 4 du Prosit)
- **Mesure COUNT :**  
  Nombre total de requêtes HTTP traitées par le microservice depuis son démarrage.  
  Consultable sur : `http://localhost:8081/actuator/metrics/http.server.requests`
- **Mesure MAX :**  
  Durée de la requête la plus lente (en secondes). Permet de repérer les goulots d'étranglement ou les requêtes SQL lentes.
- **Que donne `/actuator/metrics/http.server.requests?tag=status:200` ?**  
  Cette requête applique un filtre sur les métriques pour ne compter que les requêtes ayant abouti à un code HTTP `200 OK`. Elle retourne le nombre (`COUNT`) et la durée cumulée/maximale (`TOTAL_TIME`, `MAX`) des requêtes réussies.

#### Question 4 : Modifier le niveau de logs à chaud sans redémarrer
- Consulter le niveau d'un logger :  
  `GET http://localhost:8081/actuator/loggers/com.esprit.candidat`
- Passer dynamiquement en niveau `DEBUG` pour diagnostiquer un bug sans coupure de service :  
  `POST http://localhost:8081/actuator/loggers/com.esprit.candidat`  
  Body JSON : `{"configuredLevel": "DEBUG"}`

---

## 3. Comment Lancer et Tester les Microservices

### Méthode 1 : Lancement en un clic avec les scripts fournis
Dans le dossier `Backend/` :
1. **Lancement global ordonné :**  
   Double-cliquez sur `start-all.bat`  
   *(Ce script démarre Eureka, attend 10 secondes qu'il soit prêt, puis démarre Candidat, Job, Candidature, Meeting et Notification).*
2. **Ou lancement individuel :**
   - `start-eureka.bat`
   - `start-candidat.bat`
   - `start-job.bat` (ou `start-ms-evenements.bat`)
   - `start-candidature.bat`
   - `start-meeting.bat`
   - `start-notification.bat`

### Méthode 2 : Lancement depuis IntelliJ IDEA
1. Ouvrez IntelliJ IDEA.
2. Ouvrez le projet `smart campus`.
3. Dans la vue Maven d'IntelliJ, rechargez le fichier [Backend/pom.xml](file:///c:/Users/hp/Documents/GitHub/smart%20campus/Backend/pom.xml).
4. Lancez les classes principales :
   - `EurekaServerApplication.java`
   - `CandidatApplication.java`
   - `JobApplication.java` (ou `MsEvenementsApplication.java`)
   - `CandidatureApplication.java`

---

## 4. Vérification du Rendu (Checklist Prosit 1 & 2)

- [x] **Eureka Dashboard :** Ouvrir `http://localhost:8761` et vérifier que `CANDIDAT`, `JOB`, `CANDIDATURE`, `MEETING` et `NOTIFICATION` sont tous listés `UP (1)`.
- [x] **Redirection /info :** Cliquer sur le lien de chaque instance dans Eureka pour vérifier qu'il ouvre bien sa page `/info`.
- [x] **Test de Santé Actuator :**
  - `http://localhost:8081/actuator/health` -> `{"status": "UP"}`
  - `http://localhost:8082/actuator/health` -> composants DB, CustomJobHealthIndicator
  - `http://localhost:8083/health` (Meeting Node.js) -> `{"status": "UP"}`
  - `http://localhost:8084/health` (Notification Python) -> `{"status": "UP"}`
- [x] **Test de Panne MySQL (Simulation Prosit 2) :**
  - Arrêter MySQL dans XAMPP/WAMP.
  - Actualiser `http://localhost:8082/actuator/health` -> statut passe à `DOWN` (503).
  - Actualiser le dashboard Eureka `http://localhost:8761` -> `JOB` passe à `DOWN (1)`.
  - Redémarrer MySQL -> `JOB` repasse automatiquement à `UP (1)`.
- [x] **Endpoints Métier :**
  - Candidat : `http://localhost:8081/candidats`
  - Meeting : `http://localhost:8083/api/meetings/hello`
  - Notification : `http://localhost:8084/api/notifications/hello`
