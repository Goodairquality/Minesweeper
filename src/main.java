import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        minesweeper n = new minesweeper(18);
        n.placeBombs();

        Scanner in = new Scanner(System.in);
        // n.printMineGrid();
        run(n, in);
    }

    public static void run(minesweeper n, Scanner in) {
        n.printGrid();

        while (n.isPlaying() == true) {
            // User input

            System.out.println("What row?");
            int a = in.nextInt();
            System.out.println("What column?");
            int b = in.nextInt();

            // functionality, checks if [a][b] is a mine or clear
            
            System.out.println("");
            clear();
            System.out.println("\n\n");
            n.select(a, b);
        }

        System.out.println("Play Again?");
        in.nextLine();
        String ans = in.nextLine();

        if (ans.equalsIgnoreCase("Y")) {
            n.restart();
            run(n, in);
        } else {
            System.out.println("Thanks for playing!");
        }

    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
