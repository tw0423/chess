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
    public Collection<ChessMove> RookMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        int [][] directions = {
                {1, 0},//up

                {0, 1},//right

                {-1, 0},//downside

                {0, -1},//left

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
                ChessPosition MovingPosition = new ChessPosition(MovingRow, MovingCol);
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
