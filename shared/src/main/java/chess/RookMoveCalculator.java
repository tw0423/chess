package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;
    public RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }
    public Collection<ChessMove> rookMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        int [][] directions = {
                {1, 0},//up

                {0, 1},//right

                {-1, 0},//downside

                {0, -1},//left

        };
        addContinuingLinearMove(moves, directions, row, col);


        return moves;


    }
}
