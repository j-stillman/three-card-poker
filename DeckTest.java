import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DeckTest {

	Deck d;
	
	@BeforeEach
	void init() {
		
		d = new Deck(false);
		
	}
	
	@Test
	void testInitialized() {
		
		assertEquals("Deck", d.getClass().getName(), "Deck d should be defined as a Deck");
		assertEquals(52, d.size(), "newly created deck did not have 52 elements");
		
	}
	
	@Test
	void testElementTypes() {
		
		for(int i = 0; i < d.size(); i++)
			assertEquals("Card", d.get(i).getClass().getName(), "Deck d should be a deck of Card instances.");
		
	}
	
	@Test
	void testUniqueElements() {
		
		char[] suitsOrder = {'C', 'D', 'H', 'S'};
		int[] valsOrder = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
		
		int i = 0;
		int j = 0;
		
		for(Card c : d) {
			
			int val = c.getValue();
			char suit = c.getSuit();
			
			assertEquals(suitsOrder[j], suit, "incorrect suit in deck");
			assertEquals(valsOrder[i], val, "incorrect value in deck");
			
			j++;
			if (j > 3) {
				i++;
				j = 0;
			}
			
		}
		
	}
	
	
	
	
}
