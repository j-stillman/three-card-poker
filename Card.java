
/*  CS 342 - Project 2
 *  Card.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class is the basic data that a single playing card holds.
 */

public class Card implements Comparable {

	// Suit and value of the card. Ex: value = 10, suit = 'H' -> "10 of hearts"
	int value;
	char suit;
	
	// Two parameter Constructor 
	public Card(char suit, int value)
	{
		
		// Set value and suit
		this.setValue(value);
		this.setSuit(suit);
		
	}// end Card()
	
	
	// Default constructor
	public Card()
	{
		
		// Default to 2 of clubs I guess
		this('C', 2);
		
	}// end Card()
	
	
	// Method to print out the value/suit of the card
	public String toString() 
	{
		
		// Get the value as a string
		String valString = getValString(this.getValue());
		String suitString = getSuitString(this.getSuit());
		
		return valString + " of " + suitString;
		
	}// end toString()
	
	
	// Method to return a name for a given value. Ex: 2 -> "Two", 11 -> "Jack"
	public static String getValString(int value) 
	{
		switch(value) {
			case 2: return "Two"; 
			case 3: return "Three"; 
			case 4: return "Four"; 
			case 5: return "Five"; 
			case 6: return "Six"; 
			case 7: return "Seven";
			case 8: return "Eight"; 
			case 9: return "Nine"; 
			case 10: return "Ten";
			case 11: return "Jack"; 
			case 12: return "Queen"; 
			case 13: return "King"; 
			case 14: return "Ace"; 
			default:
					System.out.println("Error: invalid value entered into Card.getValString(): " + value);
					return "Two"; 
		}
		
	}// end getValString()
	
	
	// Method to return a name for a given suit. Ex: 'C' -> "Clubs", 'H' -> "Hearts"
	public static String getSuitString(char suit) 
	{
		switch(suit) {
		case 'C': return "Clubs";
		case 'D': return "Diamonds";
		case 'S': return "Spades";
		case 'H': return "Hearts";
		default:
				System.out.println("Error: invalid suit entered into Card.getSuitString(): " + suit);
				return "Clubs";
		}
		
	}// end getSuitString()
	
	
	// Getters/Setters for value
	public int getValue() { return this.value; }
	public void setValue(int value) {
		if (value < 2 || value > 14) { 
			this.value = 2; 
			System.out.println("Error: invlaid value entered into Card constructor: " + value);
		}else{
			this.value = value;
		}
	}
	
	// Getters/Setters for suit
	public char getSuit() { return this.suit; }
	public void setSuit(char suit) {
		if (suit != 'C' && suit != 'D' && suit != 'S' && suit != 'H') {
			this.suit = 'C';
			System.out.println("Error: invalid suit entered into Card constructor: "+ suit);
		}else{
			this.suit = suit;
		}
	}


	// Method to compare the card to another
	@Override
	public int compareTo(Object other) {
		
		// Observe the class of other to make sure it is also a card
		if (this.getClass() == other.getClass()) {
			
			// Cast the other card to match the types
			Card otherCard = (Card) other;
			
			// First compare the value. If those are equal, compare the suits
			if (this.getValue() > otherCard.getValue()) {
				return 1;
			}else if (this.getValue() < otherCard.getValue()) {
				return -1;
			}else{
				
				if (this.getSuit() > otherCard.getSuit()) {
					return 1;
				}else if (this.getSuit() < otherCard.getSuit()) {
					return -1;
				}else{
					// They truly are equal
					return 0;
				}
				
			}
			
		}else {
			
			// Just call it greater than the other if they're incompatible
			return 1;
			
		}

	}
	
}
