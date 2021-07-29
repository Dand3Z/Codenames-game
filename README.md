# Codenames-game

Client server game operating on the similar principle as:
https://codenames.game

The game is made with Java 11 and JavaFX11. 
The communication between the client and the server is via TCP.

Game rules:
There are two teams - red and blue. Each of them consists of a Spymaster and Operatives.
We also have a board with 5x5 cards. The text on the card is randomly selected from a pool of over 250 words.
Each card has its own color: red, blue - correspond to the team colors, bisque - neutral and gray - the game over card.
The colors of the cards are seen only by the Spymasters and their task is to give the Operatives such hints so that they choose the correct cards based on them.

The project includes two modules:
- game-model - contains all game logic, rules etc.
- game-client-server - the part responsible for communication between clients and the server. The client has implemented a GUI created with JavaFX.

To create the project, I used: 
- Oracle JDK 15.0.2 (compatibility mode with Java 11) 
- Apache Maven 3.6.3
- JUnit 5.4.0
- Logback 1.2.3
- JavaFX 11-ea+25
- IntelliJ IDEA ULTIMATE 2019.3

## Screenshots
### Initial game - 4 clients (One Operative and One Spymaster for each team)
![initial-game-img](/screenshots/initial-game.png?raw=true "Initial game")
### Both teams picked several cards - cards discovered by Operatives are marked black at Spymasters
![some-picks-img](/screenshots/some-picks.png?raw=true "Some picks from both teams")
### The Blue Team almost won. There is only one card left to guess
![blue-team-almost-win-img](/screenshots/blue-team-almost-win.png?raw=true "Blue team almost won")
### The Red Team won becouse the Operative of the Blue Team selected game over card
![blue-operative-selected-gameover-card-Redteam-wins-img](/screenshots/blue-operative-selected-gameover-card-Red-team-wins.png?raw=true "Blue operative selected gameover card. Redteam wins")
