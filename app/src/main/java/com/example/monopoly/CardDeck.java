package com.example.monopoly;

import java.util.*;

public class CardDeck {
    private final List<Card> deck = new ArrayList<>();
    private final Random random = new Random();

    public CardDeck(String type) {
        Card c;
        if (type.equals("CHANCE")) {
            c = new Card("Advance to GO (Collect $200)");
            c.moveTo = 0;
            c.moneyChange = 200;
            deck.add(c);

            c = new Card("Advance to Illinois Avenue. If you pass Go, collect $200");
            c.moveTo = 24;
            deck.add(c);

            c = new Card("Advance to St. Charles Place. If you pass Go, collect $200");
            c.moveTo = 11;
            deck.add(c);

            c = new Card("Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, pay owner ten times the amount shown on the dice");
            c.advanceToNearestUtility = true;
            deck.add(c);

            c = new Card("Advance token to the nearest Railroad and pay owner twice the rental to which he/she is otherwise entitled");
            c.advanceToNearestRailroad = true;
            c.payDoubleRent = true;
            deck.add(c);

            c = new Card("Bank pays you dividend of $50", 50);
            deck.add(c);

            c = new Card("Get out of Jail Free");
            c.getOutOfJailFree = true;
            deck.add(c);

            c = new Card("Go back three spaces");
            c.moveBy = -3;
            deck.add(c);

            c = new Card("Go directly to Jail – do not pass Go, do not collect $200");
            c.goToJail = true;
            deck.add(c);

            c = new Card("Make general repairs on all your property – For each house pay $25 – For each hotel $100");
            c.houseRepairCost = 25;
            c.hotelRepairCost = 100;
            deck.add(c);

            c = new Card("Pay poor tax of $15", -15);
            deck.add(c);

            c = new Card("Take a trip to Reading Railroad – If you pass Go collect $200");
            c.moveTo = 5;
            deck.add(c);

            c = new Card("Take a walk on the Boardwalk – Advance token to Boardwalk");
            c.moveTo = 39;
            deck.add(c);

            c = new Card("You have been elected Chairman of the Board – Pay each player $50");
            c.payEachPlayer = 50;
            deck.add(c);

            c = new Card("Your building and loan matures – Collect $150", 150);
            deck.add(c);

            c = new Card("You have won a crossword competition – Collect $100", 100);
            deck.add(c);
        } else {
            c = new Card("Advance to GO (Collect $200)");
            c.moveTo = 0;
            c.moneyChange = 200;
            deck.add(c);

            c = new Card("Bank error in your favor – Collect $200", 200);
            deck.add(c);

            c = new Card("Doctor's fees – Pay $50", -50);
            deck.add(c);

            c = new Card("From sale of stock you get $50", 50);
            deck.add(c);

            c = new Card("Get Out of Jail Free – This card may be kept until needed, or sold");
            c.getOutOfJailFree = true;
            deck.add(c);

            c = new Card("Go to Jail – Go directly to jail – Do not pass Go – Do not collect $200");
            c.goToJail = true;
            deck.add(c);

            c = new Card("Grand Opera Night – Collect $50 from every player for opening night seats");
            c.collectFromEachPlayer = 50;
            deck.add(c);

            c = new Card("Holiday Fund matures - Receive $100", 100);
            deck.add(c);

            c = new Card("Income tax refund – Collect $20", 20);
            deck.add(c);

            c = new Card("It is your birthday – Collect $10 from every player");
            c.collectFromEachPlayer = 10;
            deck.add(c);

            c = new Card("Life insurance matures – Collect $100", 100);
            deck.add(c);

            c = new Card("Hospital fees – Pay $50", -50);
            deck.add(c);

            c = new Card("School fees – Pay $50", -50);
            deck.add(c);

            c = new Card("Receive $25 consultancy fee", 25);
            deck.add(c);

            c = new Card("You are assessed for street repairs – $40 per house – $115 per hotel");
            c.houseRepairCost = 40;
            c.hotelRepairCost = 115;
            deck.add(c);

            c = new Card("You have won second prize in a beauty contest – Collect $10", 10);
            deck.add(c);
        }
    }

    public Card drawCard() {
        return deck.get(random.nextInt(deck.size()));
    }
}