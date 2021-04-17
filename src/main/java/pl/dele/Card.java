package pl.dele;

public final class Card {
    // == fields ==

    // text on the card e.g. car, dog etc.
    private final String text;

    // == constructors ==

    public Card(String text) {
        this.text = text.toUpperCase();
    }

    // == methods ==

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

    @Override
    public String toString() {
        return "Card{" +
                "text='" + text + '\'' +
                '}';
    }
}
