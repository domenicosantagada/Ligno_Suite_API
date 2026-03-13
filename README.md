# LignoSuite API - Backend

Questo repository contiene il codice sorgente del backend per il progetto **LignoSuite**, sviluppato in occasione
dell'esame di Web Applications.

STUDENTE: **Santagada Domenico**
MATRICOLA: **213544**

Il sistema espone i servizi necessari alla gestione degli utenti, della rubrica clienti e dei preventivi per una
falegnameria, comunicando in formato JSON con l'applicazione client (Frontend in Angular).

## Tecnologie Utilizzate

* **Linguaggio:** Java 21
* **Framework:** Spring Boot
* **Accesso ai dati:** Spring Data JPA / Hibernate
* **Database:** H2 Database (File-based)
* **Build System:** Gradle

---

## Prerequisiti

Per eseguire il progetto correttamente sul proprio ambiente locale, è necessario aver installato:

* **Java Development Kit (JDK) 21** o superiore.

*(Non è necessario avere Gradle installato sul sistema, in quanto il progetto include il Gradle Wrapper).*

## Per eseguire il progetto

1. Clonare il repository.
2. Eseguire il comando `gradle bootRun` per avviare il server.
3. Il server sarà accessibile all'indirizzo `http://localhost:8080/h2-console`.
4. Credenziali di accesso alla console H2:
5. JDBC URL: jdbc:h2:file:./data/falegnameriadb
6. Username: sa
7. Password: pa
