# Guide Officiel : Microservices Smart Campus & Observabilité Actuator
**Architecture Web Distribuée (AWD) — ESPRIT 2026-2027**  
**Équipe Smart Campus — 4DAS11, Groupe 2**

---

## 1. Découpage Officiel des Microservices (Smart Campus)

Tous les microservices s’enregistrent auprès du serveur de registre **Netflix Eureka** et intègrent **Spring Boot Actuator** pour l'observabilité.

| Microservice | Port | Membre Responsable | Base de données | Endpoints Métier & Actuator |
|---|---|---|---|---|
| **eureka-server** | `8761` | Infrastructure commune | — | [http://localhost:8761](http://localhost:8761) |
| **ms-reservations** | `8081` | Bechir Haboubi | PostgreSQL (`reservations_db`) | `/api/rooms`, `/api/equipment-types`, `/actuator/health` |
| **ms-evenements** | `8082` | **Dhia Trabelsi** | MySQL (`campus_evenements`) | `/evenements/hello`, `/evenements`, `/actuator/health` |
| **ms-annonces** | `8083` | Yassine Gharbi | H2 (`annonces_db`) | `/api/contents`, `/actuator/health`, `/actuator/info` |
| **ms-objets-perdus** | `8084` | Chadha Dalhoumi | MongoDB (`objets_db`) | `/api/items`, `/actuator/health`, `/actuator/info` |
| **ms-notifications** | `8085` | Toute l'équipe | H2 (`notifications_db`) | `/api/notifications`, `/actuator/health`, `/actuator/info` |

---

## 2. Réponses Rapides pour le Professeur (5 Points)

1. **Serveur de découverte :** Mise en place de **Netflix Eureka** (`@EnableEurekaServer`, port `8761`) comme registre centralisé des microservices Smart Campus.
2. **Découpage en microservices :** Chaque membre dispose de son propre service indépendant avec sa propre base de données (`ms-reservations`, `ms-evenements`, `ms-annonces`, `ms-objets-perdus`, `ms-notifications`).
3. **Observabilité Actuator :** Intégration de **Spring Boot Actuator** sur chaque microservice pour exposer `/actuator/health`, `/actuator/info` et `/actuator/metrics`.
4. **Remontée de santé réelle :** Avec `eureka.client.healthcheck.enabled=true`, si la base de données d'un microservice tombe, son statut bascule automatiquement de **`UP`** à **`DOWN`** dans le tableau de bord Eureka.
5. **Résultats :** Toutes les routes répondent avec un code HTTP 200 et les 5 services sont visibles simultanément dans le dashboard Eureka.
