package chess;
import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;

    public KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }

    public Collection<ChessMove> kingMoves(){
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
            int movingRow = row + direction[0];
            int movingCol = col + direction[1];
            ChessPosition movingPosition = new ChessPosition(movingRow, movingCol);
            if (movingRow < 1 || movingRow > 8 || movingCol < 1 || movingCol > 8) {
                continue;
            }
            else if (this.board.empty(movingPosition) || this.board.notSameTeam(this.position, movingPosition))
            {
                moves.add(new ChessMove(this.position, movingPosition, null));
            }


        }
        return moves;

    }
}



