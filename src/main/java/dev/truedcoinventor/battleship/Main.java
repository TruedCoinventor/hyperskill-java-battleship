package dev.truedcoinventor.battleship;

import java.util.Scanner;

//Main (the game)
public class Main {
    // config values, all set by the project description
    private final static int ROWS = 10;
    private final static int COLUMNS = 10;
    // end of config

    private static final Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        if (ROWS < 0 || COLUMNS < 0) {    // check there is no issue with (hardcoded) configuration values
            System.out.println("Error, invalid number of rows and columns");
            System.exit(1);
        }
        Player p1 = new Player("Player 1", ROWS, COLUMNS);
        Player p2 = new Player("Player 2", ROWS, COLUMNS);
        placeShips(p1);
        switchTurn();
        placeShips(p2);
        switchTurn();
        p1.getRadar().displayOcean();
        System.out.println("Take a shot!\n");
        while (p1.areAllShipsSunk() == p2.areAllShipsSunk()) {
            takeShot(p1, p2);
            if (p1.areAllShipsSunk() ^ p2.areAllShipsSunk()) {
                break;
            }
            switchTurn();
            takeShot(p2, p1);
            if (p1.areAllShipsSunk() ^ p2.areAllShipsSunk()) {
                break;
            }
            switchTurn();
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    private static void switchTurn() {
        System.out.println("Press Enter and pass the move to another player");
        input.nextLine();
        System.out.println("\n".repeat(2));
    }

    private static void placeShips(Player player) {
        System.out.println(player.getName() + ", place your ships on the game field\n");
        Ocean board = player.getMap();
        board.displayOcean();
        for (ShipType type : ShipType.values()) {
            String name = type.getName();
            int length = type.getLength();
            System.out.println("Enter the coordinates of the " + name + " (" + length + " cells):\n");
            String coordinates = input.nextLine();
            System.out.println("\n");

            while (
                    !(
                            coordinates.matches("^[a-zA-Z]+\\d+ [a-zA-Z]+\\d+$")
                                    && board.areCoordinatesInBounds(coordinates.split(" "))
                                    && board.areCoordinatesInSameLine(coordinates.split(" "))
                                    && board.getLength(coordinates.split(" ")) == length
                                    && board.haveNoAdjacentCells(board.getParts(coordinates.split(" ")))
                    )
            ) {
                if (!coordinates.matches("^[a-zA-Z]+\\d+ [a-zA-Z]+\\d+$")) {
                    System.out.println("Error, enter the coordinates of the ship in the format \"A1 A2\"\n");
                } else {
                    if (!board.areCoordinatesInBounds(coordinates.split(" "))) {
                        String bounds = board.boundsToString();
                        System.out.println("Error, enter the coordinates of the ship in the bounds " + bounds + "\n");
                    } else {
                        if (!board.areCoordinatesInSameLine(coordinates.split(" "))) {
                            System.out.println("Error, enter the coordinates of the ship in the same row or column\n");
                        } else {
                            if (board.getLength(coordinates.split(" ")) != length) {
                                System.out.println("Error, length of the " + type.getName() + " should be " + length + "\n");
                            } else {
                                if (!board.haveNoAdjacentCells(board.getParts(coordinates.split(" ")))) {
                                    System.out.println("Error, ship should not have adjacent cells with another ship in it\n");
                                }
                            }
                        }
                    }
                }
                System.out.println("Enter the coordinates of the " + name + " (" + length + " cells):\n");
                coordinates = input.nextLine();
            }
            String[] parts = board.getParts(coordinates.split(" "));
            Ship ship = new Ship(parts);
            player.addShip(ship);
            for (String cell : parts) {
                board.setSymbolInCell(Ocean.SHIP, cell);
            }
            board.displayOcean();
        }
    }

    private static void takeShot(Player player, Player enemy) {
        Ocean enemyMap = enemy.getMap();
        Ocean radar = player.getRadar();
        radar.displayOcean();
        System.out.println("-".repeat(COLUMNS));
        Ocean map = player.getMap();
        map.displayOcean();
        System.out.println(player.getName() + ", it's your turn:\n");
        String target = input.nextLine();
        while (!radar.isCellInBounds(target)) {
            System.out.println("Error, the target cell is out of bounds\n");
            target = input.nextLine();
        }
        if (enemyMap.isHit(target)) {
            radar.hitCell(target);
            enemyMap.hitCell(target);
            radar.displayOcean();
            Ship ship = enemy.getShipFromCell(target);
            if (!ship.isPartDamaged(target)) {
                ship.damagePart(target);
                if (ship.isSunk()) {
                    if (!enemy.areAllShipsSunk()) {
                        System.out.println("You sank a ship! Specify a new target:\n");
                    }
                } else {
                    System.out.println("You hit a ship! Try again:\n");
                }
            } else {
                System.out.println("You hit a ship! Try again:\n");
            }
        } else {
            radar.missCell(target);
            enemyMap.missCell(target);
            radar.displayOcean();
            System.out.println("You missed. Try again:\n");
        }

    }
}
