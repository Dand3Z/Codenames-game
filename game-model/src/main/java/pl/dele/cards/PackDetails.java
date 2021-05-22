package pl.dele.cards;

import java.util.Random;

class PackDetails {
    private final int AMOUNT;
    private final boolean STARTING_TEAM;
    private final int RED_CARDS_AMOUNT;
    private final int BLUE_CARDS_AMOUNT;
    private final int NEUTRAL_CARDS_AMOUNT;
    private final int BLACK_CARDS_AMOUNT;

    PackDetails(int cardsAmount, int startingTeamAmount, int blackAmount) {
        if (cardsAmount <= 0 || startingTeamAmount <= 0 || blackAmount < 0){
            throw new IllegalArgumentException();
        }

        Random random = new Random();
        /**
         * Which team starts
         *  false - RedTeam
         *  true  - BlueTeam
         */
        STARTING_TEAM = random.nextBoolean();
        AMOUNT = cardsAmount;
        RED_CARDS_AMOUNT = STARTING_TEAM ? startingTeamAmount - 1 : startingTeamAmount;
        BLUE_CARDS_AMOUNT = STARTING_TEAM ? startingTeamAmount : startingTeamAmount - 1;
        BLACK_CARDS_AMOUNT = blackAmount;
        NEUTRAL_CARDS_AMOUNT = cardsAmount - RED_CARDS_AMOUNT - BLUE_CARDS_AMOUNT
                                - BLACK_CARDS_AMOUNT;
    }

    public PackDetails(){
        this(25,9,1);
    }

    boolean isSTARTING_TEAM() {
        return STARTING_TEAM;
    }

    int getRED_CARDS_AMOUNT() {
        return RED_CARDS_AMOUNT;
    }

    int getBLUE_CARDS_AMOUNT() {
        return BLUE_CARDS_AMOUNT;
    }

    int getNEUTRAL_CARDS_AMOUNT() {
        return NEUTRAL_CARDS_AMOUNT;
    }

    int getBLACK_CARDS_AMOUNT() {
        return BLACK_CARDS_AMOUNT;
    }

    int getAMOUNT() {
        return AMOUNT;
    }
}
