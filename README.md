# Smart Campus

Plateforme de vie de campus : réservation de salles et de matériel, événements de clubs et de
l'établissement, annonces et blog, objets perdus.

Projet AWD (4DAS11, groupe 2), année universitaire 2026-2027 · Validation 1 : conception.

## Équipe

| Membre | Microservice | Base |
|---|---|---|
| Bechir | ms-reservations | PostgreSQL |
| Dhia | ms-evenements | MySQL |
| Yassine | ms-annonces | H2 (fichier) |
| Chadha | ms-objets-perdus | MongoDB |
| Toute l'équipe | ms-notifications (commun) | H2 (fichier) |

## Architecture

![Architecture globale](Documentation/Diagrams/architecture-globale.png)

| Composant | Responsabilité | Port |
|---|---|---|
| api-gateway | point d'entrée unique, routage, vérification du JWT | 8080 |
| ms-reservations | salles, matériel, réservations | 8081 |
| ms-evenements | clubs, adhésions, événements, inscriptions | 8082 |
| ms-annonces | annonces et articles de blog | 8083 |
| ms-objets-perdus | objets perdus et trouvés, correspondance, restitution | 8084 |
| ms-notifications | notifications individuelles, rappels, temps réel (SSE) | 8085 |
| eureka-server | découverte des services | 8761 |
| config-server | configuration centralisée | 8888 |
| Keycloak | authentification, rôles STUDENT / STAFF / ADMIN | 8180 |
| RabbitMQ | événements asynchrones | 5672 / 15672 |

Chaque microservice possède sa propre base ; aucune base n'est partagée.

## Lancement (développement)

1. `cp .env.example .env`, puis adapter les mots de passe.
2. `docker compose up -d`
3. Démarrer dans l'ordre : `eureka-server`, `config-server`, `api-gateway`, puis les microservices
   (`./mvnw spring-boot:run` dans chaque dossier de `Backend/`).
4. Vérifier : http://localhost:8761 (Eureka) et `http://localhost:<port>/actuator/health`.

## Organisation

- Suivi des tâches : GitHub Projects (voir l'onglet *Projects* du dépôt).
- Branches : `main` (stable) et `feature/<service>-<sujet>` ; fusion par Pull Request.
- Chaque membre commite son propre microservice depuis son compte.

## Structure du dépôt

```
Backend/          un projet Spring par microservice et par composant d'infrastructure
Frontend/         application cliente
Documentation/
  Presentation/   support de la séance 4
  Diagrams/       architecture, cas d'utilisation, carte des contextes, classes, séquences
  DossierTechnique/
infra/keycloak/   export du realm Keycloak
docker-compose.yml
```
