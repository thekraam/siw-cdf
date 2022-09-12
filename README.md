## Progetto universitario 'Centro di formazione professionale' per Sistemi Informativi sul Web
### Descrizione
Il progetto è basato sulla traccia richiesta per l'esame di Sistemi Informativi sul Web dell'appello di Settembre 2022. Viene richiesto lo sviluppo di una piccola applicazione web che consenta, nell'ambito logistico di un ipotetico Centro di Formazione Professionale, la gestione delle prenotazioni a degli esami di Certificazione.
Il sistema consente varie operazioni di inserimento, aggiornamento e lettura. Il sistema qui adottato assume il carattere di un tipico Pannello di Controllo ispirato al sistema 'smart_edu' dell'università di Roma Tre. L'idea è quella di fornire un accesso immediato ma sicuro alle risorse richieste, senza passare per interfacce grafiche che sarebbero inutilmente sofisticate nell'ambito di implementazione di questa web app.
L'applicazione dunque si presenta con un design minimale ed è esente di una vera landing page, poiché inteso come un supporto web ad un sistema pre-esistente (cioè il portale principale del Centro di Formazione).
### Casi d'uso
Il sistema prevede i seguenti casi d'uso:
#### UC1
##### Inserimento Certificazione - Attore primario: Amministratore/Segreteria
1. La segreteria vuole creare una nuova certificazione e renderla disponibile alla prenotazione
2. La segreteria si autentica mediante username e password. Il sitema convalida i dati immessi. Il sistema mostra la pagina index.
3. La segreteria sceglie l'attività 'Gestisci Certificazioni'. Viene mostrato l'elenco delle certificazioni già esitenti e i dettagli.
4. La segreteria sceglie l'attività 'Aggiungi nuova Certificazione'. Il sistema mostra il form da compilare.
5. La segreteria compila i campi con le informazioni della certificazione. Il sistema registra le informazioni della certificazione e la rende subito disponibile per la prenotazione.
#### UC2
##### Prenotazione di una Certificazione - Attore primario: Allievo
1. L'allievo intende prenotarsi ad una specifica certificazione.
2. L'allievo si autentica mediante username e password. Il sistema convalida i dati immessi. Il sistema mostra la pagina index.
3. L'allievo sceglie l'attività 'Certificazioni'. Viene mostrato l'elenco delle certificazioni disponibili alla prenotazione.
4. L'allievo sceglie l'attività 'Prenota' della certificazione di interesse. Il sistema registra la prenotazione tra quelle dello studente e mostra una conferma dell'operazione.
#### UC3
##### Modifica di una Certificazione - Attore primario: Amministratore/Segreteria
1. La segreteria intende effettuare la modifica di una certificazione esistente.
2. La segreteria si autentica mediante username e password. Il sitema convalida i dati immessi. Il sistema mostra la pagina index.
3. La segreteria sceglie l'attività 'Gestisci Certificazioni'. Viene mostrato l'elenco delle certificazioni già esitenti con i vari dettagli.
4. La segreteria sceglie l'attività 'Modifica' della certificazione di interesse. Il sistema mostra i vecchi dati della certificazione.
5. La segreteria compila il form della certificazione. Il sistema registra le informazioni, sovrascrivendole alle precedenti per la stessa certificazione. La certificazione è ora cambiata nell'intero sistema.
#### UC4
##### Consultazione dettagli certificazione - Attore primario: Allievo
1. L'allievo vuole creare una nuova certificazione e renderla disponibile alla prenotazione
2. L'allievo si autentica mediante username e password. Il sitema convalida i dati immessi. Il sistema mostra la pagina index.
3. L'allievo sceglie l'attività 'Certificazioni'. Viene mostrato l'elenco delle certificazioni già esitenti e alcuni dettagli.
4. L'allievo sceglie l'attività 'Visualizza dettagli' della certificazione di interesse. Il sistema mostra tutti i dati della certificazione.
#### UC5
##### Consultazione dettagli certificazione - Attore primario: Amministratore/Segreteria
1. La segreteria vuole visualizzare i dettagli di una certificazione.
2. La segreteria si autentica mediante username e password. Il sitema convalida i dati immessi. Il sistema mostra la pagina index.
3. La segreteria sceglie l'attività 'Gestisci Certificazioni'. Viene mostrato l'elenco delle certificazioni già esitenti e i dettagli.
#### UC6
##### Modifica di un utente - Attore primario: Amministratore/Segreteria
1. La segreteria vuole modificare i dati di un utente.
2. La segreteria si autentica mediante username e password. Il sitema convalida i dati immessi. Il sistema mostra la pagina index.
3. La segreteria sceglie l'attività 'Gestisci Utenti'. Il sistema mostra la lista di Allievi e Amministratori attualmente registrati.
4. La segreteria sceglie l'attività 'Modifica' dell'utente di interesse. Il sistema mostra un form precompilato modificabile con i vecchi dati dell'utente.
5. La segreteria inserisce i nuovi dati dell'utente. Il sistema registra le informazioni sovrascrivendole, per lo stesso utente, a quelle vecchie.
#### UC7
##### Creazione di un nuovo amministratore di sistema - Attore primario: Amministratore/Segreteria
1. La segreteria vuole inserire un nuovo amministratore.
2. La segreteria si autentica mediante username e password. Il sitema convalida i dati immessi. Il sistema mostra la pagina index.
3. La segreteria sceglie l'attività 'Gestisci Utenti'. Il sistema mostra la lista di Allievi e Amministratori attualmente registrati.
4. La segreteria sceglie l'attività 'Aggiungi Amministratore'. Il sistema mostra un form da compilare.
5. La segreteria inserisce i dati del nuovo amministratore, compresa la password. Il sistema registra le informazioni creando un nuovo amministratore e mostra i dati inseriti, compresa la password.
#### UCX
##### Visualizzazione contenuti digitali, 'Login' e 'Registrazione' sono casi d'uso immediati.
