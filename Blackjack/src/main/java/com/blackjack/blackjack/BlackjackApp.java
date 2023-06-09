package com.blackjack.blackjack;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BlackjackApp extends Application {

    private Deck deck = new Deck(); //create a deck
    private Hand dealer, player; //there will be 2 player which is the player and the dealer
    private Text message = new Text();

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20); //horizontal box, the spacing between element in the horizontal box
    private HBox playerCards = new HBox(20);

    private Parent CreateContent() {
        dealer = new Hand(dealerCards.getChildren());  //create an object also syncing to the horizontal box, so we can add the cares as well
        player = new Hand(playerCards.getChildren());

        Pane root = new Pane(); //we define our own layout
        root.setPrefSize(800, 600); //size of the pane

        Region background = new Region(); //set the background
        background.setPrefSize(800, 600); //the size of the background
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");  //set the color of the background to black and not transparent

        HBox rootLayout = new HBox(5); //horizontal box with spacing of 5
        rootLayout.setPadding(new Insets(5, 5, 5, 5)); //top, bottom, left, right
        Rectangle leftBG = new Rectangle(550, 560); //set the size of the left layout
        leftBG.setArcWidth(50); //set the arc width of the rectangle (corner of the rectangle)
        leftBG.setArcHeight(50); //set the arc length of the rectangle (corner of the rectangle)
        leftBG.setFill(Color.GREEN); //set the color of the left layout to green
        Rectangle rightBG = new Rectangle(230, 560); //set the size of the rectangle on the right side
        rightBG.setArcWidth(50); //set the arc width of the rectangle (corner of the rectangle)
        rightBG.setArcHeight(50); //set the arc length of the rectangle (corner of the rectangle)
        rightBG.setFill(Color.ORANGE); //set the color of the right layout to orange

        // LEFT
        VBox leftVBox = new VBox(50); //set the vertical box on the left layout
        leftVBox.setAlignment(Pos.TOP_CENTER); //set the alignment to center starting from the top of the rectangle

        Text dealerScore = new Text("Dealer: "); //create text of "Dealer"
        Text playerScore = new Text("Player: "); //create text of "Player"

        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore); //to add all the element into the left rectangle

        // RIGHT
        VBox rightVBox = new VBox(20); //set the vertical box on the left layout
        rightVBox.setAlignment(Pos.CENTER); //set the alignment to center

        final TextField bet = new TextField("BET"); //create a text-field
        bet.setDisable(false); //so we can set the bet
        bet.setMaxWidth(50); //set the width of the text-field
        Text money = new Text("Money"); //print the text "money"

        Button btnPlay = new Button("PLAY"); //add button
        Button btnHit = new Button("HIT");
        Button btnStand = new Button("STAND");

        HBox buttonsHBox = new HBox(15, btnHit, btnStand); //create the horizontal spacing
        buttonsHBox.setAlignment(Pos.CENTER); //set the alignment of the button

        rightVBox.getChildren().addAll(bet, btnPlay, money, buttonsHBox); //add all elements into the right rectangle

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        btnPlay.disableProperty().bind(playable); //while the game is running, the play button should be disabled
        btnHit.disableProperty().bind(playable.not()); //while the game is running, the hit button shouldn't be disabled
        btnStand.disableProperty().bind(playable.not()); //while the game is running, the stand button shouldn't be disabled

        playerScore.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.valueProperty().asString())); //bind the text with the value of the cards
        dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.valueProperty().asString()));

        player.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) { //if the value is over 21, the player loses the game
                endGame(); //if the value is over 21, the player loses the game
            }
        });

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) { //if the value is over 21, the dealer loses the game
                endGame(); //if the value is over 21, the dealer loses the game
            }
        });

        // INIT BUTTONS

        btnPlay.setOnAction(event -> { //set the action, when we press a button
            startNewGame();
        });

        btnHit.setOnAction(event -> {
            player.takeCard(deck.drawCard()); //take a card from the deck
        });

        btnStand.setOnAction(event -> {
            while (dealer.valueProperty().get() < 17) { //if the value is greater than 17, we take a card
                dealer.takeCard(deck.drawCard());
            }

            endGame();
        });

        return root;
    }

    private void startNewGame() { //function when we start the game
        playable.set(true); //set playable to true therefore, we can play the game
        message.setText("");

        deck.refill(); //refill the deck to 52

        dealer.reset(); //reset the all the cards and values
        player.reset();

        dealer.takeCard(deck.drawCard()); //give 2 random cards to dealer
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard()); //give 2 random cards to player
        player.takeCard(deck.drawCard());
    }

    private void endGame() {
        playable.set(false); //playable set to false, since the game has ended

        int dealerValue = dealer.valueProperty().get(); //obtain the dealer cards' value
        int playerValue = player.valueProperty().get(); //obtain the player cards' value
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue; //if there is some error with the logic of the game(debugging)


        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) { //all the condition when the dealer win the game
            winner = "DEALER";
        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) { //all the condition when the player win the game
            winner = "PLAYER";
        }

        message.setText(winner + " WON");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(CreateContent())); //set the scene with the "CreateContent" function
        primaryStage.setWidth(800); //to set the width
        primaryStage.setHeight(600); //set the height
        primaryStage.setResizable(false); //so the user won't be able to resize the window
        primaryStage.setTitle("BlackJack"); //set the title to "Blackjack"
        primaryStage.show(); //to apply the settings and show the window
    }

    public static void main(String[] args) {

        launch(args); //to launch the javafx program
    }


}
