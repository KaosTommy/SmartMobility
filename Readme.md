# 🏙️ SmartMobility - Smart Mobility System

Progetto realizzato per il corso di **Ingegneria del Software (a.a. 2025-2026)** presso l'Università degli Studi di Bari. 
Il sistema implementa una piattaforma completa per la gestione in sharing di micro-mobilità urbana (Monopattini, Biciclette, Auto e Motocicli Elettrici).

## 👨‍💻 Team CODERS
* **Tommaso Gelao** (Lead & Sviluppo App Utente)
* **Massimo Morisco** (Sviluppo Fleet Management Operatore)
* **Antonello Antenori** (Sviluppo Urban Monitoring Admin)
* **Andrea Estivo** (Sviluppo Logiche Backend)

---

## 🚀 Architettura del Sistema
Il progetto è suddiviso in un motore logico robusto e in un ecosistema di interfacce web responsive progettate per simulare le viste dei diversi attori del sistema (RBAC).

### Stack Tecnologico
* **Backend:** Java 17, Spring Boot 3, Spring Data JPA, H2 Database (Simulazione RDBMS)
* **Frontend:** HTML5, CSS3, Vanilla JavaScript, LocalStorage API (Simulazione real-time)
* **Librerie Esterne:** [Leaflet.js](https://leafletjs.com/) (Mappe interattive GIS), [Chart.js](https://www.chartjs.org/) (Data Visualization)

---

## 🎯 Moduli e Funzionalità Principali

### 📱 1. User App (`app.html` & `profilo.html`)
* **Noleggio e Geofencing:** Prenotazione e sblocco mezzi con blocco automatico della chiusura corsa in caso di parcheggio in aree interdette (ZTL).
* **Gestione Finanziaria:** Wallet virtuale ricaricabile, salvataggio metodi di pagamento e calcolo dinamico delle tariffe (inclusa la tariffa ridotta in caso di "Pausa Corsa").
* **Sicurezza & GDPR:** Validazione obbligatoria Patente B per Auto/Moto e anonimizzazione irreversibile dell'account in caso di eliminazione.

### 🔧 2. Fleet Management (`operatore.html`)
* **Controllo Remoto:** Blocco d'emergenza in tempo reale dei veicoli in movimento (spegnimento istantaneo dell'app utente).
* **Manutenzione:** Ricezione ticket guasti dagli utenti, filtro rapido batterie critiche (< 10%) e chat di supporto live bidirezionale.
* **Gestione Utenza:** Sospensione (Ban) degli account per violazione dei termini d'uso ed emissione rimborsi sul Wallet.

### 🏛️ 3. Urban Monitoring (`admin.html`)
* **Tracciamento ZTL:** Disegno interattivo sulla mappa di No-Parking Zones che vengono istantaneamente sincronizzate e visualizzate sulle mappe di tutti gli utenti.
* **KPI Dashboard:** Monitoraggio flussi di traffico e statistiche mensili sui noleggi e sui tempi di riparazione degli operatori.

---

## ⚙️ Istruzioni per l'Avvio (Simulazione Locale)

### 1. Avviare il Server Backend
Aprire il terminale nella cartella `backend` ed eseguire:
```bash
# Per Windows
.\mvnw spring-boot:run

# Per macOS/Linux
./mvnw spring-boot:run
Aprire il file frontend/auth.html in un qualsiasi browser moderno (si consiglia Google Chrome o Edge). Per una corretta simulazione dell'app utente, si consiglia di attivare gli Strumenti per Sviluppatori (F12) e selezionare la visualizzazione "Dispositivo Mobile".

🔑 Credenziali di Test per la Commissione
Il sistema indirizza automaticamente l'utente all'interfaccia corretta in base al Ruolo Aziendale:

App Utente: t.gelao2@studenti.uniba.it

Dashboard Operatore: m.morisco12@studenti.uniba.it

Cruscotto Comune (Admin): a.antenori1@studenti.uniba.it

Password universale: password123
SMART MOBILITY SYSTEM - BACKEND CONFIGURATION
- Requisiti di sistema: Java 17 o superiore, Maven (incluso tramite mvnw).
- Database: H2 Relazionale In-Memory (auto-configurato, non richiede installazione locale).
- Istruzioni per l'installazione e l'avvio:
  1. Aprire il terminale nella root del progetto (cartella backend).
  2. Eseguire il comando: .\mvnw spring-boot:run
  3. Il server si avvierà sulla porta standard http://localhost:8080
- Popolamento iniziale: All'avvio, la classe DataSeeder provvederà a inizializzare automaticamente il database con utenti di test (Ruoli: UTENTE, OPERATORE, ADMIN), veicoli e stazioni di ricarica.
