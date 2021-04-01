
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*  CS 342 - Project 2
 *  PlayerOptions.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class holds a bunch of buttons and text that the player uses to change bets and such
 */

public class PlayerOptions extends BorderPane {

	// Fields
	private ThreeCardPokerGame gameLogic;
	private Player myPlayer; // The player where all this info comes from
	
	private boolean enabled; // Whether we can click and change stuff
	private boolean ready;   // Whether we have clicked a button here
	private boolean played;  // Whether we chose to play
	private boolean folded;  // Whether we chose to fold
	
	// Labels that display the player's stats
	private Label balanceVal, winningsVal, playBetVal;
	
	// Text fields for ante bet and pair plus bet
	private TextField anteField, pairPlusField;
	
	// Buttons for play and fold
	private Button readyButton, playButton, foldButton;
	
	// BorderPane that holds everything neatly
	private BorderPane mainPane;
	
	// Alert box used when you don't enter the right values
	private Alert invalidInputAlert;
	
	// Constructor
	public PlayerOptions(ThreeCardPokerGame gameLogic, Player myPlayer)
	{
		
		super();
		
		this.setGameLogic(gameLogic);
		this.myPlayer = myPlayer; 
		
		// Create the error alert
		invalidInputAlert = new Alert(
				AlertType.ERROR, 
				"Invalid input.\n" + 
				"Ante bets must be anywhere from " + ThreeCardPokerGame.MIN_BET + " to " + ThreeCardPokerGame.MAX_BET + ".\n" +
				"Pair-Plus bets must be either 0 or anywhere from " + ThreeCardPokerGame.MIN_BET + " to " + ThreeCardPokerGame.MAX_BET + ".");
		
		// Construct the right-side components
		balanceVal = new Label("" + myPlayer.getBalance());
		winningsVal = new Label("" + myPlayer.getWinnings());
		playBetVal = new Label("" + myPlayer.getPlay());
		
		anteField = new TextField();
		pairPlusField = new TextField();
		defineTextFields();
		
		// Construct the bottom components
		readyButton = new Button("Ready");
		playButton = new Button("Play");
		foldButton = new Button("Fold");
		defineButtons();
		
		HBox buttonsBox = new HBox(readyButton, playButton, foldButton);
		buttonsBox.setPadding(new Insets(0, 5, 0, 0));
		buttonsBox.setSpacing(8);
		
		// Create a borderpane for every single section. They look nicest.
		BorderPane balanceSection = new BorderPane();
		balanceSection.setLeft(new Label("Balance: "));
		balanceSection.setRight(balanceVal);
		
		BorderPane winningsSection = new BorderPane();
		winningsSection.setLeft(new Label("Winnings: "));
		winningsSection.setRight(winningsVal);
		
		BorderPane pplusSection = new BorderPane();
		pplusSection.setLeft(new Label("Pair-Plus: "));
		pplusSection.setRight(pairPlusField);
		
		BorderPane anteSection = new BorderPane();
		anteSection.setLeft(new Label("Ante: "));
		anteSection.setRight(anteField);
		
		BorderPane playBetSection = new BorderPane();
		playBetSection.setLeft(new Label("Play Bet: "));
		playBetSection.setRight(playBetVal);
		
		VBox verticalList = new VBox(
				balanceSection,
				winningsSection,
				pplusSection,
				anteSection,
				playBetSection,
				buttonsBox
		);
		
		verticalList.setSpacing(4);
		
		// Put it all together (we inherit from BorderPane)
		this.setCenter(verticalList);
		
		this.setEnabled(true);
		this.setReady(false);
		this.setPlayed(false);
		this.setFolded(false);
		
	}// end PlayerOptions()
	
	
	// Method to define what the buttons do
	public void defineButtons()
	{
		
		readyButton.setOnAction(e -> {
			
			if (validInput(anteField, pairPlusField)) {
				
				// Update the player to be ready
				updatePlayerAnte();
				updatePlayerPairPlus();
				
				myPlayer.setReady(true);
				
				// Disable this button
				readyButton.setDisable(true);
				
				// Call on gameLogic to try and update the gameState
				gameLogic.updateGameState();
				
				
			}else {
				
				invalidInputAlert.show();
				
			}
			
		});
		
		playButton.setOnAction(e -> {
			
			if (validInput(anteField, pairPlusField)) {
				
				// Update the player to be playing
				updatePlayerAnte();
				updatePlayerPairPlus();
				
				myPlayer.setPlayed(true);
				
				// Disable both buttons
				playButton.setDisable(true);
				foldButton.setDisable(true);
				
				// Call on gameLogic to try and update the gameState
				gameLogic.updateGameState();
				
			}else{
				
				invalidInputAlert.show();
				
			}
			
		});
		
		foldButton.setOnAction(e -> {
			
			if (validInput(anteField, pairPlusField)) {
		
				// Update the player to be folded
				updatePlayerAnte();
				updatePlayerPairPlus();
				
				// Set play bet to 0 since we folded
				myPlayer.setPlay(0);
				updateAllLabels();
				
				myPlayer.setFolded(true);
				
				// Disable both buttons
				playButton.setDisable(true);
				foldButton.setDisable(true);
				
				// Call on gameLogic to try and update the gameState
				gameLogic.updateGameState();
				
			}else{
				
				invalidInputAlert.show();
				
			}
			
		});
		
	}// end defineButtons()
	
	
	// Method to define the text fields
	public void defineTextFields()
	{
		
		anteField.setOnAction(e -> {
			
			String input = anteField.getText();
			
			if (validInput(input)) {
				
				updatePlayerAnte();
				
			}else{
				
				invalidInputAlert.show();
				
			}
			
		});
		
		pairPlusField.setOnAction(e -> {
			
			String input = pairPlusField.getText();
			boolean isZero = false;
			if (validString(input)) {
				
				if (Integer.parseInt(input) == 0) {
					isZero = true;
				}
				
			}
			
			if (isZero || validInput(input)) {
				
				updatePlayerPairPlus();
				
			}else{
				
				invalidInputAlert.show();
				
			}
			
		});
		
		anteField.setAlignment(Pos.CENTER_RIGHT);
		pairPlusField.setAlignment(Pos.CENTER_RIGHT);
		anteField.setPrefColumnCount(4);
		pairPlusField.setPrefColumnCount(4);
		
	}// end defineTextFields()
	
	
	// Method to reflect changes of the ante text field to the player. Also updates the labels
	private void updatePlayerAnte()
	{
		
		int anteVal = Integer.parseInt(anteField.getText());
		
		myPlayer.setAnte(anteVal);
		myPlayer.setPlay(anteVal);
		
		updateAllLabels();
		
	}// end updatePlayerAnte()
	
	
	// Method to reflect changes of the pplus text field to the player. Also updates the labels
	private void updatePlayerPairPlus()
	{
		
		int pplusVal = Integer.parseInt(pairPlusField.getText());
		
		myPlayer.setPairPlus(pplusVal);
		
		updateAllLabels();
		
	}// end updatePlayerAnte()
	
	// Method to look at the player's stats and refresh them on the labels
	public void updateAllLabels()
	{
		
		balanceVal.setText("" + myPlayer.getBalance());
		winningsVal.setText("" + myPlayer.getWinnings());
		//pairPlusField.setText("" + myPlayer.getPairPlus());
		//anteField.setText("" + myPlayer.getAnte());
		playBetVal.setText("" + myPlayer.getPlay());
		
	}// end updateAllLabels()
	
	
	// Method to change the state of the options pane. For instance, disables and enables buttons to match
	// the given gameState
	public void updateState(int state)
	{
		
		switch(state)
		{
		
		case 0: 
			
			// Textfields enabled, ready button enabled, play/fold buttons disabled
			anteField.setEditable(true);
			pairPlusField.setEditable(true);
			readyButton.setDisable(false);
			
			playButton.setDisable(true);
			foldButton.setDisable(true);
			
			break;
			
		case 1: 
			
			// Textfields, disabled, ready button disabled, play/fold buttons enabled
			anteField.setEditable(false);
			pairPlusField.setEditable(false);
			readyButton.setDisable(true);
			
			playButton.setDisable(false);
			foldButton.setDisable(false);
			
			break;
			
		case 2: 
			
			// 
			
			break;
		default:
		
		}
		
	}// end updateState()
	
	
	// Getter/Setter for enabled
	public boolean getEnabled() { return this.enabled; }
	public void setEnabled(boolean enabled) { 
		
		this.enabled = enabled; 
	
		// Disable or enable the buttons to match with the behavior
		anteField.setEditable(enabled);
		pairPlusField.setEditable(enabled);
		playButton.setDisable(!enabled);
		foldButton.setDisable(!enabled);
		
	}
	
	
	// Helper method to determine if input is valid
	// Credit: https://www.baeldung.com/java-check-string-number
	private boolean validString(String s) 
	{
		
		if (s == null) {
			return false;
		}
		
		try {
			double d = Double.parseDouble(s);
		}catch(NumberFormatException e) {
			return false;
		}
		
		return true;
		
	}// end validString()
	
	
	// Helper method to see if the INPUT is valid. (ie valid string and within a certain range)
	private boolean validInput(String s) 
	{
		
		if (validString(s)) {
		
			// Since the string is a valid number, check that it is in the right range
			int val = Integer.parseInt(s);
			if (val < ThreeCardPokerGame.MIN_BET || val > ThreeCardPokerGame.MAX_BET) {
				
				return false;
				
			}
			
			return true;
		
		}
		
		return false;
		
	}// end validInput()
	
	private boolean validInput(TextField t1, TextField t2) 
	{
		
		// Give a special treatment to t2, because it can also be 0
		boolean t1Good = validInput(t1.getText());
		boolean t2Good = validString(t2.getText());
		if (t2Good) {
			if (Integer.parseInt(t2.getText()) == 0 || validInput(t2.getText())) {
				t2Good = true;
			}else {
				t2Good = false;
			}
		}
		
		return t1Good && t2Good;
		
	}// end validInput()
	
	
	// Getter/Setter for gameLogic
	public ThreeCardPokerGame getGameLogic() { return this.gameLogic; }
	public void setGameLogic(ThreeCardPokerGame gameLogic) { this.gameLogic = gameLogic; }
	
	// Getter/Setter for played
	public boolean getPlayed() { return this.played; }
	public void setPlayed(boolean played) { this.played = played; }
	
	// Getter/Setter for folded
	public boolean getFolded() { return this.folded; }
	public void setFolded(boolean folded) { this.folded = folded; }
	
	// Getter/Setter for ready
	public boolean getReady() { return this.ready; }
	public void setReady(boolean ready) { 
		
		this.ready = ready; 
		
		// Also, re-enable the panel if we are ready again
		if (ready) {
			this.setEnabled(true);
		}
	
	}
	
}
