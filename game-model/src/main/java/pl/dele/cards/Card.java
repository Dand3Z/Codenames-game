package pl.dele.cards;

public final class Card {
    // == fields ==

    // text on the card e.g. car, dog etc.
    private final String phrase;

    // == constructors ==
    public Card(String phrase) {
        this.phrase = phrase.toUpperCase();
    }

    // == methods ==
    public String getPhrase() {
        return phrase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.phrase.equals(card.phrase);
    }

    @Override
    public int hashCode() {
        return this.phrase.hashCode();
    }

    @Override
    public String toString() {
        return "Card{" +
                "text='" + phrase + '\'' +
                '}';
    }
}
