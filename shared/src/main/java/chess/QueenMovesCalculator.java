package chess;

import java.util.ArrayList;
import java.util.Collection;


//remember the board needs to position minus 1
public class QueenMovesCalculator extends ChessMoveCalculator {
    private int col;
    private int row;
    private final ChessPosition current_position;
    private final ChessBoard board;
    private ChessPiece piece;


    public QueenMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        super(position, board, piece); // Call the base class constructor
        this.col = position.getColumn();
        this.row = position.getRow();
        this.current_position = position;
        this.board = board;
        this.piece = piece;
    }


    public Collection<ChessMove> QueenMoves() {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        int c = col;
        int r = row;
        //lower left diagonal

        while ( c > 1  && r > 1 ) {
            r -= 1;
            c -= 1;
            ChessPosition movingPosition = new ChessPosition(r, c);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }



        // Lower right diagonal
        c = col;
        r = row;
        while (c < 8 && r > 1) {
            c += 1;
            r -= 1;
            ChessPosition movingPosition = new ChessPosition(r, c);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }

        // Upper left diagonal
        c = col;
        r = row;
        while (c > 1 && r < 8) {
            c -= 1;
            r += 1;
            ChessPosition movingPosition = new ChessPosition(r, c);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }

        // Upper right diagonal
        c = col;
        r = row;
        while (c < 8 && r < 8) {
            c += 1;
            r += 1;
            ChessPosition movingPosition = new ChessPosition(r, c);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }

        // Left
        c = col;
        while (c > 1) {
            c -= 1;
            ChessPosition movingPosition = new ChessPosition(row, c);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }

        // Right
        c = col;
        while (c < 8) {
            c += 1;
            ChessPosition movingPosition = new ChessPosition(row, c);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }

        // Down
        r = row;
        while (r > 1) {
            r -= 1;
            ChessPosition movingPosition = new ChessPosition(r, col);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }
        }

        // Up
        r = row;
        while (r < 8) {
            r += 1;
            ChessPosition movingPosition = new ChessPosition(r, col);
            if(board.empty(movingPosition)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
            }
            else if (!board.sameTeamPos(movingPosition, current_position)) {
                chessMoves.add(new ChessMove(current_position,movingPosition, null));
                break;
            }
            else{
                break;
            }            //  chessMoves.add(new ChessMove(position, movingPosition, ChessPiece.PieceType.QUEEN));
        }

        return chessMoves;
    }
}

// really smart way to code, I want to write it done cuz I didn't think of this way
//
// int [][]direction =
//         {
//                 {-1, -1}, // lower left
//                 {-1, 1},  // lower right
//                 {1, -1},  // upper left
//                 {1, 1},   // upper right
//                 {0, -1},  // left
//                 {0, 1},   // right
//                 {-1, 0},  // down
//                 {1, 0}    // up
//         };
//for (int[]directions: direction)
//        {
//        int currentRow = row;
//        int currentCol = col;
//        while(....)
//                {
//        currentRow +=direction[0];
//        currentCol +=direction[1];
//        ChessPosition MovingPosition = new ChessPosition(currentRow, currentCol)
//        }
//                }


