package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends ChessMoveCalculator {
    private int col;
    private int row;
    private ChessPosition current_position;
    private ChessBoard board;
    private final ChessPiece kingPiece;


    public KingMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        super(position, board, piece); // Call the base class constructor
        this.col = position.getColumn();
        this.row = position.getRow();
        this.current_position = position;
        this.board = board;
        this.kingPiece = piece;

    }
    public Collection<ChessMove> KingMoves() {
        Collection<ChessMove> chessMoves = new ArrayList<>();

         int [][]directions =
                 {
                    {1, 0},    // up
                    {1, 1},   // upper right
                    {0, 1},   // right
                    {-1, 1},  // lower right
                    {-1, 0},  // down
                    {-1, -1}, // lower left
                    {0, -1},  // left
                    {1, -1},  // upper left
                 };

         for (int[] direction : directions) {
             int movingCol = col + direction[1];
             int movingRow = row + direction[0];
             if(movingCol >= 1 && movingRow >=1 && movingCol <= 8 && movingRow <= 8 ) {
             ChessPosition movingPosition = new ChessPosition(movingRow, movingCol);

                 //check the spot is empty
                 if(board.empty(movingPosition)) {
                     chessMoves.add(new ChessMove(current_position,movingPosition, null));
                 }
                 else if (!board.sameTeamPos(movingPosition, current_position)) {
                     chessMoves.add(new ChessMove(current_position,movingPosition, null));
                 }

             }


         }
         return chessMoves;
    }
}



