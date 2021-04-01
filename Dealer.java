import java.util.ArrayList;

/*  CS 342 - Project 2
 *  Dealer.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class holds the dealer info, including their hand and the game's deck
 */

public class Dealer {

	// Fields
	Deck theDeck;
	ArrayList<Card> dealersHand;
	
	// Constructor
	public Dealer() 
	{
		
		this.theDeck = new Deck();
		
	}// end Dealer()
	
	
	// Non-shuffled deck constructor
	public Dealer(boolean isShuffled) 
	{
		
		this.theDeck = new Deck(isShuffled);
		
	}// end Dealer()
	
	
	// Method to return a hand of three cards from the deck
	public ArrayList<Card> dealHand() 
	{
		
		ArrayList<Card> hand = new ArrayList<Card>();
		
		for(int i = 0; i < 3; i++) {
			
			// Remove the front card off the deck, then add it to hand
			Card c = this.getDeck().remove(0);
			hand.add(c);
			
		}
		
		return hand;
		
	}// end dealHand()
	
	
	// Method to renew the deck when there are 34 or less cards in the deck
	public void renewDeck()
	{
		
		if (this.theDeck.size() <= 34) {
			
			this.theDeck.newDeck();
			
		}
		
	}// end renewDeck()
	

	// Getter for theDeck
	public Deck getDeck() { return this.theDeck; }

	// Getter/Setter for dealersHand
	public ArrayList<Card> getDealersHand() { return this.dealersHand; }
	public void setDealersHand(ArrayList<Card> dealersHand) { this.dealersHand = dealersHand; }
	
}
