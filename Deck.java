import java.util.ArrayList;
import java.util.Collections;

/*  CS 342 - Project 2
 *  Deck.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class is the data structure (an array list) that holds a full deck of unique cards.
 */

public class Deck extends ArrayList<Card> {

	private boolean startShuffled = true;
	
	// Constructor
	public Deck() 
	{
		
		this.newDeck();	
	
	}// end Deck()
	
	
	// Constructor to determine whether to start shuffled or not
	public Deck(boolean startShuffled) 
	{
		
		this.startShuffled = startShuffled;
		this.newDeck();
		
	}// end Deck()
	
	
	// Method to clear up the entire deck and start anew
	public void newDeck() 
	{
		
		// Empty the list if it is not already
		if (this.size() != 0) {
			this.clear();
		}
		
		// Start from i = 2 and go to 14. This represents the value
		for(int i = 2; i <= 14; i++) {
			
			// Start from j = 0 up to and including 3. This represents the suit
			for(int j = 0; j < 4; j++) {
				
				// Decide the suit based on j
				char c;
				switch(j) {
				case 0: c = 'C'; break;
				case 1: c = 'D'; break;
				case 2: c = 'H'; break;
				default: c = 'S'; break;
				}
				
				// Create a new card based on c and i and add it
				Card newCard = new Card(c, i);
				this.add(newCard);
				
			}
			
		}
		
		// Now that everything's added, shuffle it
		if (startShuffled) {
			Collections.shuffle(this);
		}
		
	}// end newDeck()
	
}
