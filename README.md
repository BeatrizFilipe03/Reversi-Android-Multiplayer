# Reversi - Android Multiplayer
 
An Android app to play Reversi online against another player in real time. Programming for Mobile Devices course project (ISEL, 2024).

**[▶ Demo video](https://www.dropbox.com/scl/fi/9ayyl0muzmarws2mtxy8i/CheR-g42-Video.mp4?rlkey=1mznswwl2plicr352lcbidhjo&st=io2j6rxo&dl=0)**

## Features

- **Lobby and invites** — see who is online, invite a player and choose your piece colour.
- **Real-time matches** — moves are synced between both phones through Cloud Firestore; only valid moves are allowed.
- **Favourites and replay** — save finished games locally and replay them move by move.
The game logic lives in a domain layer independent from Android and is covered by 50 unit tests.
 
**Tech:** Kotlin · Jetpack Compose · MVVM · Firebase Cloud Firestore · Jetpack DataStore · JUnit
 
**Authors:** Beatriz Filipe, Danilo Vieira
