import java.util.ArrayList;
import java.util.Collections;

/*  CS 342 - Project 2
 *  ThreeCardLogic.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class defines methods to how the game plays out. Such as when one hand is greater than the other
 */

public class ThreeCardLogic {

	// Methods
	
	public static int evalHand(ArrayList<Card> hand) 
	{
		
		// Start by sorting the hand from least to greatest
		Collections.sort(hand);
		
		if (isStraightFlush(hand)) {
			
			return 1;
		
		}else if (isThreeOfAKind(hand)) {
			
			return 2;
			
		}else if (isStraight(hand)) {
			
			return 3;
			
		}else if (isFlush(hand)) {
			
			return 4;
			
		}else if (isPair(hand)) {
			
			return 5;
			
		}
		
		// Otherwise there's nothing special about the hand
		return 0;
		
	}// end evalHand()
	
	
	public static String getHandType(ArrayList<Card> hand)
	{
		
		if (hand != null) {
			
			// Start by sorting the hand from least to greatest
			Collections.sort(hand);
			
			if (isStraightFlush(hand)) {
				
				return "Straight Flush";
			
			}else if (isThreeOfAKind(hand)) {
				
				return "Three of a Kind";
				
			}else if (isStraight(hand)) {
				
				return "Straight";
				
			}else if (isFlush(hand)) {
				
				return "Flush";
				
			}else if (isPair(hand)) {
				
				return "Pair";
				
			}
			
			// Otherwise, we return "x-high" where x is the value of the highest card
			int highestVal = highestValue(hand);
			
			return Card.getValString(highestVal) + "-High";
		
		}else{
			
			// If the hand is null (empty) then return "???"
			return "???";
			
		}
		
	}
	
	
	public static String getHandString(ArrayList<Card> hand)
	{
		
		String s = "";
		
		if (hand != null) {
			for(int i = 0; i < hand.size(); i++) {
				s += "[" + hand.get(i) + "] ";
			}
		}else {
			
			s = "[] [] []";
			
		}
	
		return s;
		
	}// end getHandString()
	
	
	public static int evalPPWinnings(ArrayList<Card> hand, int bet) 
	{
		
		int handKind = evalHand(hand);
		
		switch(handKind) {
		case 1: return 40 * bet; // straight flush
		case 2: return 30 * bet; // three of a kind
		case 3: return 6 * bet;  // straight
		case 4: return 3 * bet;  // flush
		case 5: return 1 * bet;  // pair
		default:
				return 0;        // U LOSE
		}
		
	}// end evalPPWinnings()
	
	
	public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player)
	{
		
		// Get the ranking of both hands
		int dRank = evalHand(dealer);
		int pRank = evalHand(player);
		
		// Compare dRank and pRank
		if (dRank != 0 && pRank != 0) {
			
			// Both dealer and player have a special hand
			if (pRank < dRank) {
				return 2;
			}else if (dRank < pRank) {
				return 1;
			}else{

				// If they're both a pair, then compare which pair is higher
				if (dRank == 5 && pRank == 5) {
					
					return comparePairValues(dealer, player);
					
				}else {
				
					// Compare which hand has the higher max
					return compareHandValues(dealer, player);
				
				}
			}
			
		}else if (dRank != 0 && pRank == 0) {
			
			// Dealer automatically wins
			return 1;
			
		}else if (dRank == 0 && pRank != 0) {
		
			// Dealer has a no-name rank, but player has a special rank. Player wins
			return 2;
			
		}else if (dRank == 0 && pRank == 0) {
			
			// Compare which hand has the higher max
			return compareHandValues(dealer, player);
			
		}
		
		return 0;
		
	}// end compareHands()
	
	
	// Method to decide if the dealer's hand qualifies
	public static boolean dealerQualifies(ArrayList<Card> dealer) 
	{
		
		int handRank = evalHand(dealer);
		
		if (handRank == 0) {
			
			// If the highest card is a queen or more
			if (highestValue(dealer) >= 12) {
				return true;
			}
			
			// The hand has no special rank, and is not a queen or more.
			return false;
			
		}else{
			
			// The hand has a special rank
			return true;
			
		}
		
	}// end dealerQualifies()
	
	// Helper methods
	
	// Method to compare the hands based on the card values. Highest gets compared first, then second highest, then third highest
	public static int compareHandValues(ArrayList<Card> dealer, ArrayList<Card> player)
	{
		
		// Start by sorting both
		Collections.sort(dealer);
		Collections.sort(player);
		
		// Compare the highest valued cards, which are the last in the list.
		if (dealer.get(2).getValue() > player.get(2).getValue()) {
			return 1;
		}else if (player.get(2).getValue() > dealer.get(2).getValue()) {
			return 2;
		}else{
			
			// Highest cards are the same! Compare the second highest cards
			if (dealer.get(1).getValue() > player.get(1).getValue()) {
				return 1;
			}else if (player.get(1).getValue() > dealer.get(1).getValue()) {
				return 2;
			}else{
				
				// Second highest cards are the same! Compare the smallest cards
				if (dealer.get(0).getValue() > player.get(0).getValue()) {
					return 1;
				}else if (player.get(0).getValue() > dealer.get(0).getValue()) {
					return 2;
				}else{
					
					return 0;
					
				}
				
			}
			
		}
		
	}// end compareHandValues()
	
	
	// Method to determine which hand (assuming both have a pair) has the better pair
	public static int comparePairValues(ArrayList<Card> dealer, ArrayList<Card> player)
	{
		
		// Get the pair value for dealer
		int dealerPairValue;
		int d1 = dealer.get(0).getValue();
		int d2 = dealer.get(1).getValue();
		int d3 = dealer.get(2).getValue();
		
		if (d1 == d2) {
			dealerPairValue = d1;
		}else if (d2 == d3) {
			dealerPairValue = d2;
		}else{ dealerPairValue = d2; }
		
		// Get the pair value for the player
		int playerPairValue;
		int p1 = player.get(0).getValue();
		int p2 = player.get(1).getValue();
		int p3 = player.get(2).getValue();
		
		if (p1 == p2) {
			playerPairValue = p1;
		}else if (p2 == p3) {
			playerPairValue = p2;
		}else{ playerPairValue = p2; }
		
		// Now finally compare them
		if (playerPairValue > dealerPairValue) {
			return 2;
		}else if (dealerPairValue > playerPairValue) {
			return 1;
		}else {
			return 0;
		}
		
	}// end comparePairValues()
	
	
	// Method to see if the hand is a straight (values of the form x, x + 1, x + 2)
	// Assumes the hand is already in sorted order
	public static boolean isStraight(ArrayList<Card> hand)
	{
		
		int x = hand.get(0).getValue();
		int y = hand.get(1).getValue();
		int z = hand.get(2).getValue();
		
		if (y == (x + 1) && z == (x + 2)) {
			return true;
		}

		return false;
		
	}// end isStraight()
	
	
	// Method to see if the hand is a flush (all three cards have the same suit)
	public static boolean isFlush(ArrayList<Card> hand) 
	{
		
		char a = hand.get(0).getSuit();
		char b = hand.get(1).getSuit();
		char c = hand.get(2).getSuit();
		
		if (b == a && c == a) {
			return true;
		}
		
		return false;
		
	}// end isFlush()
	
	
	// Method to see if the hand contains a pair (contains a repeated value)
	public static boolean isPair(ArrayList<Card> hand)
	{
		
		int x = hand.get(0).getValue();
		int y = hand.get(1).getValue();
		int z = hand.get(2).getValue();
		
		if (x == y || y == z || z == x) {
			return true;
		}
		
		return false;
		
	}// end isPair()
	
	
	// Method to see if the hand is three-of-a-kind (all three have the same value)
	public static boolean isThreeOfAKind(ArrayList<Card> hand)
	{
		
		int x = hand.get(0).getValue();
		int y = hand.get(1).getValue();
		int z = hand.get(2).getValue();
		
		if (x == y && y == z && z == x) {
			return true;
		}
		
		return false;
		
	}// end isThreeOfAKind()
	
	
	// Method to see if the hand is a straight-flush (isStraight and isFlush)
	public static boolean isStraightFlush(ArrayList<Card> hand)
	{
		
		return (isStraight(hand) && isFlush(hand));
		
	}// end isStraightFlush()
	
	
	// Method to determine the highest value in the hand
	public static int highestValue(ArrayList<Card> hand)
	{
		
		int max = hand.get(0).getValue();
		
		for(int i = 1; i < 3; i++) {
			if (hand.get(i).getValue() > max) {
				max = hand.get(i).getValue();
			}
		}
		
		return max;
		
	}// end highestValue()
	
}
