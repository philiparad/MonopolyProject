package com.example.monopoly;

public class Card {
    public String description;
    public int moneyChange;
    public Integer moveTo;
    public Integer moveBy;
    public boolean goToJail;
    public boolean getOutOfJailFree;
    public boolean advanceToNearestRailroad;
    public boolean advanceToNearestUtility;
    public boolean payDoubleRent;
    public int houseRepairCost;
    public int hotelRepairCost;
    public int collectFromEachPlayer;
    public int payEachPlayer;

    public Card(String description) {
        this.description = description;
    }

    public Card(String description, int moneyChange) {
        this.description = description;
        this.moneyChange = moneyChange;
    }
}