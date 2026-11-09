# Script de verification complete des endpoints Eureka et Actuator
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Verification des Microservices AWD (Eureka & Actuator)" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

$endpoints = @(
    @{ Name = "Eureka Dashboard"; Url = "http://localhost:8761" },
    @{ Name = "Eureka Apps (JSON/XML)"; Url = "http://localhost:8761/eureka/apps" },
    @{ Name = "Candidat - Hello"; Url = "http://localhost:8081/candidats/hello" },
    @{ Name = "Candidat - Liste Candidats"; Url = "http://localhost:8081/candidats" },
    @{ Name = "Candidat - Actuator Health"; Url = "http://localhost:8081/actuator/health" },
    @{ Name = "Candidat - Actuator Info"; Url = "http://localhost:8081/actuator/info" },
    @{ Name = "Candidat - Actuator Metrics"; Url = "http://localhost:8081/actuator/metrics" },
    @{ Name = "Candidat - HTTP Requests Metric"; Url = "http://localhost:8081/actuator/metrics/http.server.requests" },
    @{ Name = "Job - Hello"; Url = "http://localhost:8082/jobs/hello" },
    @{ Name = "Job - Actuator Health (DB Check)"; Url = "http://localhost:8082/actuator/health" },
    @{ Name = "Job - Actuator Info"; Url = "http://localhost:8082/actuator/info" },
    @{ Name = "Meeting (Node.js) - Hello"; Url = "http://localhost:8083/api/meetings/hello" },
    @{ Name = "Meeting (Node.js) - Health"; Url = "http://localhost:8083/health" },
    @{ Name = "Meeting (Node.js) - Info"; Url = "http://localhost:8083/info" },
    @{ Name = "Notification (Python) - Hello"; Url = "http://localhost:8084/api/notifications/hello" },
    @{ Name = "Notification (Python) - Health"; Url = "http://localhost:8084/health" },
    @{ Name = "Notification (Python) - Info"; Url = "http://localhost:8084/info" },
    @{ Name = "Candidature - Hello"; Url = "http://localhost:8085/candidatures/hello" },
    @{ Name = "Candidature - Health"; Url = "http://localhost:8085/actuator/health" }
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
