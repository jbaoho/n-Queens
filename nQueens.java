import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class nQueens {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the size n, of your n x n chess board:");
        int n = scan.nextInt();

        System.out.println("The first solution to an " + n + " by " + n + " chess board is:");
        int[] firstSol = firstSolution(n);
        for (int i = 0; i < n; i++) {
            System.out.printf("Row %d, Column %d\n", i + 1, firstSol[i]);
        }
        System.out.printf("The number of solutions to a %d x %d chess board is %d.", n, n, countSolutions(n));

    }

    /**
     * Determine if the given move is legal
     * @param board an array representing the position of each piece (queen) with the row being its position in the array
     * @param n the size of the board (n x n)
     * @return true if the board given is a legal move and if it results in a collision path (fails the n-Queens problem)
     */
    public static boolean isLegalPosition(int[] board, int n) {

        // if entry [i][j] of the 2D array is true, then that move causes a collision and the position is not legal
        boolean[][] illegalMoves = new boolean[n][n];

        // loop through all rows on the board that are currently filled
        int i = 0;

        while (i < n && board[i] != 0) {
            int col = board[i] - 1;

            // if the board is out of bounds, return false
            if (col >= n) return false;

            // if the move is an illegal move, return false
            if (illegalMoves[i][col]) return false;

            // mark the new queens collision routes as all illegal moves
            for (int j = 0; j < n; j++) {
                // mark the horizontal including the space the queen is on
                illegalMoves[i][j] = true;
            }
            for (int k = i + 1, a = 1; k < n; k++) {
                // mark the vertical
                // do not need to mark rows above the queen because it is impossible for
                // the board representation to have another queen that is above the current queen
                illegalMoves[k][col] = true;
                // mark the diagonals
                if (col - a >= 0) illegalMoves[k][col - a] = true;
                if (col + a < n) illegalMoves[k][col + a] = true;
                a++;
            }
            i++;
        }
        // if the function reaches the end of the position array and there are no collisions, the position is legal
        return true;
    }

    /**
     * Determine the next legal move to place a queen on the board resulting in no collisions
     * @param board an array representing the current position of each piece (queen) with the row being its position in the array
     * @param n the size of the board (n x n)
     * @return an array of size n representing the positions of all the queens for the next legal move on the chess board
     */
    public static int[] nextLegalMove(int[] board, int n) {
        // make a copy of the board to work with
        int[] b = Arrays.copyOf(board, n);

        // get the index of the last row of the board that has a queen in it
        int lastRow = getLastRow(b);

        // if the given board is legal, then add a queen to the next row
        if (isLegalPosition(b, n)) {

            // check to see if the board is full
            if (lastRow == n - 1) {
                while (b[lastRow] >= n) {
                    b[lastRow] = 0;
                    if (lastRow == 0) {
                        return new int[n];
                    }
                    b[--lastRow]++;
                }
                // backtrack until find next legal board
                while (!isLegalPosition(b, n)) {
                    if (b[lastRow] > n) {
                        b[lastRow] = 0;
                        lastRow--;
                        // if no valid next board
                        if (lastRow < 0) {
                            return new int[n];
                        }
                    }
                    b[lastRow]++;
                }
                return b;
            }

            // see if there is a valid position for a queen in the next row
            for (int i = 1; i <= n; i++) {
                b[lastRow + 1] = i;
                if (isLegalPosition(b, n)) {
                    return b;
                }
            }

            // if no legal move in the next row, go back one row before the last row and increment by one
            b[lastRow + 1] = 0;
            b[lastRow] = 0;
            if (lastRow == 0) {
                return new int[n];
            }
            b[--lastRow]++;

            // backtrack until find next legal board
            while (!isLegalPosition(b, n)) {
                if (b[lastRow] > n) {
                    b[lastRow] = 0;
                    lastRow--;
                    // if no valid next board
                    if (lastRow < 0) {
                        return new int[n];
                    }
                }
                b[lastRow]++;
            }
            return b;
        }

        // if the given board is not legal then increment in same row or backtrack
        else {
            b[lastRow]++;
            while (!isLegalPosition(b, n)) {
                if (b[lastRow] > n) {
                    b[lastRow] = 0;
                    lastRow--;
                    // if no valid next board
                    if (lastRow < 0) {
                        return new int[n];
                    }
                }
                b[lastRow]++;
                while (b[lastRow] >= n) {
                    b[lastRow] = 0;
                    if (lastRow == 0) {
                        return new int[n];
                    }
                    b[--lastRow]++;
                }
            }
            return b;
        }
    }

    /**
     * determine the first solution to the n-Queens problem
     * @param n the size of the board (n x n)
     * @return the first solution to the n-Queens problem on an n x n board
     */
    public static int[] firstSolution(int n) {
        int[] board = new int[n];
        int[] baseCase = new int[n];
        board[0] = 1;
        while (!Arrays.equals(board, baseCase) && getLastRow(board) < n - 1) {
            board = nextLegalMove(board, n);
        }
        return board;
    }

    /**
     * determine the total number of solutions to the n-Queens problem
     * @param n the size of the board (n x n)
     * @return the number of solutions to the n-Queens problem on an n x n board
     */
    public static int countSolutions(int n) {
        int[] board = new int[n];
        board[0] = 1;
        // declare an array to compare to see if all possibilities have been explored
        int[] baseCase = new int[n];
        int numSolutions = 0;
        while (!Arrays.equals(board, baseCase)) {
            // if the board is a solution, increment the number of solutions found
            if (getLastRow(board) == n - 1) {
                numSolutions++;
                board = successor(board, n);
            }
            board = nextLegalMove(board, n);
        }
        return numSolutions;
    }



    /**
     * helper method to determine the last row filled on a given board
     * @param board an array representing the current position of each piece (queen) with the row being its position in the array
     * @return the index of the last nonzero index in board
     */
    private static int getLastRow(int[] board) {
        int i = 0;
        while (i < board.length && board[i] != 0) {
            i++;
        }
        return i - 1;
    }

    /**
     * a helper method to determine the successor of a solved board
     * @param board a solved board
     * @param n the size of the board (n x n)
     * @return the immediate next board
     */
    private static int[] successor(int[] board, int n) {
        int[] b = Arrays.copyOf(board, n);
        b[n - 1]++;
        int lastRow = n - 1;
        while (b[lastRow] > n) {
            b[lastRow] = 0;
            lastRow--;
            if (lastRow < 0) return new int[n];
            b[lastRow]++;
        }
        return b;
    }


}
