import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        // Scanner for user input
        Scanner theScanner = new Scanner(System.in);

        // Recursive function keep the game running
        runGame(theScanner);

        // Close the scanner so it stop screaming at me
        theScanner.close();
    }

    public static void runGame(Scanner theScanner) {
        // Clear console (not really)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Print the board
        ChessBoard.printChessBoard();
        System.out.println("");

        //Print the taken units
        ChessBoard.printTakenUnits();
        
        // Print the request
        InputHandler.printNextRequest();

        // Handel the input
        InputHandler.handel(theScanner.nextLine());

        // Next cycle
        runGame(theScanner);
    }








    // OLD STUFF I FORGET EXISTED

    public static boolean printStar(int height) {
        boolean result = false;
        int count = 1;
        for (int i = 1; i <= height; i++) {
            String stringRow = "";
            stringRow += " ".repeat(height-i);
            stringRow += "*".repeat(count);
            count += 2;
            System.out.println(stringRow);
        }
        
        result = true;
        return result;
    }

    public static boolean boxChar(char inputChar) {
        boolean result = false;
        int multiplier = 6;
        int length =  1+ multiplier*2;
        
        System.out.println("-".repeat(length*3));
        for (int i = 0; i <= length-1; i++) {
            if (i == length / 2){
                System.out.println("|" + " ".repeat(multiplier*3) + inputChar + " ".repeat(multiplier*3)+ "|");
            } else {
                System.out.println("|" + " ".repeat(length*3-2) + "|");
            }
        }
        System.out.println("-".repeat(length*3));
        
        result = true;
        return result;
    }
}

// try {
//     TimeUnit.SECONDS.sleep(5);
// } catch (InterruptedException e) {
//     // TODO Auto-generated catch block
//     e.printStackTrace();
// }
