package com.blackjack.blackjack;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import com.blackjack.blackjack.Card.Rank;
public class Hand {
    private ObservableList<Node> cards; //user interface to add cards
    private SimpleIntegerProperty value = new SimpleIntegerProperty(0);

    private int aces = 0; //initiate the value since aces can be count as 1 or 11

    public Hand(ObservableList<Node> cards) { //constructor

        this.cards = cards;
    }

    public void takeCard(Card card) { //function to take a card from the deck
        cards.add(card); //add card to the list of tha cards

        if (card.rank == Rank.ACE) { //if the player gets aces, we increment the value of aces
            aces++;
        }

        if (value.get() + card.value > 21 && aces > 0) { //if the card value is greater than 21, aces is greater than 0
            value.set(value.get() + card.value - 10);    //then we count ace as '1' not '11' we add the value of cards with the aces(1) then we count ace as '1' not '11'
            aces--; //decrement the value of aces into 0
        }
        else {
            value.set(value.get() + card.value); //if the above isn't true, we add the value of the cards that the player had,
            // add with the values that the player just obtained
        }
    }

    public void reset() { //when we restart the game, all the variables go back to 0
        cards.clear();
        value.set(0);
        aces = 0;
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }
}
