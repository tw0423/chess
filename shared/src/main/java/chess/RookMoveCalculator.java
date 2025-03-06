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
                ChessPosition movingPosition = new ChessPosition(movingRow, movingCol);
                if(board.empty(movingPosition)){
                    moves.add(new ChessMove(this.position, movingPosition, null));
                }
                else if(board.notSameTeam(movingPosition, this.position)){
                    moves.add(new ChessMove(this.position, movingPosition, null));
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
