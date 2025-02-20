import java.util.Arrays;

public class InputHandler {
    
    // Store info for easy access (set back to true to reset)
    private static boolean isCorrectTeam = true;
    private static boolean isValidInput = true;
    // Use to check if there is error in input
    private static boolean hasNoError = true;
    // Is the next move is to an empty space or to take a unit?
    private static boolean isToPreview = false;
    private static boolean hasAWinner = false;
    // Is team 1 victorious?
    private static boolean team1Victorious = false;

    // Base on the state of the game output will change accordingly
    private static final String[] output = {"Select a unit: ",
                                      "Type 'reselect' to chose a different unit\nWhere would this unit go: ",
                                      "Promote the pawn to: ",
                                      "Team "+ ((team1Victorious) ? 1 : 2) + " is the winner"};
    // Same idea for error output
    private static final String[] errorOutput = {"There is no unit there", "Invalid target destination"};


    // Store the row and int gets from input
    private static int row = 0;
    private static int col = 0;

    // Store the color for the output (always being reassign)
    private static String outputColor = "";
    private static final String resetColor = "\u001B[0m";

    // Some Function work best when return many valuables this is to store them and assign later for ease of use
    private static Object[] results = new Object[]{};
    
    // Store the index of the next move in either the preview list or the kill list from chessboard class
    private static int nextMoveIndex = 0;

    // Store all valid col input
    private static final String[] validChars = {"a", "b", "c", "d", "e", "f", "g", "h"};

    // Store the valid promotion
    private static final String[] validPromos = {"Q", "R", "N", "B"};

    // 0 = Select, 1 = Move, 2 = Pawn promotion, 3 = Someone won
    private static int state = 0;

    public static boolean printNextRequest() {
        boolean result = false;

        // if invalid input print out so, if valid but the unit isn't the same team print out so
        if (isValidInput) {
            if (!isCorrectTeam) {
                System.out.println("That unit isn't your team");
            }
            if (!ChessBoard.hasUnitAtIndex(row, col)) {
                System.out.println(errorOutput[state]);
            }
        } else {
            System.out.println("Invalid input");
        }

        // Get the team color for output
        outputColor = ChessBoard.getTeamColor();
        
        // Print the output
        System.out.print(outputColor + output[state] + resetColor);
        
        // Reset the checking conditions
        isCorrectTeam = true;
        isValidInput = true;
        hasNoError = true;

        result = true;
        return result;
    }

    public static boolean handel(String input) {
        boolean result = false;
        // handel the input based on the current state
        switch (state) {
            case 0:
                selectAUnit(input);
                break;
            case 1:
                selectAPlaceToMove(input);
                break;
            case 2:
                promotePawn(input);
                break;
        }
        
        result = true;
        return result;
    }

    private static boolean isValidSquare(String input) {
        boolean result = false;
        // VALIDATION
        if (input.length() != 2) {
            return result; 
        }

        // get the row and column and check them
        String[] splitInput = input.split("");
        String colString = splitInput[0];
        String rowString = splitInput[1];

        // check the col
        // if input col does not contain characters in array then not valid
        if (!Arrays.stream(validChars).anyMatch(colString::equals)) {
            return result;
        }
        // convert the col string to an index
        col = Arrays.asList(validChars).indexOf(colString);

        // check the row
        // try change sting to int -> if fail or out of range then fail
        try {
            row = Integer.parseInt(rowString)-1;
            if (row < 0 || row > 7) {
                throw new ArithmeticException("error message");
            }
        } catch (Exception e) {
            return result; 
        }

        result = true;
        return result;
    }

    private static boolean selectAUnit(String input) {
        boolean result = false;

        // check if input is valid
        isValidInput = isValidSquare(input);
        // if valid check if there is error in destination or wrong team
        if (isValidInput) {
            hasNoError = ChessBoard.hasUnitAtIndex(row, col);
            if (hasNoError) {
                isCorrectTeam = ChessBoard.checkTeam(row, col);
                if (isCorrectTeam) {
                    // Move state
                    state = 1;
                    // Add preview to the board
                    ChessBoard.setSelectedIndex(row, col);
                    ChessBoard.checkPossibleMoves(row, col);
                }
            }
        }
        
        result = true;
        return result;
    }

    private static boolean selectAPlaceToMove(String input) {
        boolean result = false;
        
        // check if player wants to reselect the return
        if (input.equals("reselect")) {
            state = 0;
            ChessBoard.removeSelectedIndex();
            ChessBoard.removePreviewMoves();
            ChessBoard.removePreviewsKillMoves();
            return true;
        }

        // check if input is valid
        isValidInput = isValidSquare(input);
        if (isValidInput) {
            // see if the move is valid
            results = ChessBoard.isValidMove(row, col);
            hasNoError = (boolean) results[0];
            isToPreview = (boolean) results[1];
            nextMoveIndex = (int) results[2];
            if (hasNoError) {
                // Move state
                state = 0;
                ChessBoard.moveUnit(row, col, isToPreview, nextMoveIndex);
                ChessBoard.removeSelectedIndex();
                ChessBoard.removePreviewMoves();
                ChessBoard.removePreviewsKillMoves();
                handelMoveConsequent();
                ChessBoard.switchSide();
            }
        }
        result = true;
        return result;
    }

    private static boolean promotePawn(String input) {
        boolean result = false;

        if (Arrays.stream(validPromos).anyMatch(input::equals)) {
            ChessBoard.promotePawn(row, col, input);
            state = 0;
            ChessBoard.switchSide();
        } else {
            isValidInput = false;
        }

        result = true;
        return result;
    }

    private static boolean handelMoveConsequent() {
        boolean result = false;

        results = ChessBoard.hasAWinner();
        hasAWinner = (boolean) results[0];
        team1Victorious = (boolean) results[1];

        ChessBoard.checkFirstMove(row, col);

        if (hasAWinner) {
            state = 3;
            // switch team back so we can get the correct team color
            ChessBoard.switchSide();
        }
        
        boolean hasPromotion = ChessBoard.hasPromotion(row, col);

        if (hasPromotion) {
            state = 2;
            // switch team back so we can get the correct team color
            ChessBoard.switchSide();
        }
        
        result = true;
        return result;
    }

}