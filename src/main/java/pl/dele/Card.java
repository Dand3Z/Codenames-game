package pl.dele;

public class Card {
    // text on the card e.g. car, dog etc.
    private final String text;

    public Card(String text) {
        this.text = text.toUpperCase();
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.text.equals(card.text);
    }

    @Override
    public int hashCode() {
        return this.text.hashCode();
    }

}
