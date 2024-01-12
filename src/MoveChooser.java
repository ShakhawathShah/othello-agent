import java.util.ArrayList;  

public class MoveChooser {

    public static Move chooseMove(BoardState boardState) {
        
        int searchDepth = Othello.searchDepth;
        Move bestMove = null;
        int bestMoveScore = Integer.MIN_VALUE;
        int moveScore;
        int a = Integer.MIN_VALUE;
        int b = Integer.MAX_VALUE;

        ArrayList<Move> moves = boardState.getLegalMoves();
        // return nothing if can't move
        if (moves.isEmpty()) {
            return null;
        }
        // loop through moves and call minimax on each move
        for (Move move : moves) {
            // Create a temp board to make moves on 
            BoardState tempBoardState = boardState.deepCopy();
            // Make the move on temp state
            tempBoardState.makeLegalMove(move.x, move.y);
            // Call minimax at top level node to return a score for each move
            moveScore = minimax(tempBoardState, searchDepth, a, b);
            // Updates best score and best move
            if (moveScore >= bestMoveScore) {
                bestMoveScore = moveScore;
                bestMove = move;
            }
        }
        return bestMove;
    }
    
    public static int evaluate(BoardState boardState) {
        // Score values for each position on the board 
        int boardScore[][] ={
                { 120, -20, 20, 5, 5, 20, -20, 120 },
                { -20, -40, -5, -5, -5, -5, -40, -20 },
                { 20, -5, 15, 3, 3, 15, -5, 20 },
                { 5, -5, 3, 3, 3, 3, -5, 5 },
                { 5, -5, 3, 3, 3, 3, -5, 5 },
                { 20, -5, 15, 3, 3, 15, -5, 20 },
                { -20, -40, -5, -5, -5, -5, -40, -20 },
                { 120, -20, 20, 5, 5, 20, -20, 120 }             
        };
        // re-evaluate score from zero each time
        int whiteScore = 0;
        int blackScore = 0;

        // Loop through whole board and calc white and black positions
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Increment white score 
                if (boardState.getContents(i, j) == 1) {
                    whiteScore += (boardScore[i][j]);
                }
                // Increment black score 
                else if (boardState.getContents(i, j) == -1) {
                    blackScore += (boardScore[i][j]);
                }
            }
        }
        // Returns score from white POV
        int totalScore = whiteScore - blackScore;
        return totalScore;
    }

    public static int minimax(BoardState boardState, int depth, int a, int b) {
        // new list of 'submoves' for current move 
        ArrayList<Move> subMoves = boardState.getLegalMoves();
        // change player if no moves available
        if (subMoves.isEmpty()) {
            boardState.colour *= -1; 
        }
        if (depth == 0) {
            // returns board score
            int evalNum = evaluate(boardState);
            return evalNum;
        }
        // White players turn
        else if (boardState.colour == 1) {
            // loop through submoves and creates new board state 
            for (Move subMove : subMoves) {
                BoardState tempBoardState = boardState.deepCopy();
                tempBoardState.makeLegalMove(subMove.x, subMove.y);
                // Recursive call to return value to a
                a = Math.max(a, minimax(tempBoardState, depth - 1, a, b));
                // pruning implemented prevents unncessary recursive calls
                // returns a for white - maximising score
                if (a >= b) {
                    return a;
                }
            }
            return a;
        }
        // Black players turn
        else {
            // loop through submoves and creates new board state
            for (Move subMove : subMoves) {
                BoardState tempBoardState = boardState.deepCopy();
                tempBoardState.makeLegalMove(subMove.x, subMove.y);
                // Recursive call to return value to b
                b = Math.min(b, minimax(tempBoardState, depth - 1, a, b));
                // pruning implemented prevents unncessary recursive calls
                // returns b for black - minimising score
                if (a >= b) {
                    return b;
                }
            }
            return b;
        }

    }
    

}
