package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;

    public KnightMoveCalculator(ChessBoard board, chessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }

    public Collection<chessMove> knightMoves(){
        Collection<chessMove> moves = new ArrayList<>();

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
            int MovingRow = row + direction[0];
            int MovingCol = col + direction[1];
            chessPosition MovingPosition = new chessPosition(MovingRow, MovingCol);
            if (MovingRow < 1 || MovingRow > 8 || MovingCol < 1 || MovingCol > 8) {
                continue;
            }
            else if (this.board.empty(MovingPosition) || this.board.notSameTeam(this.position, MovingPosition))
            {
                moves.add(new chessMove(this.position, MovingPosition, null));
            }


        }
        return moves;

    }
}
