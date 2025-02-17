package dev.truedcoinventor.battleship;

import java.util.*;

//Ocean (the board)
class Ocean {
    // config values, all set by the project description
    private static final String EMPTY = "~";
    static final String SHIP = "O";
    private static final String HIT = "X";
    private static final String MISS = "M";
    // end of config
    private final String[][] ocean;
    private final String columnIndicators;
    private final String[] rowIndicators;
    // private final int alignment; // only one space to pass the tests
    private final String[] bounds = new String[4];

    Ocean(int rows, int cols) {
        this.ocean = new String[rows][cols];
        for (String[] row : ocean) {
            Arrays.fill(row, EMPTY);
        }

        // these class variables should be computed only once, when object is created
        // computed directly in constructor so the variables are final
        bounds[0] = "A";
        bounds[1] = computerIndexToHuman(rows);
        bounds[2] = "1";
        bounds[3] = String.valueOf(cols);

        // for readability, it is better if the spacing between all rows are the same, including the column indicators row; same for the columns
        /*int digitRow = computerIndexToHuman(rows).length();
        int digitCol = cols>0 ? (int) (Math.floor(Math.log10(cols)) + 1) : 1;   // if reused for something else: lack of precision when dealing with numbers bigger than int
        alignment = Math.max(digitRow, digitCol);    // only one space to pass the tests
        StringBuilder columnBuilder = new StringBuilder(" ".repeat(alignment + 1));   // adjust the space before the column indicators depending on measured length*/
        StringBuilder columnBuilder = new StringBuilder("  ");   // only one space to pass the tests
        for (int i = 1; i <= cols; i++) {
            /*int digits = (int) (Math.floor(Math.log10(i)) + 1);
            int spaces = alignment - digits + 1; // only one space to pass the tests*/
            columnBuilder.append(i);
            if (i != cols) {    // remove trailing space if indicator is the last of the line
                // columnBuilder.append(" ".repeat(spaces));
                columnBuilder.append(" ");  // only one space to pass the tests
            }
        }
        columnIndicators = columnBuilder.toString();

        rowIndicators = new String[rows];
        for (int i = 0; i < rows; i++) {
            String indicator = computerIndexToHuman(i + 1);
            // rowIndicators[i] = indicator + " ".repeat(alignment - indicator.length() + 1);
            rowIndicators[i] = indicator + " ";   // only one space to pass the tests
        }
    }

    private static int humanToComputerIndex(String humanIndex) {
        int computerIndex = 0;
        int length = humanIndex.length();

        for (int i = 0; i < length; i++) {
            int value = humanIndex.charAt(i) - 'A' + 1; // convert the character to its corresponding number
            computerIndex = computerIndex * 26 + value; // update the result using a base-26 system
        }
        return computerIndex;
    }

    private static String computerIndexToHuman(int computerIndex) {
        StringBuilder result = new StringBuilder();
        while (computerIndex > 0) {
            computerIndex--;    // adjust since A = 1 not 0
            int remainder = computerIndex % 26;
            result.insert(0, (char) ('A' + remainder)); // convert remainder to character
            computerIndex /= 26; // go to next character
        }
        return result.toString();
    }

    void displayOcean() {
        StringBuilder oceanBuilder = new StringBuilder();
        oceanBuilder.append(columnIndicators).append("\n");
        for (int i = 0; i < ocean.length; i++) {
            String[] row = ocean[i];
            // oceanBuilder.append(rowIndicators[i]).append(String.join(" ".repeat(alignment), row)).append("\n");
            oceanBuilder.append(rowIndicators[i]).append(String.join(" ", row)).append("\n");   // only one space to pass the tests
        }
        System.out.println(oceanBuilder);
    }

    boolean isCellInBounds(String cell) {
        String[] parts = Utilities.splitCoordinates(cell);
        int col = Integer.parseInt(parts[1]);
        return (
                Integer.parseInt(bounds[2]) <= col
                && col <= Integer.parseInt(bounds[3])
        ) && (
                parts[0].compareTo(bounds[0]) >= 0
                && parts[0].compareTo(bounds[1]) <= 0
        );
    }

    boolean areCoordinatesInBounds(String[] coordinates) {
        return isCellInBounds(coordinates[0]) && isCellInBounds(coordinates[1]);
    }

    String boundsToString() {
        return bounds[0] + " and " + bounds[1] + ", " + bounds[2] + " and " + bounds[3];
    }

    boolean areCoordinatesInSameLine(String[] coordinates) {
        String[] firstParts = Utilities.splitCoordinates(coordinates[0]);
        String[] secondParts = Utilities.splitCoordinates(coordinates[1]);
        return firstParts[0].equals(secondParts[0]) || firstParts[1].equals(secondParts[1]);

    }

    int getLength(String[] coordinates) {
        int length;
        String[] firstParts = Utilities.splitCoordinates(coordinates[0]);
        String[] secondParts = Utilities.splitCoordinates(coordinates[1]);
        if (firstParts[0].equals(secondParts[0])) {
            length = Math.abs(Integer.parseInt(firstParts[1]) - Integer.parseInt(secondParts[1]));
        } else {
            length = Math.abs(humanToComputerIndex(firstParts[0]) - humanToComputerIndex(secondParts[0]));
        }
        return length + 1;
    }

    String[] getParts(String[] coordinates) {
        int length = getLength(coordinates);
        String[] parts = new String[length];
        String[] firstParts = Utilities.splitCoordinates(coordinates[0]);
        String[] secondParts = Utilities.splitCoordinates(coordinates[1]);
        if (firstParts[0].equals(secondParts[0])) {
            String constant = firstParts[0];
            if (Integer.parseInt(firstParts[1]) < Integer.parseInt(secondParts[1])) {
                for (int i = Integer.parseInt(firstParts[1]); i <= Integer.parseInt(secondParts[1]); i++) {
                    parts[i - Integer.parseInt(firstParts[1])] = constant + i;
                }
            } else {
                for (int i = Integer.parseInt(secondParts[1]); i <= Integer.parseInt(firstParts[1]); i++) {
                    parts[i - Integer.parseInt(secondParts[1])] = constant + i;
                }
            }
        } else {
            String constant = firstParts[1];
            if (humanToComputerIndex(firstParts[0]) < humanToComputerIndex(secondParts[0])) {
                for (int i = humanToComputerIndex(firstParts[0]); i <= humanToComputerIndex(secondParts[0]); i++) {
                    parts[i - humanToComputerIndex(firstParts[0])] = computerIndexToHuman(i) + constant;
                }
            } else {
                for (int i = humanToComputerIndex(secondParts[0]); i <= humanToComputerIndex(firstParts[0]); i++) {
                    parts[i - humanToComputerIndex(secondParts[0])] = computerIndexToHuman(i) + constant;
                }
            }
        }
        return parts;
    }

    private String getSymbolAtCoords(int rows, int cols) {
        return ocean[rows][cols];
    }

    private String getSymbolInCell(String cell) {
        int[] indexes = getIndexesFromCell(cell);
        return getSymbolAtCoords(indexes[0], indexes[1]);
    }

    boolean haveNoAdjacentCells(String[] cells) {
        for (String cell: cells) {
            String[] parts = Utilities.splitCoordinates(cell);
            int row = humanToComputerIndex(parts[0]) - 1;
            int col = Integer.parseInt(parts[1]) - 1;

            if (!Objects.equals(parts[0], bounds[0])) {
                if (Objects.equals(getSymbolAtCoords(row - 1, col), SHIP)) {
                    return false;
                }
            }
            if (!Objects.equals(parts[0], bounds[1])) {
                if (Objects.equals(getSymbolAtCoords(row + 1, col), SHIP)) {
                    return false;
                }
            }
            if (!Objects.equals(parts[1], bounds[2])) {
                if (Objects.equals(getSymbolAtCoords(row, col - 1), SHIP)) {
                    return false;
                }
            }
            if (!Objects.equals(parts[1], bounds[3])) {
                if (Objects.equals(getSymbolAtCoords(row, col + 1), SHIP)) {
                    return false;
                }
            }
        }
        return true;
    }

    void setSymbolAtCoordinates(String symbol, int row, int col) {
        ocean[row][col] = symbol;
    }

    void setSymbolInCell(String symbol, String cell) {
        int[] parts = getIndexesFromCell(cell);
        setSymbolAtCoordinates(symbol, parts[0], parts[1]);
    }

    int[] getIndexesFromCell(String cell) {
        int[] indexes = new int[2];
        String[] parts = Utilities.splitCoordinates(cell);
        indexes[0] = humanToComputerIndex(parts[0]) - 1;
        indexes[1] = Integer.parseInt(parts[1]) - 1;
        return indexes;
    }

    boolean isHit(String cell) {
        String symbol = getSymbolInCell(cell);
        return symbol.equals(SHIP);
    }

    void hitCell(String cell) {
        setSymbolInCell(HIT, cell);
    }
    void missCell (String cell) {
        setSymbolInCell(MISS, cell);
    }
}