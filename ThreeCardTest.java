import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ThreeCardTest {

	ArrayList<Card> h1;
	ArrayList<Card> h2;
	ArrayList<Card> h3;
	ArrayList<Card> h4;
	
	@BeforeEach
	void init() {
		
		h1 = new ArrayList<Card>();
		h2 = new ArrayList<Card>();
		h3 = new ArrayList<Card>();
		h4 = new ArrayList<Card>();
		
		// Straight flush hand
		h1.add(new Card('C', 2));
		h1.add(new Card('C', 3));
		h1.add(new Card('C', 4));
		
		// Arbitrary hand 1
		h2.add(new Card('H', 5));
		h2.add(new Card('S', 7));
		h2.add(new Card('S', 11));
		
		// Arbitrary hand 2
		h3.add(new Card('D', 6));
		h3.add(new Card('H', 7));
		h3.add(new Card('S', 11));
		
		// Flush hand
		h4.add(new Card('C', 4));
		h4.add(new Card('C', 6));
		h4.add(new Card('C', 8));
		
	}

	@Test
	void testStraight() {
		
		assertTrue(ThreeCardLogic.isStraight(h1), "h1 should be a straight.");
		
	}
	
	@Test
	void testFlush() {
		
		assertTrue(ThreeCardLogic.isFlush(h1), "h1 should be a flush.");
		
	}
	
	@Test
	void testStraightFlush() {
		
		assertTrue(ThreeCardLogic.isStraightFlush(h1), "h1 should be a straight flush.");
		
	}
	
	@Test
	void testPair() {
		
		h1.clear();
		h1.add(new Card('S', 12));
		h1.add(new Card('H', 12));
		h1.add(new Card('D', 6));
		assertTrue(ThreeCardLogic.isPair(h1), "h1 should be a pair (12S, 12H, 6D)");
		
	}
	
	@Test
	void testThreeOfAKind() {
		
		h1.clear();
		h1.add(new Card('S', 12));
		h1.add(new Card('H', 12));
		h1.add(new Card('D', 12));
		
		assertTrue(ThreeCardLogic.isThreeOfAKind(h1), "h1, all queens, should be three of a kind.");
		
	}
	
	@Test
	void testHighestValue() {
		
		int max = ThreeCardLogic.highestValue(h3);
		assertEquals(11, max, "highest valued card in h3 should be 11, a jack..");
		
	}
	
	@Test
	void testCompareHands() {
		
		// Test when two hands have a pair, and determining which pair is better
		h1.clear();
		h2.clear();
		h1.add(new Card('C', 12));
		h1.add(new Card('H', 12));
		h1.add(new Card('D', 8));
		
		h2.add(new Card('D', 12));
		h2.add(new Card('H', 8));
		h2.add(new Card('C', 8));
		
		assertEquals(1, ThreeCardLogic.compareHands(h1, h2), "h1 (12-12-8) should be better than h2 (12-8-8)");
		
	}

	@Test
	void testEvalHand() {
		
		int hand1 = ThreeCardLogic.evalHand(h1);
		int hand2 = ThreeCardLogic.evalHand(h2);
		int hand3 = ThreeCardLogic.evalHand(h3);
		int hand4 = ThreeCardLogic.evalHand(h4);
		
		assertEquals(1, hand1, "hand 1 should be a straight flush");
		assertEquals(0, hand2, "hand 2 did not yield 0 for evalHand");
		assertEquals(0, hand3, "hand 3 did not yield 0 for evalHand");
		assertEquals(4, hand4, "hand 4 should be a flush.");
		
	}
	
	@Test
	void testCompare() {
		
		int result = ThreeCardLogic.compareHands(h4, h1);
		assertEquals(2, result, "Player straight flush did not win against dealer flush.");
		
	}
	
	@Test
	void testHighestValuedCard() {

		// Assume h2 is the player and h3 the dealer
		int result = ThreeCardLogic.compareHands(h3, h2);
		
		assertEquals(1, result, "Test: dealer 6-7-11 did not win against player 5-7-11");
		
		// Assume h3 the dealer and h2 the player
		result = ThreeCardLogic.compareHands(h2,  h3);
		assertEquals(2, result, "Test: player 6-7-11 did not win against dealer 5-7-11");
		
	}
	
	@Test
	void testGetHandType() {
		
		assertEquals("Straight Flush", ThreeCardLogic.getHandType(h1), "h1 should be named \"Straight Flush\".");
		assertEquals("Jack-High", ThreeCardLogic.getHandType(h2), "h2 should be named \"Jack-High\".");
		assertEquals("Jack-High", ThreeCardLogic.getHandType(h3), "h3 should be named \"Jack-High\".");
		assertEquals("Flush", ThreeCardLogic.getHandType(h4), "h4 should be named \"Flush\".");

		
	}
	
	@Test
	void testEvalPPWinnings() {
		
		int bet = 1;
		int result = ThreeCardLogic.evalPPWinnings(h1, bet);
		assertEquals(40, result, "h1, a straight flush, should return 40 when betting 1.");
		result = ThreeCardLogic.evalPPWinnings(h2, bet);
		assertEquals(0, result, "h2 should return 0");
		
		result = ThreeCardLogic.evalPPWinnings(h4, bet);
		assertEquals(3, result, "h4, a flush, should return 3 when betting 1");
		
	}
	
	@Test
	void testDealerQualifies() {
		
		assertTrue(ThreeCardLogic.dealerQualifies(h1), "h1, a straight flush, should qualify if it were the dealer's hand.");
		assertFalse(ThreeCardLogic.dealerQualifies(h2), "h2, a Jack-High, shouldn't qualify.");
		
	}
	
}
