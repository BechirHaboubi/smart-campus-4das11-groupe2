# Script de verification complete des endpoints Smart Campus
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Verification des Microservices Smart Campus (AWD 4DAS11)" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

$endpoints = @(
    @{ Name = "Eureka Dashboard"; Url = "http://localhost:8761" },
    @{ Name = "ms-reservations - Salles (Bechir)"; Url = "http://localhost:8081/api/rooms" },
    @{ Name = "ms-reservations - Actuator Health"; Url = "http://localhost:8081/actuator/health" },
    @{ Name = "ms-reservations - Actuator Info"; Url = "http://localhost:8081/actuator/info" },
    @{ Name = "ms-evenements - Clubs & Events (Dhia)"; Url = "http://localhost:8082/evenements/hello" },
    @{ Name = "ms-evenements - Actuator Health (MySQL)"; Url = "http://localhost:8082/actuator/health" },
    @{ Name = "ms-evenements - Actuator Info"; Url = "http://localhost:8082/actuator/info" },
    @{ Name = "ms-annonces - Contents (Yassine)"; Url = "http://localhost:8083/api/contents" },
    @{ Name = "ms-annonces - Actuator Health"; Url = "http://localhost:8083/actuator/health" },
    @{ Name = "ms-annonces - Actuator Info"; Url = "http://localhost:8083/actuator/info" },
    @{ Name = "ms-objets-perdus - Items (Chadha)"; Url = "http://localhost:8084/api/items" },
    @{ Name = "ms-objets-perdus - Actuator Health"; Url = "http://localhost:8084/actuator/health" },
    @{ Name = "ms-objets-perdus - Actuator Info"; Url = "http://localhost:8084/actuator/info" },
    @{ Name = "ms-notifications - Notifications (Equipe)"; Url = "http://localhost:8085/api/notifications" },
    @{ Name = "ms-notifications - Actuator Health"; Url = "http://localhost:8085/actuator/health" },
    @{ Name = "ms-notifications - Actuator Info"; Url = "http://localhost:8085/actuator/info" }
)

foreach ($ep in $endpoints) {
    try {
        $response = Invoke-WebRequest -Uri $ep.Url -Method Get -TimeoutSec 3 -UseBasicParsing
        Write-Host "[OK] $($ep.Name) -> HTTP $($response.StatusCode) ($($ep.Url))" -ForegroundColor Green
    } catch {
        Write-Host "[ATTENTE/HORS-LIGNE] $($ep.Name) ($($ep.Url))" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "Verification terminee !" -ForegroundColor Cyan
