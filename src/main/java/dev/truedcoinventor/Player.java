package dev.truedcoinventor;

import java.util.ArrayList;

class Player {
    private final String name;
    private final Ocean map;    // Ocean with Player's ships
    private final Ocean radar;  // Ocean with Player's hits
    private final ArrayList<Ship> ships;

    Player(String name, int rows, int columns) {
        this.name = name;
        this.map = new Ocean(rows, columns);
        this.radar = new Ocean(rows, columns);
        this.ships = new ArrayList<>();
    }

    String getName() {
        return name;
    }

    Ocean getMap() {
        return map;
    }

    Ocean getRadar() {
        return radar;
    }

    void addShip(Ship ship) {
        ships.add(ship);
    }

    Ship getShipFromCell(String cell) {
        Ship containerShip = null;
        for (Ship ship : ships) {
            if (ship.checkCellInShip(cell)) {
                containerShip = ship;
            }
        }
        return containerShip;
    }

    boolean areAllShipsSunk() {
        boolean all = true;
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                all = false;
            }
        }
        return all;
    }
}