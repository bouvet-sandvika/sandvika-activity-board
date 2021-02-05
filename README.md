# Sandvika Activity Board

Strava er en internettjeneste for sporing av fysisk aktivitet som inkluderer funksjoner for sosiale nettverk. 
Den brukes mest til sykling og løping ved hjelp av GPS-data. Strava har et åpent API som man lett kan koble til 
egne apper, som Sandvika Activity Board benytter seg av.

Dokumentasjon til Strava sin API finnes her: https://developers.strava.com/docs/reference/
 
Sandvika Activity Board er i produksjon her: https://speedo.bouvet.no/ 

Man må ha en Strava-profil og være medlem av en konkurranse definert av Bouvet Sandvika for å få opp visningen. 

Boardet viser informasjon hentet fra Strava koblet til de forskjellige medlemmene av konkurransen. 
Her kan man se hvilke turer de forskjellige medlemmene har vært på, hvor mange poeng de har fått osv. 
Det viser også hvem som er i ledelsen. 

Dette er et initiativ for å få medarbeiderene i Bouvet Sandvika til å være aktive, da man basert på informasjonen 
i konkurransen kårer en vinner. Det skal være en lav terskel for å bli med i konkurransen, og derfor får man et 
"handicap" som gir mer poeng om det er den første aktiviteten på en stund, og mindre om man er veldig aktiv. Dette 
betyr at man ikke nødvendigvis må trene aller mest for å vinne en konkurranse. 

### Kjøring lokalt

For å bygge og kjøre dette prosjektet lokalt, er det en del ting man må ha installert først.

- IntelliJ kjører både frontend og backend. JDK og Lombok må være satt opp.
- Databasen (MongoDB) kjøres på et Docker image -> Docker og Docker Compose må være innstallert.
- Om man vil sjekke den lokale databasen kan man bruke MongoDB Compass
- Postman er nyttig for testing av REST-kall, og for å få satt opp brukere og konkurranser lokalt

I IntelliJ kjører man `mvn clean install` for å bygge prosjektet. 

For å kjøre databasen lokalt kan man kjøre `docker-compose up -d` for eksempel i terminalen i IntelliJ.

`docker ps` gir informasjon om databasen kjører riktig og på hvilken port. 
Denne porten bruker man når man skal se på databasen i Compass, i formen `mongodb://localhost:27017`.

Programmet kjøres på `localhost:8005` ved hjelp av `SandvikaActivityBoardApplication.java`.

#### Bruker og Konkurranse

For å få sett boardet lokalt må man først ha en innlogget bruker som er medlem av en konkurranse.
- Sørg for å ha en bruker å logge inn med på https://www.strava.com/
- For å kunne gjøre usikrede kall fra Postman: Fjern kommentaren av linje 47 og 48 i fila `OAuth2Config.java`.
- Sørg for at prosjektet kjører med profil develop (forandres i `application.yaml` - forandring av profil må ikke 
  committes til produksjon!)

I Postman: POST kall til `http://localhost:8005/club` med Body = raw JSON:
```json
{
  "id" : "Test",
  "competitionStartDate" : "2021-01-01",
  "adminIds" : [123],
  "memberIds" : [123]
}
```
hvor "Test" blir erstattet med ditt valgte navn for konkurransen, startdatoen blir valgt og "123" blir erstattet med 
din Strava-Id som du finner ved å kopiere tallet som står til slutt i URL-en når du går til din profil i Strava. 
URL-en skal se slik ut: `https://www.strava.com/athletes/123`.

Nå er minst en bruker og en konkurranse lagt til lokalt i databasen. Dette kan man sjekke i Compass. 

***NB: Husk å kommentere ut igjen linje 47 og 48 i `OAuth2Config.java` etter kallene til Postman. Programmet blir veldig 
sårbart om denne forandringen kommer ut i produksjon.***


## Deploy

