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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*  CS 342 - Project 2
 *  HandViewer.java
 *  Author: Jeremiah Stillman
 *  Date: 03/07/20
 *  
 *  This class displays a player's hand with some images along with the info about what kind of hand it is
 */

public class HandViewer extends BorderPane {

	// Fields
	private Player myPlayer;
	private Dealer myDealer;
	
	private ArrayList<Card> myHand;
	private ArrayList<Image> defaultCardsImages;
	private ArrayList<Image> defaultCardsImages2;
	private ArrayList<ImageView> handImages;
	
	private HBox handRow;
	private Insets handRowPadding = new Insets(10, 10, 10, 10);
	private int handRowSpacing = 16;
	private Label handTitle, handRank;	
	private int nameFontSize = 24;
	private Font myFont1 = new Font(nameFontSize);
	
	// Constructor
	public HandViewer(Player myPlayer, String playerName) 
	{
		
		super();
		
		this.myPlayer = myPlayer;
		
		handTitle = new Label(playerName);
		handRank = new Label("???");
		handTitle.setFont(myFont1);
		handRank.setFont(myFont1);
		
		// Create the list of card images
		defaultCardsImages = new ArrayList<Image>();
		defaultCardsImages.add(new Image("cards/cardBack.png"));
		defaultCardsImages.add(new Image("cards/cardBack.png"));
		defaultCardsImages.add(new Image("cards/cardBack.png"));
		
		defaultCardsImages2 = new ArrayList<Image>();
		defaultCardsImages2.add(new Image("cards2/cardBack.png"));
		defaultCardsImages2.add(new Image("cards2/cardBack.png"));
		defaultCardsImages2.add(new Image("cards2/cardBack.png"));
		
		// Create the image views for each card
		handImages = new ArrayList<ImageView>();
		for(Image i : defaultCardsImages) {
			ImageView img = new ImageView(i);
			img.setFitWidth(64);
			img.setFitHeight(96);
			img.setPreserveRatio(true);
			handImages.add(img);
		}
		
		handRow = new HBox(handImages.get(0), handImages.get(1), handImages.get(2));
		handRow.setPadding(handRowPadding);
		handRow.setSpacing(handRowSpacing);
		
		VBox verticalList = new VBox(handTitle, handRow, handRank);
		verticalList.setAlignment(Pos.CENTER);
		
		// Put it all together
		this.setCenter(verticalList);
		
	}// end HandViewer()
	
	
	// Constructor that a dealer would use. Purely redundant. I was rushing. Sorry :(
	public HandViewer(Dealer myDealer, String dealerName)
	{
		
		super();
		
		this.myDealer = myDealer;
		
		handTitle = new Label(dealerName);
		handRank = new Label("RANK OF HAND");
		handTitle.setFont(myFont1);
		handRank.setFont(myFont1);
		
		// Create the list of card images
		defaultCardsImages = new ArrayList<Image>();
		defaultCardsImages.add(new Image("cards/cardBack.png"));
		defaultCardsImages.add(new Image("cards/cardBack.png"));
		defaultCardsImages.add(new Image("cards/cardBack.png"));
		
		defaultCardsImages2 = new ArrayList<Image>();
		defaultCardsImages2.add(new Image("cards2/cardBack.png"));
		defaultCardsImages2.add(new Image("cards2/cardBack.png"));
		defaultCardsImages2.add(new Image("cards2/cardBack.png"));
		
		// Create the image views for each card
		handImages = new ArrayList<ImageView>();
		for(Image i : defaultCardsImages) {
			ImageView img = new ImageView(i);
			img.setFitWidth(64);
			img.setFitHeight(96);
			img.setPreserveRatio(true);
			handImages.add(img);
		}
		
		handRow = new HBox(handImages.get(0), handImages.get(1), handImages.get(2));
		handRow.setPadding(handRowPadding);
		handRow.setSpacing(handRowSpacing);
		
		VBox verticalList = new VBox(handTitle, handRow, handRank);
		verticalList.setAlignment(Pos.TOP_CENTER);
		
		// Put it all together
		this.setCenter(verticalList);
		
	}
	
	
	// Method that updates the hand that is shown
	public void updateState(int state)
	{
		
		switch(state)
		{
		
		case 0: 
			
			// The hand should not be seen regardless if it is a player or dealer
			myHand = null;
			
			updateHandView(myHand);
			
			break;
		case 1: 
			
			// The player's hand should be in view but not the dealer
			if (myPlayer != null) {
				myHand = myPlayer.getHand();
			}else {
				myHand = null;
			}
			
			updateHandView(myHand);
			
			break;
			
		case 2: 
			
			// Both player and dealer have their hands shown
			if (myPlayer != null) {
				myHand = myPlayer.getHand();
			}else if (myDealer != null) {
				myHand = myDealer.getDealersHand();
			}
			
			updateHandView(myHand);	
			
			break;
		default:
		
		}
		
		
		
	}// end updateState
	
	
	// Method to update what is shown based on the hand (if the hand is null or it is shown)
	public void updateHandView(ArrayList<Card> hand)
	{
		
		if (hand != null) {

			// Obtain the image names by matching with the value and suit of the card
			for(int i = 0; i < 3; i++) {
				
				String filename;
				if (ThreeCardPokerGame.theme == 1) {
					filename = "cards2/";
				}else {
					filename = "cards/";
				}
				
				int cardVal = hand.get(i).getValue();
				char cardSuit = hand.get(i).getSuit();
				
				filename = filename.concat("" + cardVal);
				filename = filename.concat("" + cardSuit);
				filename = filename.concat(".png");
				
				handImages.get(i).setImage(new Image(filename));
				
			}
			
		}else {
			
			// Figure out what the default back is based on theme
			ArrayList<Image> defaults = defaultCardsImages;
			
			if (ThreeCardPokerGame.theme == 1) {
				defaults = defaultCardsImages2;
			}
			
			// Just set the card images to the default back
			for(int i = 0; i < 3; i++) {
				
				handImages.get(i).setImage(defaults.get(i));
				
			}
			
		}
		
		// Set the text that displays what kind of hand it is
		handRank.setText(ThreeCardLogic.getHandType(hand));
		
	}// end updateHandView()
	
}
