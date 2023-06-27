package myBlackJack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Card.java Card represents a playing card.
 */
public class Card {
    // String value that holds the suit of the card
    private String suit;
    // String value that holds the rank of the card
    private String rank;
    // int value that holds the point value.
    private int pointValue; // Create an instance variable of type boolean that returns true if rank is

    private Boolean isAce;
    // "Ace"

    /**
     * Creates a new Card instance.
     *
     * @param cardRank
     *            a String value containing the rank of the card
     * @param cardSuit
     *            a String value containing the suit of the card
     * @param cardPointValue
     *            an int value containing the point value of the card
     */
    public Card(String cardRank, String cardSuit, int cardPointValue) {
	suit = cardSuit;
	rank = cardRank;
	pointValue = cardPointValue; 
	if (cardRank.equals("Ace")) {
	    isAce = true;
	}
	else {
	    isAce=false;
	}
    }


    public static JLabel getCardFace(Card c) {
	return new JLabel(new ImageIcon("src\\cards\\" + c.toString() +".gif"));
    }
    
    public static JLabel getBlankCard() {
	return new JLabel(new ImageIcon("src\\cards\\back1.gif"));
    }
    
    public void changeValue() {
	pointValue = 1;
    }
    
    /**
     * Accesses this Card's suit.
     * 
     * @return this Card's suit.
     */
    public String suit() {
	return suit;
    }

    /**
     * Accesses this Card's rank.
     * 
     * @return this Card's rank.
     */
    public String rank() {
	return rank;
    }

    /**
     * Accesses this Card's point value.
     * 
     * @return this Card's point value.
     */
    public int pointValue() {
	return pointValue;
    }

    /**
     * Compare this card with the argument.
     * 
     * @param otherCard
     *            the other card to compare to this
     * @return true if the rank, suit, and point value of this card are equal to
     *         those of the argument; false otherwise.
     */
    public boolean matches(Card otherCard) {
	return ((otherCard.pointValue() == this.pointValue()) && (otherCard.suit().equals(this.suit()))
		&& (otherCard.rank().equals(this.rank())));
    }

    /**
     * Converts the rank, suit, and point value into a string in the format "[Rank]
     * of [Suit] (point value = [PointValue])". This provides a useful way of
     * printing the contents of a Deck in an easily readable format or performing
     * other similar functions.
     *
     * @return a String containing the rank, suit, and point value of the card.
     */
    @Override
    public String toString() {
	return (this.rank + this.suit );
    }
    public Boolean isAce() {
	return isAce;
    }
}