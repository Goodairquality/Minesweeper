import java.util.Arrays;
import java.util.Random;

public class minesweeper {
    private String[][] grid = { // visual grid
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" },
            { "██", "██", "██", "██", "██", "██", "██", "██" }
    };

    private String[][] mineGrid = { // practical grid
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" },
            { "0", "0", "0", "0", "0", "0", "0", "0" }
    };
    private boolean firstGuess = true;

    private boolean playing = true;

    private final int minesAmtF;
    private int minesAmt;

    public minesweeper(int mines) {
        minesAmtF = mines;
        minesAmt = minesAmtF;
    } // initialize grid

    public void printGrid() {
        int y = 0;

        System.out.println("   0| 1| 2| 3| 4| 5| 6| 7");
        for (String[] a : grid) {
            System.out.print(y++);
            for (String b : a) {
                System.out.printf("|%-2s", b);
            }
            System.out.println("");
        }

    }

    public void printMineGrid() {
        for (String[] a : mineGrid) {
            for (String b : a) {
                System.out.printf("|%-2s", b);
            }
            System.out.println("");
        }
    }

    public void printSolvedMineGrid() {
        for (int a = 0; a < mineGrid.length; a++) {
            for (int b = 0; b < mineGrid[0].length; b++) {
                if (mineGrid[a][b].equals("X")) {
                    System.out.printf("|%-2s", mineGrid[a][b]);
                } else {
                    System.out.printf("|%-2s", checkSurrounded(a, b));
                }
            }
            System.out.println("");
        }
    }

    public void placeBombs() {
        // randomly placed
        int totalMines = minesAmt;

        for (int i = 0; i < totalMines; i++) {
            int rany = new Random().nextInt(grid.length);
            int ranx = new Random().nextInt(grid.length);

            if (mineGrid[rany][ranx].equals("X")) {
                i -= 1;
                continue;
            } else {
                mineGrid[rany][ranx] = "X";
            }
        }

    }

    public void select(int a, int b) {
        int y = (a < grid.length) ? a : grid.length - 1;
        int x = (b < grid[0].length) ? b : grid[0].length - 1;

        int around;

        if (firstGuess && mineGrid[y][x].equals("X")) {
            firstGuess = false;
            minesAmt -= 1;
            mineGrid[y][x] = "0";
            around = checkSurrounded(y, x);
            grid[y][x] = Integer.toString(around);
            clearSurrounded(y, x);
            System.out.println("Free one");
            printGrid();
            // printMineGrid();

        } else if (mineGrid[y][x].equals("X")) {
            System.out.println("End of game!");
            printMineGrid();
            playing = false;
        } else {
            if (firstGuess) {
                clearSurrounded(y, x);
                firstGuess = false;
            } else {
                clearSurroundChance(y, x);
            }
            around = checkSurrounded(y, x);
            grid[y][x] = Integer.toString(around);
            System.out.println("Cleared!");
            printGrid();
            // printMineGrid();
        }
        checkWon();
    }

    public int checkSurrounded(int a, int b) {
        int sum = 0;

        boolean noUp = false;
        boolean noDown = false;
        boolean noLeft = false;
        boolean noRight = false;

        if (a == 0) {
            noUp = true;
        }

        if (a == grid.length - 1) {
            noDown = true;
        }

        if (b == 0) {
            noLeft = true;
        }

        if (b == grid[0].length - 1) {
            noRight = true;
        }

        // if on left edge
        // check values (y-1, x ), (y-1, x+1), (y, x + 1),

        /*
         * ASSUMING BLOCK IS FULLY SURROUNDED
         * direct left - (y, x-1)
         * Up left - (y+1, x-1)
         * down left - (y-1, x-1)
         * 
         * direct right - (y, x+1)
         * Up left - (y+1, x+1)
         * down left - (y-1, x+1)
         * 
         * direct above - (y+1, x)
         * direct below - (y-1, x)
         * 
         * 
         * Cases:
         * block on ceil(a=0) // exclude all UP
         * block on floor(a=max) // exclude all DOWN
         * block on left wall (b=0) // exclude all LEFT
         * block on right wall (b=max) // exclude all RIGHT
         */

        if (noUp == false && mineGrid[a - 1][b].equals("X")) {
            // up case
            sum += 1;
        }

        if (noDown == false && mineGrid[a + 1][b].equals("X")) {
            // down case
            sum += 1;
        }

        if (noRight == false && mineGrid[a][b + 1].equals("X")) {
            // right case
            sum += 1;
        }

        if (noUp == false && noRight == false && mineGrid[a - 1][b + 1].equals("X")) {
            // right case // Up case
            sum += 1;
        }

        if (noDown == false && noRight == false && mineGrid[a + 1][b + 1].equals("X")) {
            // right case // DOwn case
            sum += 1;
        }

        if (noLeft == false && mineGrid[a][b - 1].equals("X")) {
            // left case
            sum += 1;
        }

        if (noUp == false && noLeft == false && mineGrid[a - 1][b - 1].equals("X")) {
            // left case // Up case
            sum += 1;
        }

        if (noDown == false && noLeft == false && mineGrid[a + 1][b - 1].equals("X")) {
            // left case // DOwn case
            sum += 1;
        }

        return sum;
    }

    public void clearSurrounded(int a, int b) {

        boolean noUp = false;
        boolean noDown = false;
        boolean noLeft = false;
        boolean noRight = false;

        if (a == 0) {
            noUp = true;
        }

        if (a == grid.length - 1) {
            noDown = true;
        }

        if (b == 0) {
            noLeft = true;
        }

        if (b == grid[0].length - 1) {
            noRight = true;
        }

        // if on left edge
        // check values (y-1, x ), (y-1, x+1), (y, x + 1),

        /*
         * ASSUMING BLOCK IS FULLY SURROUNDED
         * direct left - (y, x-1)
         * Up left - (y+1, x-1)
         * down left - (y-1, x-1)
         * 
         * direct right - (y, x+1)
         * Up left - (y+1, x+1)
         * down left - (y-1, x+1)
         * 
         * direct above - (y+1, x)
         * direct below - (y-1, x)
         * 
         * 
         * Cases:
         * block on ceil(a=0) // exclude all UP
         * block on floor(a=max) // exclude all DOWN
         * block on left wall (b=0) // exclude all LEFT
         * block on right wall (b=max) // exclude all RIGHT
         */

        if (noUp == false && !mineGrid[a - 1][b].equals("X")) {
            // up case
            grid[a - 1][b] = Integer.toString(checkSurrounded(a - 1, b));
        }

        if (noDown == false && !mineGrid[a + 1][b].equals("X")) {
            // down case
            grid[a + 1][b] = Integer.toString(checkSurrounded(a + 1, b));
        }

        if (noRight == false && !mineGrid[a][b + 1].equals("X")) {
            // right case
            grid[a][b + 1] = Integer.toString(checkSurrounded(a, b + 1));
        }

        if (noUp == false && noRight == false && !mineGrid[a - 1][b + 1].equals("X"))
        {
        // right case // Up case
            grid[a-1][b + 1] = Integer.toString(checkSurrounded(a-1, b + 1));
        }

        // if (noDown == false && noRight == false && mineGrid[a + 1][b +
        // 1].equals("X")) {
        // // right case // DOwn case
        // sum += 1;
        // }

        if (noLeft == false && !mineGrid[a][b - 1].equals("X")) {
            // left case
            grid[a][b - 1] = Integer.toString(checkSurrounded(a, b - 1));
        }

        // if (noUp == false && noLeft == false && mineGrid[a - 1][b - 1].equals("X")) {
        // // left case // Up case
        // sum += 1;
        // }

        if (noDown == false && noLeft == false && !mineGrid[a + 1][b - 1].equals("X"))
        {
        // left case // DOwn case
            grid[a+1][b - 1] = Integer.toString(checkSurrounded(a+1, b - 1)); 
        }
    }

    public void clearSurroundChance(int a, int b) {
        int ran = new Random().nextInt(6);

        if (ran < 3) {
            clearSurrounded(a, b);
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void playingTrue() {
        playing = true;
    }

    public void restart() {
        playing = true;
        firstGuess = true;
        minesAmt = minesAmtF;

        for (int a = 0; a < mineGrid.length; a++) {
            for (int b = 0; b < mineGrid[0].length; b++) {
                mineGrid[a][b] = "0";
            }
        }

        for (int a = 0; a < grid.length; a++) {
            for (int b = 0; b < grid[0].length; b++) {
                grid[a][b] = "██";
            }
        }

        placeBombs();
    }

    public void checkWon() {
        int sum = 0;

        for (String[] a : grid) {
            for (String b : a) {
                if (b.equals("██")) {
                    sum += 1;
                }
            }
        }

        if (sum == minesAmt) {
            printMineGrid();
            System.out.println("\nWON");
            playing = false;
        }

        // - minesAmt == (grid.length * grid[0].length)

    }

}
