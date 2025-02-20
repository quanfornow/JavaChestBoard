import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChessBoard {

    private static final String[][] numbers = {{"  __ "," /_ |","  | |","  | |","  | |","  |_|"},
                                               {"  ___  ", " |__ \\ ", "    ) |", "   / / ", "  / /_ ", " |____|"},
                                               {"  ____  ", " |___ \\ ", "   __) |", "  |__ < ", "  ___) |", " |____/ "},
                                               {"  _  _   ", " | || |  ", " | || |_ ", " |__   _|", "    | |  ", "    |_|  "},
                                               {"  _____ ", " | ____|", " | |__  ", " |___ \\ ", "  ___) |", " |____/ "},
                                               {"    __  ", "   / /  ", "  / /_  ", " |  _ \\ ", " | (_) |", "  \\___/ "},
                                               {"  ______ ", " |____  |", "     / / ", "    / /  ", "   / /   ", "  /_/    "},
                                               {"   ___  ", "  / _ \\ ", " | (_) |", "  > _ < ", " | (_) |", "  \\___/ "}};

    private static final String[] letters = {"                   ____          _____         _____         ______         ______       _____        _    _ ",
                                             "       /\\         |  _ \\        / ____|       |  __ \\       |  ____|       |  ____|     / ____|      | |  | |",
                                             "      /  \\        | |_) |      | |            | |  | |      | |__          | |__       | |  __       | |__| |",
                                             "     / /\\ \\       |  _ <       | |            | |  | |      |  __|         |  __|      | | |_ |      |  __  |",
                                             "    / ____ \\      | |_) |      | |____        | |__| |      | |____        | |         | |__| |      | |  | |",
                                             "   /_/    \\_\\     |____/        \\_____|       |_____/       |______|       |_|          \\_____|      |_|  |_|"};

    private static String[][] chessBoard = {{"R", "N", "B", "Q", "K", "B", "N", "R"},
                                            {"P", "P", "P", "P", "P", "P", "P", "P"},
                                            {" ", " ", " ", " ", " ", " ", " ", " "},
                                            {" ", " ", " ", " ", " ", " ", " ", " "},
                                            {" ", " ", " ", " ", " ", " ", " ", " "},
                                            {" ", " ", " ", " ", " ", " ", " ", " "},
                                            {"P", "P", "P", "P", "P", "P", "P", "P"},
                                            {"R", "N", "B", "Q", "K", "B", "N", "R"}};

    // 0 -> 1st team, 1 -> 2nd team, 2 -> not team
    private static int[][] colorBoard = {{0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {2,2,2,2,2,2,2,2},
                                         {2,2,2,2,2,2,2,2},
                                         {2,2,2,2,2,2,2,2},
                                         {2,2,2,2,2,2,2,2},
                                         {1,1,1,1,1,1,1,1},
                                         {1,1,1,1,1,1,1,1}};
    
    // 0 -> " ", 1 -> selected, 2 -> in kill list, 3 -> promote, 4 -> castling, 5 -> en passant
    private static int[][] stateBoard = {{0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0},
                                         {0,0,0,0,0,0,0,0}};

    private static List<String> takenUnits1 = new ArrayList<String>();
    private static List<String> takenUnits2 = new ArrayList<String>();
    private static List<int[]> previewsList = new ArrayList<int[]>();
    private static List<int[]> previousPreviewsList = new ArrayList<int[]>();
    private static List<int[]> previewsKillList = new ArrayList<int[]>();
    private static List<int[]> previousPreviewsKillList = new ArrayList<int[]>();

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final String[] unitColor = {ANSI_GREEN, ANSI_BLUE, ANSI_WHITE};

    private static final String[] stateLeft = {" ", ">", ANSI_RED+"x"+ANSI_RESET, "+", "(", ANSI_RED+"^"+ANSI_RESET};
    private static final String[] stateRight = {" ", "<", ANSI_RED+"x"+ANSI_RESET, "+", ")",ANSI_RED+"^"+ANSI_RESET};

    public static boolean hasLetters = true;
    public static boolean hasNumbers = true;

    private static boolean isPlayer1 = true;
    private static boolean king1FirstMove = true;
    private static boolean king2FirstMove = true;
    private static boolean rook1LeftFirstMove = true;
    private static boolean rook1RightFirstMove = true;
    private static boolean rook2LeftFirstMove = true;
    private static boolean rook2RightFirstMove = true;

    public static int[] selectedIndex = {0,0};

    public static boolean printTakenUnits() {
        boolean result = false;

        System.out.println(unitColor[0] + "Lost Units: " + takenUnits1.toString() + ANSI_RESET);
        System.out.println();
        System.out.println(unitColor[1] + "Lost Units: " + takenUnits2.toString() + ANSI_RESET);
        System.out.println();

        result = true;
        return result;
    }

    public static String getTeamColor() {
        if (isPlayer1) {
            return unitColor[0];
        }
        return unitColor[1];
    }

    public static boolean setSelectedIndex(int row, int col) {
        boolean result = false;

        selectedIndex[0] = row;
        selectedIndex[1] = col;
        stateBoard[row][col] = 1;

        result = true;
        return result;
    }

    private static boolean printLetters(){
        boolean result = false;

        for (int i = 0; i < letters.length; i++){
            System.out.println(letters[i]);
        }

        result = true;
        return result;
    }

    public static boolean switchSide() {
        boolean result = false;

        isPlayer1 = !isPlayer1;

        result = true;
        return result;
    }

    public static boolean printChessBoard() {
        boolean result = false;
        
        // Print letters
        if (hasLetters) { printLetters(); }

        // PRINT THE BOARD
        int multiplier = 2;
        int length =  1+ multiplier*2;

        String Border = "|" + ("-".repeat(length*3-2)+"|").repeat(8);
        String borderWithGap = "|" + (" ".repeat(length*3-2)+"|").repeat(8);
        // String gapWithLineInBetween = halfGap+ "|" + halfGap;

        // Loop row
        for (int i = 0; i < chessBoard.length; i++) {

            //Print border + number
            System.out.print(Border);
            if (hasNumbers) { System.out.print(numbers[i][0]);}
            System.out.println();

            // Loop col
            for (int j = 0; j <= length-1; j++) {

                // If in the middle print the unit or preview
                if (j == length / 2){
                    String hold = "|";

                    for (int k = 0; k < chessBoard.length; k++) {
                        hold += " ".repeat(multiplier*3-1)+ stateLeft[stateBoard[i][k]] + unitColor[colorBoard[i][k]]+chessBoard[i][k]+ANSI_RESET+ stateRight[stateBoard[i][k]] +  " ".repeat(multiplier*3-1) + "|";
                    }

                    System.out.print(hold);
                    // + number
                    if (hasNumbers) { System.out.print(numbers[i][j+1]);}
                    System.out.println();
                } else {
                    // if not then just print with gap
                    System.out.print(borderWithGap);
                    if (hasNumbers) { System.out.print(numbers[i][j+1]);}
                    System.out.println();
                }
            }
        }
        System.out.println(Border);


        result = true;
        return result;
    }

    public static boolean hasUnitAtIndex(int row, int col) {
        if ((chessBoard[row][col] == " ")) {
            return false;
        }
        return true;
    }

    public static boolean checkPossibleMoves(int row, int col) {
        boolean result = false;

        switch(chessBoard[row][col]) {
            case "R":
                checkStraightMove(true, true, true, true, 0, true, true, 1);
                break;
            case "N":
                checkKnightMove(true, true);
                break;
            case "B":
                checkDiagonalMove(true, true, true, true, 0, true, true, 1);
                break;
            case "Q":
                checkStraightMove(true, true, true, true, 0, true, true, 1);
                checkDiagonalMove(true, true, true, true, 0, true, true, 1);
                break;
            case "K":
                checkStraightMove(true, true, true, true, 1, true, true, 1);
                checkDiagonalMove(true, true, true, true, 1, true, true, 1);
                checkCastling(row, col);
                break;
            case "P":
                if (isPlayer1) {
                    if (selectedIndex[0] == 1) {
                        checkStraightMove(false, true, false, false, 2, true, false, 1);
                    } else {
                        checkStraightMove(false, true, false, false, 1, true, false, 1);
                    }
                    checkDiagonalMove(false, false, true, true, 1, false, true, 1);
                    
                } else {
                    if (selectedIndex[0] == 6) {
                        checkStraightMove(true, false, false, false, 2, true, false, 1);
                    } else {
                        checkStraightMove(true, false, false, false, 1, true, false , 1);
                    }
                    checkDiagonalMove(true, true, false, false, 1, false, true, 1);
                }
                break;
        }

        
        addPreviewMoves();

        result = true;
        return result;
    }

    private static boolean checkCastling(int row, int col) {
        boolean result = false;
        
        if (isPlayer1) {
            if (king1FirstMove && rook1LeftFirstMove && rook1RightFirstMove) {
                if (isKingChecked(true)) {
                    
                }
            }
        } else {
            if (king2FirstMove && rook2LeftFirstMove && rook2RightFirstMove) {
                if (isKingChecked(false)) {
                    
                }
            }
        }

        result = true;
        return result;
    }

    private static boolean checkPreviewAndKill(boolean checkPreview, boolean checkKill, int nextRow, int nextCol) {
        boolean result = false;

        previousPreviewsList.clear();
        previousPreviewsKillList.clear();

        boolean hasUnit = hasUnitAtIndex(nextRow, nextCol);
        boolean isCorrectTeam = checkTeam(nextRow, nextCol);
        // if checking for previews and there is no unit
        if (!hasUnit) {
            if (checkPreview) {
                previewsList.add(new int[] {nextRow, nextCol});
                previousPreviewsList.add(new int[] {nextRow, nextCol});
            }
        } else {
            if (checkKill) {
                if (!isCorrectTeam){
                    previewsKillList.add(new int[] {nextRow, nextCol});
                    previousPreviewsKillList.add(new int[] {nextRow, nextCol});
                    stateBoard[nextRow][nextCol] = 2;
                }
                return result;
            }
        }

        result = true;
        return result;
    }

    // count start at 1, distant = 0 if you want to check until out of bounds or hit a unit
    private static boolean checkStraightMove(boolean up, boolean down, boolean left, boolean right, int distant, boolean checkPreview, boolean checkKill, int count) {
        boolean result = false;

        int selectedRow = selectedIndex[0];
        int selectedCol = selectedIndex[1];

        int nextRow = 0;
        int nextCol = 0;
        
        if (!up && !down && !left && !right) {
            result = true;
            return result;
        }

        if (up) {
            // if in bounds
            nextRow = selectedRow - count;
            nextCol = selectedCol;

            if (nextRow >= 0) {
                up = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                up = false;
            }
        }
        if (down) {
            nextRow = selectedRow + count;
            nextCol = selectedCol;

            if (nextRow <= 7) {
                down = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                down = false;
            }
        }
        if (left) {
            nextRow = selectedRow;
            nextCol = selectedCol - count;

            if (nextCol >= 0) {
                left = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                left = false;
            }
        }
        if (right) {
            nextRow = selectedRow;
            nextCol = selectedCol + count;

            if (nextCol <= 7) {
                right = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                right = false;
            }
        }

        if (distant == count) {
            result = true;
            return result;
        }

        count += 1;

        checkStraightMove(up, down, left, right, distant, checkPreview, checkKill, count);

        result = true;
        return result;
    }

    private static boolean checkDiagonalMove(boolean upLeft, boolean upRight, boolean downLeft, boolean downRight, int distant, boolean checkPreview, boolean checkKill, int count) {
        boolean result = false;

        int selectedRow = selectedIndex[0];
        int selectedCol = selectedIndex[1];

        int nextRow = 0;
        int nextCol = 0;

        if (!upLeft && !upRight && !downLeft && !downRight) {
            result = true;
            return result;
        }

        if (upLeft) {
            // if in bounds
            nextRow = selectedRow - count;
            nextCol = selectedCol - count;

            if (nextRow >= 0 && nextCol >= 0) {
                upLeft = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                upLeft = false;
            }
        }
        if (upRight) {
            nextRow = selectedRow - count;
            nextCol = selectedCol + count;

            if (nextRow >= 0 && nextCol <= 7) {
                upRight = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                upRight = false;
            }
        }
        if (downLeft) {
            nextRow = selectedRow + count;
            nextCol = selectedCol - count;

            if (nextRow <= 7 && nextCol >= 0) {
                downLeft = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                downLeft = false;
            }
        }
        if (downRight) {
            nextRow = selectedRow + count;
            nextCol = selectedCol + count;

            if (nextRow <= 7 && nextCol <= 7) {
                downRight = checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            } else {
                downRight = false;
            }
        }

        if (distant == count) {
            result = true;
            return result;
        }

        count += 1;

        checkDiagonalMove(upLeft, upRight, downLeft, downRight, distant, checkPreview, checkKill, count);

        result = true;
        return result;
    }

    private static boolean checkKnightMove(boolean checkPreview, boolean checkKill) {
        boolean result = false;

        int[][] count = {{1,2},{-1,-2},{1,-2},{-1,2},
                         {2,1},{-2,-1},{2,-1},{-2,1}};
        
        int selectedRow = selectedIndex[0];
        int selectedCol = selectedIndex[1];

        for (int i = 0; i < count.length; i++) {
            int nextRow = selectedRow + count[i][0];
            int nextCol = selectedCol + count[i][1];
            if (nextRow >= 0 && nextCol >= 0 && nextRow <= 7 && nextCol <= 7) {
                checkPreviewAndKill(checkPreview, checkKill, nextRow, nextCol);
            }
        }

        result = true;
        return result;
    }

    private static boolean isKingChecked(boolean checkTeam1) {
        boolean result = false;

        int teamColor = (checkTeam1) ? 0 : 1;

        for (int i = 0; i < previousPreviewsKillList.size(); i++) {
            if(chessBoard[previousPreviewsKillList.get(i)[0]][previousPreviewsKillList.get(i)[1]] == "K") {
                if (colorBoard[previousPreviewsKillList.get(i)[0]][previousPreviewsKillList.get(i)[1]] == teamColor) {
                    result = true;
                    return result;
                }
            }
        }

        return result;

    }

    private static boolean addPreviewMoves() {
        boolean result = false;

        for (int[] previewIndex : previewsList) {
            chessBoard[previewIndex[0]][previewIndex[1]] = "x";
        }

        result = true;
        return result;
    }

    public static boolean removePreviewMoves() {
        boolean result = false;

        for (int i = 0; i < previewsList.size(); i++) {
            chessBoard[previewsList.get(i)[0]][previewsList.get(i)[1]] = " ";
        }

        previewsList.clear();

        result = true;
        return result;
    }

    public static boolean removeSelectedIndex() {
        boolean result = false;

        stateBoard[selectedIndex[0]][selectedIndex[1]] = 0;

        result = true;
        return result;
    }

    public static boolean removePreviewsKillMoves() {
        boolean result = false;

        for (int i = 0; i < previewsKillList.size(); i++) {
            stateBoard[previewsKillList.get(i)[0]][previewsKillList.get(i)[1]] = 0;
        }

        previewsKillList.clear();

        result = true;
        return result;
    }

    public static boolean checkTeam(int row, int col) {
        if (isPlayer1) {
            if (colorBoard[row][col] == 0) {
                return true;
            }
        } else {
            if(colorBoard[row][col] == 1){
                return true;
            }
        }
        return false;
    }

    public static Object[] isValidMove(int row, int col) {
        boolean result = false;
        int index = 0;
        boolean isToPreview = false;

        for (int i = 0; i < previewsList.size(); i++) {
            if(previewsList.get(i)[0] == row && previewsList.get(i)[1] == col) {
                result = true;
                isToPreview = true;
                index = i;
            }
        }
        for (int i = 0; i < previewsKillList.size(); i++) {
            if(previewsKillList.get(i)[0] == row && previewsKillList.get(i)[1] == col) {
                result = true;
                index = i;
            }
        }

        return new Object[] {result, isToPreview, index};
    }

    public static boolean moveUnit(int row, int col, boolean isToPreview, int nextMoveIndex) {
        boolean result = false;

        // prepare for the switch
        String hold = chessBoard[row][col];
        int holdColor = colorBoard[row][col];

        // switch the destination position in the preview list to the current position of unit
        if (isToPreview){
            previewsList.remove(nextMoveIndex);
            previewsList.add(new int[] {selectedIndex[0],selectedIndex[1]});
        } else {
            previewsKillList.remove(nextMoveIndex);
            previewsKillList.add(new int[] {selectedIndex[0],selectedIndex[1]});
        }
        
        // switch the current position of the unit in the chestBoard and colorBoard to the destination
        chessBoard[row][col] = chessBoard[selectedIndex[0]][selectedIndex[1]];
        colorBoard[row][col] = colorBoard[selectedIndex[0]][selectedIndex[1]];
        if (isToPreview) {
            chessBoard[selectedIndex[0]][selectedIndex[1]] = hold;
            colorBoard[selectedIndex[0]][selectedIndex[1]] = holdColor;
        } else {
            chessBoard[selectedIndex[0]][selectedIndex[1]] = " ";
            if (isPlayer1) {
                takenUnits2.add(hold);
            } else {
                takenUnits1.add(hold);
            }
            colorBoard[selectedIndex[0]][selectedIndex[1]] = 2;
        }

        stateBoard[row][col] = 0;
        stateBoard[selectedIndex[0]][selectedIndex[1]] = 0;


        result = true;
        return result;
    }

    public static Object[] hasAWinner() {
        boolean result = false;
        boolean isTeam1 = false;

        for (int i = 0; i < takenUnits1.size(); i++) {
            if (takenUnits1.get(i) == "K") {
                result = true;
                return new Object[] {result, isTeam1};
            }
        }
        for (int i = 0; i < takenUnits2.size(); i++) {
            if (takenUnits2.get(i) == "K") {
                result = true;
                isTeam1 = true;
                return new Object[] {result, isTeam1};
            }
        }
        return new Object[] {result, isTeam1};
    }

    public static boolean promotePawn(int row, int col, String position) {
        boolean result = false;

        chessBoard[row][col] = position;
        
        result = true;
        return result;
    }

    public static boolean hasPromotion(int row, int col) {
        boolean result = false;

        if (chessBoard[row][col] == "P") {
            if ((isPlayer1 && row == 7) || (!isPlayer1 && row == 0)) {
                result = true;
            }
        }

        return result;
    }

    public static void checkFirstMove(int row, int col) {
        switch (chessBoard[row][col]) {
            case "K":
                if (isPlayer1) {
                    king1FirstMove = false;
                } else {
                    king2FirstMove = false;
                }
                break;
        
            case "R":
                if (isPlayer1) {
                    if (selectedIndex[0] == 0 && selectedIndex[1] == 0) {
                        rook1LeftFirstMove = false;
                    } else if (selectedIndex[0] == 0 && selectedIndex[1] == 7) {
                        rook1RightFirstMove = false;
                    }
                } else {
                    if (selectedIndex[0] == 7 && selectedIndex[1] == 0) {
                        rook2LeftFirstMove = false;
                    } else if (selectedIndex[0] == 7 && selectedIndex[1] == 7) {
                        rook2RightFirstMove = false;
                    }
                }
                break;
        }
    }
}
        // try {
        //     TimeUnit.SECONDS.sleep(5);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }