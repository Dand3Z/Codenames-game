package pl.dele.teams;

public class Spymaster extends Player{

    public PlayerType playerType = PlayerType.SPYMASTER;

    Spymaster(String name) {
        super(name);
    }

    public Spymaster() { super(); }
}
