package dev.truedcoinventor;

import java.util.Arrays;

class Ship {
    private final String[] parts;
    private final String[] damagedParts;
    private int damage = 0;

    Ship(String[] parts) {
        this.parts = parts;
        this.damagedParts = new String[parts.length];
    }
    void damagePart(String part) {
        damagedParts[damage++] = part;
    }

    boolean isSunk() {
        return parts.length == damage;
    }

    boolean checkCellInShip(String cell) {
        return Arrays.asList(parts).contains(cell);
    }

    boolean isPartDamaged(String part) {
        return Arrays.asList(damagedParts).contains(part);
    }
}