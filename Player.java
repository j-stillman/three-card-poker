import java.util.ArrayList;

/*  CS 342 - Project 2
 *  Player.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class holds player info, such as their current hand, total winnings, and bets.
 */

public class Player {

	// Fields
	ArrayList<Card> hand;
	int anteBet;
	int playBet;
	int pairPlusBet;
	int totalWinnings;
	int balance;
	
	// Conditions of the current game.
	boolean ready = false;    // Becomes true when the player enters their bets
	boolean folded = false;
	boolean played = false;
	
	// Constructor
	public Player() 
	{
		
		this.setHand(null);
		this.setAnte(0);
		this.setPlay(0);
		this.setPairPlus(0);
		this.setWinnings(0);
		this.setBalance(100);
		
	}// end Player()
	
	
	// Method to reset all player values
	public void reset()
	{
		
		this.setHand(null);
		this.setAnte(0);
		this.setPlay(0);
		this.setPairPlus(0);
		this.setWinnings(0);
		this.setBalance(100);
		
		this.setReady(false);
		this.setPlayed(false);
		this.setFolded(false);
		
	}// end reset()
	
	
	// Getter/Setter for hand
	public ArrayList<Card> getHand() { return this.hand; }
	public void setHand(ArrayList<Card> hand) { this.hand = hand; }
	
	// Getter/Setter for anteBet
	public int getAnte() { return this.anteBet; }
	public void setAnte(int anteBet) { this.anteBet = anteBet; }
	
	// Getter/Setter for playBet
	public int getPlay() { return this.playBet; }
	public void setPlay(int playBet) { this.playBet = playBet; }
	
	// Getter/Setter for pairPlusBet
	public int getPairPlus() { return this.pairPlusBet; }
	public void setPairPlus(int pairPlusBet) { this.pairPlusBet = pairPlusBet; }
	
	// Getter/Setter for totalWnnings
	public int getWinnings() { return this.totalWinnings; }
	public void setWinnings(int totalWinnings) { this.totalWinnings = totalWinnings; }
	
	// Getter/Setter for balance
	public int getBalance() { return this.balance; }
	public void setBalance(int balance) { this.balance = balance; }
	
	// Getter/Setter for ready
	public boolean getReady() { return this.ready; }
	public void setReady(boolean ready) { this.ready = ready; }
	
	// Getter/Setter for played
	public boolean getPlayed() { return this.played; }
	public void setPlayed(boolean played) { this.played = played; }
	
	// Getter/Setter for folded
	public boolean getFolded() { return this.folded; }
	public void setFolded(boolean folded) { this.folded = folded; }
	
	
	// Method to override toString() so that we can print the player info
	public String toString()
	{
		
		String s = "";
		
		s += "Player info: \n";
		s += "    balance:       " + this.getBalance() + "\n";
		s += "    winnings:      " + this.getWinnings() + "\n";
		s += "    ante bet:      " + this.getAnte() + "\n";
		s += "    pair plus bet: " + this.getPairPlus() + "\n";
		s += "    play bet:      " + this.getPlay() + "\n";
		s += "    hand:          ";
		
		if (this.getHand() != null) {
			for(int i = 0; i < this.getHand().size(); i++) {
				s += "[" + this.getHand().get(i) + "] ";
			}
		}
		
		s += "\n";
		
		return s;
		
	}// end toString()
	
}
