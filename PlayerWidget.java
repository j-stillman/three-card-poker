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
 *  PlayerWidget.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class is a container for the player's cards, and their options on what to bet
 */

public class PlayerWidget extends BorderPane {

	// Fields
	private ThreeCardPokerGame gameLogic; // A reference to the game logic so we can send changes upward
	private Player myPlayer;
	private boolean ready;
	
	private HandViewer handView;
	private PlayerOptions options;
	
	// Constructor
	public PlayerWidget(ThreeCardPokerGame gameLogic, Player myPlayer, String playerName)
	{
		
		super();
		
		this.setGameLogic(gameLogic);
		this.myPlayer = myPlayer;

		handView = new HandViewer(myPlayer, playerName);
		options = new PlayerOptions(this.getGameLogic(), myPlayer);
		
		this.setLeft(handView);
		this.setRight(options);
		this.setPadding(new Insets(0, 0, 32, 0));
		
	}// end PlayerWidget()
	
	
	// Method to set the state of the handviewer and the options
	public void updateWidget(int state)
	{
		
		options.updateState(state);
		handView.updateState(state);
		
	}// end updateWidget()
	
	
	// Method to reset the labels to match the player's current stats
	public void refreshStats()
	{
		
		options.updateAllLabels();
		handView.updateHandView(myPlayer.getHand());
		
	}// end refreshStats()
	
	
	// Getter/Setter for gameLogic
	public ThreeCardPokerGame getGameLogic() { return this.gameLogic; }
	public void setGameLogic(ThreeCardPokerGame gameLogic) { this.gameLogic = gameLogic; }
	
	// Getter/Setter for ready
	public boolean getReady() { return this.ready; }
	public void setReady(boolean ready) { this.ready = ready; }
	
}
