package dev.truedcoinventor.battleship;

enum ShipType {
    CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final int length;
    private final String name;

    ShipType(String name, int length) {
        this.name = name;
        this.length = length;
    }

    String getName() {
        return name;
    }

    int getLength() {
        return length;
    }
}