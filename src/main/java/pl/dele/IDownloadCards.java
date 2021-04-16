package pl.dele;

import java.util.ArrayList;

public interface IDownloadCards {

    /**
     * Return the desired number of cards
     * @return
     */
    ArrayList<Card> generatePack(int amount);
}
