package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;
    public BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }
    public Collection<ChessMove> bishopMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        int [][] directions = {

                {1, 1},//upper right

                {-1, 1},//lower right

                {-1, -1},//lower left

                {1, -1}//upper left
        };
        addContinuingLinearMove(moves, directions, row, col);
        return moves;


    }
}
