package chess;
import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;
    public QueenMoveCalculator(ChessBoard board, chessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
    }
    public Collection<chessMove> queenMoves() {
        Collection<chessMove> moves = new ArrayList<>();

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

            int MovingRow = row;
            int MovingCol = col;
            while (true)
            {
                MovingRow += direction[0];
                MovingCol += direction[1];
                if (MovingRow < 1 || MovingRow > 8 || MovingCol < 1 || MovingCol > 8) {
                    break;
                }
                chessPosition MovingPosition = new chessPosition(MovingRow, MovingCol);
                if(board.empty(MovingPosition)){
                    moves.add(new chessMove(this.position, MovingPosition, null));
                }
                else if(board.notSameTeam(MovingPosition, this.position)){
                    moves.add(new chessMove(this.position, MovingPosition, null));
                    break;
                }
                else{
                    break;
                }


            }
        }
        return moves;


    }
}


