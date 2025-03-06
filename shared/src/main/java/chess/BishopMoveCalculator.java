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
        for (int[] direction : directions) {

            int movingRow = row;
            int movingCol = col;
            while (true)
            {
                movingRow += direction[0];
                movingCol += direction[1];
                if (movingRow < 1 || movingRow > 8 || movingCol < 1 || movingCol > 8) {
                    break;
                }
                ChessPosition MovingPosition = new ChessPosition(movingRow, movingCol);
                if(board.empty(MovingPosition)){
                    moves.add(new ChessMove(this.position, MovingPosition, null));
                }
                else if(board.notSameTeam(MovingPosition, this.position)){
                    moves.add(new ChessMove(this.position, MovingPosition, null));
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
