//Import statements needed to implement the Connect Four Game
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConnectFourGame {
    /*
    List of needed variables for the system
    Defines the length, width and winning token length for the game
    As well as the grid as well
     */
    private static final char[] PLAYERS = {'R', 'Y'};
    private final int boardWidth, boardHeight;
    private final int winningTokenLength;
    private char[][] grid;
    private int lastColumn = -1, lastTop = -1;
    // Initializing the variables and then initializing the grid
    public ConnectFourGame(int width, int height, int winLength) {
        boardWidth = width;
        boardHeight = height;
        winningTokenLength = winLength;
        initializeGrid();
    }
    //Initialize the grid with the symbol "*" meaning empty
    public void initializeGrid() {
        grid = new char[boardHeight][boardWidth];
        for (char[] row : grid) {
            Arrays.fill(row, '*');
        }
    }

    public String toString() {
        // Create a string of column numbers from 1 to boardWidth (e.g., "1234...width")
        String columnNumbers = IntStream.range(1, boardWidth + 1)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

        // Create a string representation of the grid by converting each row (char array) to a string
        String gridRepresentation = Arrays.stream(grid)
                .map(String::new)
                .collect(Collectors.joining("\n"));

        // Combine the column numbers string and grid representation string, separated by a newline
        return columnNumbers + "\n" + gridRepresentation;
    }
    // horizontal check to see if a user has won along the x-axis
    public String checkHorizontal() {
        return new String(grid[lastTop]);
    }
    // vertical check to see if a user wins along the y-axis
    public String checkVertical() {
        StringBuilder sb = new StringBuilder(boardHeight);
        for (int h = 0; h < boardHeight; h++) {
            sb.append(grid[h][lastColumn]);
        }
        return sb.toString();
    }
    // checking if someone wins with a forward diagonal slash
    public String checkForwardSlashDiagonal() {
        StringBuilder sb = new StringBuilder(boardHeight);
        for (int h = 0; h < boardHeight; h++) {
            int w = lastColumn + lastTop - h;
            if (isValidPosition(h, w)) {
                sb.append(grid[h][w]);
            }
        }
        return sb.toString();
    }
    // checking if someone wins with a backwards diagonal slash
    public String checkBackslashDiagonal() {
        StringBuilder sb = new StringBuilder(boardHeight);
        for (int h = 0; h < boardHeight; h++) {
            int w = lastColumn - lastTop + h;
            if (isValidPosition(h, w)) {
                sb.append(grid[h][w]);
            }
        }
        return sb.toString();
    }
    /*
    Making sure that the user's input is valid with the size of the board
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < boardHeight && col >= 0 && col < boardWidth;
    }
    // Check if the last move made was a winning move
    public boolean isWinningMove() {
        if (lastColumn == -1) {
            // Output error message
            System.err.println("No move has been made yet");
            return false;
        }
        // get the last move as a value
        char sym = grid[lastTop][lastColumn];
        String streak = String.valueOf(sym).repeat(winningTokenLength);
        // check the value against all checks to see if it's enough to win
        return contains(checkHorizontal(), streak) ||
                contains(checkVertical(), streak) ||
                contains(checkForwardSlashDiagonal(), streak) ||
                contains(checkBackslashDiagonal(), streak);
    }
    // method for a user's input
    public void makeMove(char symbol, Scanner input) {
        do {
            // loop to check for each user's guess
            System.out.println("\nPlayer " + symbol + "'s turn: ");
            int col = input.nextInt() - 1;
            if (!(0 <= col && col < boardWidth)) {
                // message so the user knows where the input should lay
                System.out.println("Column must be between 1 and " + boardWidth);
                continue;
            }
            // place the token in the lowest available row
            for (int h = boardHeight - 1; h >= 0; h--) {
                if (grid[h][col] == '.') {
                    grid[lastTop = h][lastColumn = col] = symbol;
                    return;
                }
            }
            // make the user change their guess if the column is full
            System.out.println("Column " + (col + 1) + " is full.");
        } while (true);
    }

    private static boolean contains(String str, String substring) {
        return str.contains(substring);
    }
    // Main method to run the game
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            // boolean expression for if the user wants to replay
            boolean replay;
            do {
                // setting the height of the board
                System.out.println("Enter the height of the board: ");
                int height = input.nextInt();
                // setting the width of the board
                System.out.println("Enter the width of the board: ");
                int width = input.nextInt();
                // setting the winning token length
                System.out.println("Enter the winning length: ");
                int winningLength = input.nextInt();
                // setting the grid with the correct grid size and setting the game up properly
                ConnectFourGame game = new ConnectFourGame(width, height, winningLength);
                // Explain how to make a move
                System.out.println("Use 1-" + width + " to choose a column");
                System.out.println(game);

                int moves = height * width;
                // change between player 1 and player 2
                for (int player = 0; moves-- > 0; player = 1 - player) {
                    char symbol = PLAYERS[player];
                    game.makeMove(symbol, input);
                    System.out.println(game);
                    if (game.isWinningMove()) {
                        System.out.println("\nPlayer " + symbol + " wins!");
                        break;
                    }
                }
                // check if the game is a draw
                if (moves <= 0) {
                    System.out.println("Game over. No winner.");
                }
                // check for the user to replay the game again if they want to
                System.out.println("Do you want to play again? (yes/no)");
                String replayResponse = input.next();
                replay = replayResponse.equalsIgnoreCase("yes");

            } while (replay);
        }
    }
}
