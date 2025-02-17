package dev.truedcoinventor.battleship;

public class Utilities {
    public static String[] splitCoordinates(String coordinate) {
        return coordinate.split("(?<=^[a-zA-Z])(?=\\d)");
    }
}
