package chess;
import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;
    public QueenMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }
    public Collection<ChessMove> queenMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        int [][] directions = {
                {1, 0},//up
                {1, 1},//upper right
                {0, 1},//right
                {-1, 1},//lower right
                {-1, 0},//downside
                {-1, -1},//lower left
                {0, -1},//left
                {1, -1}//upper left
        };
        addContinuingLinearMove(moves, directions, row, col);

        return moves;


    }
}


