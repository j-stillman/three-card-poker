import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ThreeCardPokerGame extends Application {

	// Global constants
	public static final boolean GUI_GAME = true;
	public static final int MIN_BET = 5;
	public static final int MAX_BET = 25;
	
	// Characteristics
	public static int theme = 0;
	
	// Fields
	private Background mainBackground;
	int gameState = 0;
	Player playerOne, playerTwo;
	Dealer theDealer;
	private PlayerWidget playerOnePane, playerTwoPane;
	private HandViewer dealerPane;
	private BorderPane mainBorderPane;
	private MenuBar optionsMenu;
	private Button continueButton;
	private Alert playerOneResults, playerTwoResults;
	private String[] resultsMessage = {"", ""};
	
	private Stack<String> gameInfo;
	private ListView<String> infoPanel;
	private ObservableList<String> infoPanelList;
	
	// Main method
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (GUI_GAME) {
			
			launch(args);
		
		}else{
			
			System.out.println("Creating input scanner...");
			Scanner input = new Scanner(System.in);
			
			System.out.println("Creating players...");
			Player playerOne = new Player();
			Player playerTwo = new Player();
			
			System.out.println("Creating dealer...");
			Dealer theDealer = new Dealer();
			
			System.out.println("READY!\n");
			
			playInConsole(input, playerOne, playerTwo, theDealer);
			
		}
		
		
		
	}

	// GUI start method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Let's Play Three Card Poker!!!");
		
		// Create the info panel to the left
		infoPanel = createInfoPanel();
		
		// Create the options menu at the top
		optionsMenu = createOptionsMenu();
		
		// Create the button to go to the next round
		continueButton = createContinueButton();
		
		// Create the alerts that tell you if a player won or lost
		playerOneResults = new Alert(AlertType.INFORMATION);
		playerTwoResults = new Alert(AlertType.INFORMATION);
		
		// Create the players and dealer
		playerOne = new Player();
		playerTwo = new Player();
		theDealer = new Dealer();
		
		// Create the widgets for the players and the dealer
		playerOnePane = new PlayerWidget(this, playerOne, "PLAYER 1");
		playerTwoPane = new PlayerWidget(this, playerTwo, "PLAYER 2");
		dealerPane = new HandViewer(theDealer, "DEALER");
		
		// Assign the states of these widgets
		playerOnePane.updateWidget(0);
		playerTwoPane.updateWidget(0);
		dealerPane.updateState(0);
		
		// Construct the BorderPane to hold these widgets
		BorderPane widgetsPane = new BorderPane();
		
		BorderPane topComponents = new BorderPane();
		VBox continueButtonContainer = new VBox(continueButton);
		continueButtonContainer.setSpacing(32);
		continueButtonContainer.setPadding(new Insets(8, 0, 8, 400));
		dealerPane.setPadding(new Insets(16, 450, 0, 0));
		topComponents.setRight(dealerPane);
		
		widgetsPane.setTop(topComponents);
		
		HBox playersBox = new HBox(playerOnePane, playerTwoPane);
		VBox controlsList = new VBox(playersBox, continueButtonContainer);
		controlsList.setAlignment(Pos.CENTER);
		playersBox.setSpacing(32);
		playersBox.setPadding(new Insets(0, 96, 0, 32));
		widgetsPane.setBottom(controlsList);
		
		
		// Create the main border pane
		mainBorderPane = new BorderPane();
		
		changeMainBackground(theme);
		mainBorderPane.setTop(optionsMenu);
		mainBorderPane.setLeft(infoPanel);
		mainBorderPane.setRight(widgetsPane);
		
		Scene scene = new Scene(mainBorderPane, 1280, 720);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	
	// Method that creates the list view (info panel) that rests at the left of the screen
	public ListView<String> createInfoPanel()
	{
	
		// Create the game info stack
		gameInfo = new Stack<String>();
		
		// Create the list view and attach the observable list to it
		ListView<String> listView = new ListView<String>();
		infoPanelList = FXCollections.observableArrayList();
		
		listView.setItems(infoPanelList);
		
		return listView;
		
	}// end createInfoPanel()
	
	
	// Method that creates a button where we press it to go to the next game
	public Button createContinueButton()
	{
		
		
		Button button = new Button("Continue");
		
		button.setOnAction(e -> {
					
			resetGameState();
			
		});
		
		// Disable it since it is only allowed once a game has been completed
		button.setDisable(true);
		
		return button;
		
	}// end createContinueButton()
	
	
	// Method that pushes a new message onto the stack, and in the process updates the info panel
	public void updateInfo(String message)
	{
		
		gameInfo.push(message);

		// Obtain the observable list and remove everything from it
		infoPanel.getItems().removeAll(infoPanelList);
		infoPanelList.clear();
		
		// Now go through and re-add everything to this list
		Iterator<String> iter = gameInfo.iterator();
		while(iter.hasNext()) {
			infoPanelList.add(iter.next());
		}
		
		// Since I don't know why it's not listing it top first, I'm just gonna reverse this info list
		Collections.reverse(infoPanelList);
		
		infoPanel.setItems(infoPanelList);

	}// end updateInfo()
	
	
	// Method that creates the options menu on the top of the screen
	public MenuBar createOptionsMenu()
	{
		
		MenuBar theMenu = new MenuBar();
		
		Menu options = new Menu("Options");
		
		MenuItem freshStart = new MenuItem("Fresh Start");
		MenuItem clearInfo = new MenuItem("Clear Info Panel");
		MenuItem newLook = new MenuItem("New Look");
		MenuItem exitGame = new MenuItem("Exit");
		
		// Event in which "Fresh Start" is clicked
		freshStart.setOnAction(e -> {

			// Reset the player's values
			playerOne.reset();
			playerTwo.reset();
			
			// Reset the deck
			theDealer.setDealersHand(null);
			theDealer.renewDeck();
			
			// Go back to game state 0
			resetGameState();
			
			// Clear the info panel
			gameInfo.clear();
			
			// Obtain the observable list and remove everything from it
			infoPanel.getItems().removeAll(infoPanelList);
			infoPanelList.clear();

			infoPanel.setItems(infoPanelList);

		});
		
		// Event in which "Clear Info Panel" is clicked
		clearInfo.setOnAction(e -> {

			// Clear the message stack
			gameInfo.clear();
			
			// Obtain the observable list and remove everything from it
			infoPanel.getItems().removeAll(infoPanelList);
			infoPanelList.clear();

			infoPanel.setItems(infoPanelList);
			
		});
		
		// Event in which "New Look" is clicked
		newLook.setOnAction(e -> {
			
			// Toggle the theme
			if (theme == 0) {
				theme = 1;
			}else {
				theme = 0;
			}
			
			// Change all the cards to match the new theme
			playerOnePane.refreshStats();
			playerTwoPane.refreshStats();
			dealerPane.updateHandView(theDealer.getDealersHand());
			
			// Change the background to match the theme
			changeMainBackground(theme);
			
		});
		
		// Event in which "Exit" is clicked
		exitGame.setOnAction(e -> {
			
			Platform.exit();
			
		});
		
		
		options.getItems().addAll(freshStart, clearInfo, newLook, exitGame);
		theMenu.getMenus().addAll(options);
		
		return theMenu;
		
	}// end createOptionsMenu()
	
	
	// Method to change the background color of the game
	public void changeMainBackground(int theme) {
		
		BackgroundFill bgFill;
		if (theme == 1) {
			bgFill = new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY);
		}else {
			bgFill = new BackgroundFill(Color.LIME, CornerRadii.EMPTY, Insets.EMPTY);
		}
		
		mainBackground = new Background(bgFill);
		mainBorderPane.setBackground(mainBackground);
		
	}
	
	
	// Method that evaluates changes to the game state (called whenever a button is pressed usually)
	public void updateGameState()
	{
		
		// Look at gameState. Depending on the state, check the current conditions, and advance if allowed
		switch(gameState) 
		{
		
		case 0: 
			
			// See if both of the players have entered bets and pressed the "ready" button
			if (playerOne.getReady() && playerTwo.getReady()) {
				
				System.out.println("Both are ready");
				
				// Deal out the cards to both players
				playerOne.setHand(theDealer.dealHand());
				playerTwo.setHand(theDealer.dealHand());
				
				// Sort the hands immediately because otherwise they get sorted out of nowhere
				// and it looks weird.
				Collections.sort(playerOne.getHand());
				Collections.sort(playerTwo.getHand());
				
				// Notify the info panel
				updateInfo("Player 1 has received their hand.");
				updateInfo("Player 2 has received their hand.");
				
				// Update both widgets to reflect these changes
				playerOnePane.updateWidget(1);
				playerTwoPane.updateWidget(1);
				dealerPane.updateState(1);
				
				gameState = 1;
				
			}else {
				System.out.println("Not quite ready");
			}
			
			break;
		case 1: 
			
			// Create a pausetransition for when we are going to reset the game back to state 0
			PauseTransition resetStatePause = new PauseTransition(Duration.seconds(1));
			resetStatePause.setOnFinished(e -> {
				
				resetGameState();
				
			});
			
			// Create a pausetransition for when we are about to show the dealer's cards
			PauseTransition showDealerPause = new PauseTransition(Duration.seconds(1));
			showDealerPause.setOnFinished(e -> {
				
				// Give the dealer a hand
				theDealer.setDealersHand(theDealer.dealHand());
				
				// Again, sort the hand because it's gonna sort anyway and that changes how it looks
				Collections.sort(theDealer.getDealersHand());
				
				playerOnePane.updateWidget(2);
				playerTwoPane.updateWidget(2);
				dealerPane.updateState(2);
				
				// Now we evaluate the results of each play
				evaluatePlays();
				
				// Tell the user directly if a player won or lost
				playerOneResults.setContentText(resultsMessage[0]);
				playerTwoResults.setContentText(resultsMessage[1]);
				
				playerTwoResults.show();
				playerOneResults.show();
				
				
				// Update the widgets to reflect the player's earnings
				playerOnePane.refreshStats();
				playerTwoPane.refreshStats();
				
				// Enable the button to go to the next game
				continueButton.setDisable(false);
				
			});
			
			// See if both of the players have either played or folded
			if ((playerOne.getFolded() || playerOne.getPlayed()) && (playerTwo.getFolded() || playerTwo.getPlayed())) {
				
				System.out.println("Both have decided to play/fold");
				
				// Notify whether each player has played or folded
				if (playerOne.getFolded()) {
					updateInfo("Player 1 has folded!");
				}else if (playerOne.getPlayed()) {
					updateInfo("Player 1 will play!");
				}
				
				if (playerTwo.getFolded()) {
					updateInfo("Player 2 has folded!");
				}else if (playerTwo.getPlayed()) {
					updateInfo("Player 2 will play!");
				}
				
				// Wait two seconds before showing the dealer's cards, and then
				// afterward, it will enable the continue button so we can go to the next game
				showDealerPause.play();
				
				
			}else {
				
				System.out.println("Players haven't chosen whether to play/fold yet");
				
			}
			
			break;
		default:
		
		}
		
	}// end updateGameState()
	
	
	// Method to reset the game state back to 0 so we can play another round
	public void resetGameState()
	{
		
		// Set the players folded, played, and ready variables back to false
		playerOne.setReady(false);
		playerOne.setFolded(false);
		playerOne.setPlayed(false);
		playerTwo.setReady(false);
		playerTwo.setFolded(false);
		playerTwo.setPlayed(false);
		
		// Empty the player and dealer hands
		playerOne.setHand(null);
		playerTwo.setHand(null);
		theDealer.setDealersHand(null);
		
		// Update the widgets to reflect the player's earnings
		playerOnePane.refreshStats();
		playerTwoPane.refreshStats();
		
		// Change the buttons back to how they were in state 0
		playerOnePane.updateWidget(0);
		playerTwoPane.updateWidget(0);
		dealerPane.updateState(0);
		
		// Disable continue button before moving on. It has served its purpose
		continueButton.setDisable(true);
		
		// Renew the dealer's deck
		theDealer.renewDeck();
		
		// Provide a little divider in the info panel for the new game
		updateInfo("--------------------------------------------------");
		
		gameState = 0;
		updateGameState();
		
	}// end resetGameState()
	
	
	// Method to compare the hand of the players with the dealer and add to the winnings and so forth
	public void evaluatePlays()
	{
		
		// First reset the strings for the alert dialogues
		resultsMessage[0] = "";
		resultsMessage[1] = "";
		
		// Figure out if the dealer qualifies
		String dealerRank = ThreeCardLogic.getHandType(theDealer.getDealersHand());
		updateInfo("Dealer hand: " + dealerRank);
		
		if (ThreeCardLogic.dealerQualifies(theDealer.getDealersHand())) {
			
			updateInfo("Dealer does qualify.");
			
			// For each player that did not fold, pit their hand against the dealer
			if (!playerOne.getFolded()) {
				playerVsDealerGUI(playerOne, theDealer, 1);
			}else {
				playerOne.setBalance(playerOne.getBalance() - playerOne.getAnte() - playerOne.getPairPlus());
			}
			
			if (!playerTwo.getFolded()) {
				playerVsDealerGUI(playerTwo, theDealer, 2);
			}else {
				playerTwo.setBalance(playerTwo.getBalance() - playerTwo.getAnte() - playerTwo.getPairPlus());
			}
			
		}else{
			
			// If the dealer does not qualify, then the 
			updateInfo("Dealer does not qualify.");
			
			if (!playerOne.getFolded()) {
				resultsMessage[0] += "Player 1: Dealer does not qualify.\n";
				resultsMessage[0] += "Ante: -$0\n";
				resultsMessage[0] += "Play: -$0\n";
			}else {
				playerOne.setBalance(playerOne.getBalance() - playerOne.getAnte() - playerOne.getPairPlus());
			}
			
			if (!playerTwo.getFolded()) {
				resultsMessage[1] += "Player 2: Dealer does not qualify.\n";
				resultsMessage[1] += "Ante: -$0\n";
				resultsMessage[1] += "Play: -$0\n";
			}else {
				playerTwo.setBalance(playerTwo.getBalance() - playerTwo.getAnte() - playerTwo.getPairPlus());
			}
			
		}
		
		// Now, evaluate the pair plus bets
		if (!playerOne.getFolded()) {
			playerPairPlusGUI(playerOne, 1);
		}else {
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[0] += "Player 1 FOLDS.\n";
			resultsMessage[0] += "Ante: -$" + playerOne.getAnte() + "\n";
			resultsMessage[0] += "Play: -$0\n";
			resultsMessage[0] += "Pair Plus: -$" + playerOne.getPairPlus() + "\n";
			
		}
		
		if (!playerTwo.getFolded()) {
			playerPairPlusGUI(playerTwo, 2);
		}else {
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[1] += "Player 2 FOLDS.\n";
			resultsMessage[1] += "Ante: -$" + playerTwo.getAnte() + "\n";
			resultsMessage[1] += "Play: -$0\n";
			resultsMessage[1] += "Pair Plus: -$" + playerTwo.getPairPlus() + "\n";
			
		}
		
	}// end evaluatePlays()
	
	
	// Method that runs the entire game in the console
	public static void playInConsole(Scanner input, Player playerOne, Player playerTwo, Dealer theDealer)
	{
		
		
		while(true) {
			
			System.out.println("================================================================================");
			
			// First thing to do is renew the deck if there are 34 or less cards
			theDealer.renewDeck();
			
			// Take the bets from the players
			takeBets(input, playerOne, 1);
			takeBets(input, playerTwo, 2);
			System.out.println();
			
			System.out.println("Okay, now distributing your cards...");
			playerOne.setHand(theDealer.dealHand());
			playerTwo.setHand(theDealer.dealHand());
			System.out.println("*Player 1 and Player 2 got some cards.*");
			
			System.out.println("And now I will deal myself some cards...");
			System.out.println("*The dealer got some cards.*");
			theDealer.setDealersHand(theDealer.dealHand());
			
			printPlayerInfo(playerOne, playerTwo);
			
			System.out.println();
			playOrFold(input, playerOne, 1);
			playOrFold(input, playerTwo, 2);
			
			System.out.println();
			System.out.println("Okay, now I will show my cards!");
			
			System.out.print("    Dealer's hand: ");
			printHand(theDealer.getDealersHand());
			System.out.println();
			
			// Figure out if the dealer qualifies
			if (ThreeCardLogic.dealerQualifies(theDealer.getDealersHand())) {
				
				// For each player that did not fold, pit their hand against the dealer
				if (!playerOne.getFolded()) {
					
					System.out.println("Player 1 vs Dealer: ");
					playerVsDealer(playerOne, theDealer);
				
				}
				
				if (!playerTwo.getFolded()) {
					
					System.out.println("Player 2 vs Dealer: ");
					playerVsDealer(playerTwo, theDealer);
					
				}
				
			}else{
				
				// If the dealer does not qualify, then the 
				System.out.println("Dealer does not qualify. Ante bet and Play bets are off.");
				
			}
			
			// Next, evaluate the pair plus bets for the players who have not folded
			if (!playerOne.getFolded()) {
				System.out.println();
				System.out.println("Pair plus - Player 1: ");
				playerPairPlus(playerOne);
			}
			
			if (!playerTwo.getFolded()) {
				System.out.println();
				System.out.println("Pair plus - Player 2: ");
				playerPairPlus(playerTwo);
			}
			
			printPlayerInfo(playerOne, playerTwo);
			
		}
		
		// Close the input scanner, we're done with it
		//input.close();
		
	}
	
	
	// Method to simply print the stats of the players
	public static void printPlayerInfo(Player p1, Player p2)
	{
		
		System.out.println();
		System.out.println(p1);
		System.out.println(p2);
		
	}// end printPlayerInfo()
	
	
	// Method to print a given hand (usually the dealer's hand)
	public static void printHand(ArrayList<Card> hand)
	{
		
		for(int i = 0; i < hand.size(); i++) {
			System.out.print("[" + hand.get(i) + "] ");
		}
		System.out.println();
		
	}// end printHand()
	
	
	// Method to take the bets of a player (for the non-GUI version of the game)
	public static void takeBets(Scanner s, Player player, int playerNum) 
	{
		
		int ante = 0;
		int pplus = 0;
		
		System.out.println("Player " + playerNum + ": ");
		System.out.print("Please enter the ante bet followed by pair plus bet> ");
		
		// Take in the values from the scanner
		ante = s.nextInt();
		pplus = s.nextInt();
		
		// Now, set that to the player
		player.setAnte(ante);
		player.setPairPlus(pplus);
		
	}// end takeBets()

	
	// Method for the players to determine whether they will play or fold
	public static void playOrFold(Scanner s, Player player, int playerNum) 
	{
		
		int decide = 0;
		
		System.out.println("Player " + playerNum + ": ");
		System.out.print("Please decide whether you will play (1) or fold (0)> ");
		
		// Take in the value from the scanner
		decide = s.nextInt();
		
		// If you decide to play
		if (decide != 0) {
			
			int playBet = player.getAnte();
			System.out.println("    Okay, your play bet will be " + playBet);
			
			player.setPlay(playBet);
			
		}else{
			
			// Notify the players that this player has folded
			System.out.println("    Player " + playerNum + " has folded! ");
			System.out.println("    They lose their ante, $" + player.getAnte() + ", and their pair plus, $" + player.getPairPlus());
			
			// Adjust the player's score
			player.setBalance(player.getBalance() - player.getAnte() - player.getPairPlus());
			//player.setAnte(0);
			//player.setPairPlus(0);
			player.setFolded(true);
			
		}
		
		
	}// end playOrFold()
	
	
	// Method to compare the player hand with the dealer hand and see who wins
	public static void playerVsDealer(Player player, Dealer dealer)
	{
		
		int playResult = ThreeCardLogic.compareHands(dealer.getDealersHand(), player.getHand());
		
		String playerHand = ThreeCardLogic.getHandType(player.getHand());
		String dealerHand = ThreeCardLogic.getHandType(dealer.getDealersHand());
		System.out.println("  Player: " + playerHand);
		System.out.println("  Dealer: " + dealerHand);
		
		if (playResult == 1) {
			
			// Player loses to dealer, they lose both the ante bet and play bet
			System.out.println("    Player loses to dealer!");
			System.out.println("    Player loses ante bet, $" + player.getAnte() + ", and play bet, $" + player.getPlay() + ".");
			
			player.setBalance(player.getBalance() - player.getAnte() - player.getPlay());
			
		}else if (playResult == 2) {
			
			// Player wins against dealer, they win both the ante bet and play bet
			System.out.println("    Player wins against dealer!");
			System.out.println("    Player wins ante bet, $" + player.getAnte() + ", and play bet, $" + player.getPlay());
			
			player.setWinnings(player.getWinnings() + player.getAnte() + player.getPlay());
			player.setBalance(player.getBalance() + player.getAnte() + player.getPlay());
			
		}else{
			
			// Nobody wins. The ante bet and play bet are off
			System.out.println("    DRAW! Nobody wins or loses.");
			
		}
		
		
	}// end playerVsDealer()
	
	
	// Method that is the GUI version of playerVsDealer()
	public void playerVsDealerGUI(Player player, Dealer dealer, int playerNum)
	{
		
		int playResult = ThreeCardLogic.compareHands(dealer.getDealersHand(), player.getHand());
		
		String playerHand = ThreeCardLogic.getHandType(player.getHand());
		String dealerHand = ThreeCardLogic.getHandType(dealer.getDealersHand());
		updateInfo("Player " + playerNum + ": " + playerHand + " Dealer: " + dealerHand);
		
		if (playResult == 1) {
			
			// Player loses to dealer, they lose both the ante bet and play bet
			updateInfo("Player " + playerNum + " vs Dealer: Player loses.");
			updateInfo("Player " + playerNum + " loses $" + (player.getAnte() + player.getPlay()) + ".");
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[playerNum - 1] += "Player " + playerNum + " LOSES.\n";
			resultsMessage[playerNum - 1] += "Ante: -$" + player.getAnte() + "\n";
			resultsMessage[playerNum - 1] += "Play: -$" + player.getPlay() + "\n";
			
			player.setBalance(player.getBalance() - player.getAnte() - player.getPlay());
			
		}else if (playResult == 2) {
			
			// Player wins against dealer, they win both the ante bet and play bet
			updateInfo("Player " + playerNum + " vs Dealer: Player wins!");
			updateInfo("Player " + playerNum + " is awarded $" + (player.getAnte() + player.getPlay()) + "!");
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[playerNum - 1] += "Player " + playerNum + " WINS!\n";
			resultsMessage[playerNum - 1] += "Ante: +$" + player.getAnte() + "\n";
			resultsMessage[playerNum - 1] += "Play: +$" + player.getPlay() + "\n";
			
			player.setWinnings(player.getWinnings() + player.getAnte() + player.getPlay());
			player.setBalance(player.getBalance() + player.getAnte() + player.getPlay());
			
		}else{
			
			// Nobody wins. The ante bet and play bet are off
			updateInfo("Player " + playerNum + " vs Dealer: Draw.");
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[playerNum - 1] += "Player " + playerNum + " vs Dealer: DRAW.\n";
			resultsMessage[playerNum - 1] += "Ante: +$0\n";
			resultsMessage[playerNum - 1] += "Play: +$0\n";
			
		}
		
	}// end playerVsDealerGUI()
	
	
	// Method to evaluate the pair plus bonus for the player
	public static void playerPairPlus(Player player)
	{
		
		int pairPlusPay = ThreeCardLogic.evalPPWinnings(player.getHand(), player.getPairPlus());
		
		if (pairPlusPay == 0) {
			
			System.out.println("    Player loses pair plus bet of $" + player.getPairPlus() + ".");
			player.setBalance(player.getBalance() - player.getPairPlus());
			
		}else {
			
			String pairPlusType = ThreeCardLogic.getHandType(player.getHand());
			System.out.println("    " + pairPlusType + ": ");
			System.out.println("    Player wins pair plus bet of $" + pairPlusPay + "!");
			player.setBalance(player.getBalance() + pairPlusPay);
			player.setWinnings(player.getWinnings() + pairPlusPay);
			
		}
		
	}// end playerPairPlus()
	
	// Method that is the GUI version of playerPairPlus
	public void playerPairPlusGUI(Player player, int playerNum)
	{
		
		int pairPlusPay = ThreeCardLogic.evalPPWinnings(player.getHand(), player.getPairPlus());
		
		if (pairPlusPay == 0) {
			
			updateInfo("Player " + playerNum + " loses Pair Plus bet of $" + player.getPairPlus());
			player.setBalance(player.getBalance() - player.getPairPlus());
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[playerNum - 1] += "Pair Plus: -$" + player.getPairPlus() + "\n";
			
		}else {
			
			String pairPlusType = ThreeCardLogic.getHandType(player.getHand());
			
			updateInfo("Player " + playerNum + ": Pair Plus bonus! (" + pairPlusType + ")");
			updateInfo("Player " + playerNum + " is awarded $" + pairPlusPay + "!");
			
			// Update the results message so it coincides with the alert dialogue
			resultsMessage[playerNum - 1] += "Pair Plus: +$" + pairPlusPay + "\n";
			
			player.setBalance(player.getBalance() + pairPlusPay);
			player.setWinnings(player.getWinnings() + pairPlusPay);
			
		}
		
	}// end playerPairPlus()
	
}
