package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KingMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;

    public KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }

    public Collection<ChessMove> KingMoves(){
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
        for (int[] direction : directions) {
            int MovingRow = row + direction[0];
            int MovingCol = col + direction[1];
            ChessPosition MovingPosition = new ChessPosition(MovingRow, MovingCol);
            if (MovingRow < 1 || MovingRow > 8 || MovingCol < 1 || MovingCol > 8) {
                continue;
            }
            else if (this.board.empty(MovingPosition) || this.board.notSameTeam(this.position, MovingPosition))
            {
                moves.add(new ChessMove(this.position, MovingPosition, null));
            }


        }
        return moves;

    }
}



