import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DealerTest {
	
	Dealer dealer;
	
	@BeforeEach
	void initDealer() {
		
		dealer = new Dealer(false);
		
	}
	
	@Test
	void testInitialized() {
		
		assertEquals("Dealer", dealer.getClass().getName(), "Dealer not initialized properly.");
		
	}
	
	@Test
	void testDeckSize() {
		
		assertEquals(52, dealer.getDeck().size(), "Dealer's deck, when initialized, was not of size 52.");
		
	}
	
	@Test
	void testDealHand() {
		
		ArrayList<Card> hand1 = dealer.dealHand();
		ArrayList<Card> hand2 = dealer.dealHand();
		
		assertEquals(46, dealer.getDeck().size(), "Calling dealHand() twice did not reduce the amount of cards by 6");
		assertEquals("[Two of Clubs] [Two of Diamonds] [Two of Hearts] ", ThreeCardLogic.getHandString(hand1), "hand dealt did not have correct cards.");
		assertEquals("[Two of Spades] [Three of Clubs] [Three of Diamonds] ", ThreeCardLogic.getHandString(hand2), "hand dealt did not have correct cards.");
		
	}
	
	@Test
	void testDealToSelf() {
		
		dealer.setDealersHand(dealer.dealHand());
		
		assertEquals(49, dealer.getDeck().size(), "dealer did not reduce total cards by 3 when dealing to themselves");
		assertEquals("[Two of Clubs] [Two of Diamonds] [Two of Hearts] ", ThreeCardLogic.getHandString(dealer.getDealersHand()), "dealer's hand did not have correct cards");

	}
	
	@Test
	void testRenewDeck() {
		
		// Deal out enough hands to warrant renewing it
		for(int i = 0; i < 9; i++) {
			
			dealer.dealHand();
			
		}
		
		assertEquals(25, dealer.getDeck().size(), "9 hands dealt did not reduce the deck size to 25");
		
		dealer.renewDeck();
		
		assertEquals(52, dealer.getDeck().size(), "deck was not renewed to 52 cards after having less than 34 cards");
		
	}
	
}
