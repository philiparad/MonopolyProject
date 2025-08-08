package com.example.monopoly;

import java.util.*;

public class CardDeck {
    private final List<Card> deck = new ArrayList<>();
    private final Random random = new Random();

    public CardDeck(String type) {
        if (type.equals("CHANCE")) {
            deck.add(new Card("Advance to GO", 0));
            deck.add(new Card("Pay poor tax of $15", -15));
            deck.add(new Card("Your building loan matures. Receive $150", 150));
        } else {
            deck.add(new Card("Doctor's fees. Pay $50", -50));
            deck.add(new Card("Bank error in your favor. Collect $200", 200));
            deck.add(new Card("You inherit $100", 100));
        }
    }

    public Card drawCard() {
        return deck.get(random.nextInt(deck.size()));
    }
}