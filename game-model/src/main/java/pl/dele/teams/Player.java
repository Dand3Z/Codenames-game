package pl.dele.teams;

public abstract class Player {

    private String name;

    Player(String name) {
        this.name = name;
    }

    Player() { this("empty"); }

    String getName(){
        return name;
    }
}
