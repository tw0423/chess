package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;

    public KnightMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }

    public Collection<ChessMove> knightMoves(){
        Collection<ChessMove> moves = new ArrayList<>();

        int [][] directions = {
                {2, 1},
                {1, 2},
                {-1, 2},
                {-2 ,1},
                {-2, -1},
                {-1, -2},
                {1, -2},
                {2, -1}
        };
        for (int[] direction : directions) {
            int movingRow = row + direction[0];
            int movingCol = col + direction[1];
            addMoveIfValid(moves, movingRow, movingCol);


        }
        return moves;

    }
}
