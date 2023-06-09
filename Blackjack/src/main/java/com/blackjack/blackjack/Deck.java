package com.blackjack.blackjack;

import com.blackjack.blackjack.Card.Rank;
import com.blackjack.blackjack.Card.Suit;

public class Deck {
    private Card[] cards = new Card[52]; // there are 52 cards in a standard deck

    public Deck() {

        refill();
    }

    public final void refill() {
        int i = 0;          //let the first index be 0
        for (Suit suit : Suit.values()) {    //check the suit values
            for (Rank rank : Rank.values()) { //check the rank values
                cards[i++] = new Card(suit, rank);  //increment the index everytime we add something to the deck
            }
        }
    }

    public Card drawCard() {  //to claim a random card everytime we press "hit" from the deck
        Card card = null;  //while the card is empty, and we don't have any cards
        while (card == null) {
            int index = (int)(Math.random()*cards.length);  //multiply random number with the index of the array
            card = cards[index]; //assign the card with the result of the multiplication
            cards[index] = null;
        }
        return card;
    }

}
