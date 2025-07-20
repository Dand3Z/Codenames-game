package pl.dele.teams;

public class Operative extends Player{

    public PlayerType playerType = PlayerType.OPERATIVE;

    Operative(String name) {
        super(name);
    }

    public Operative() { super(); }
}
