package chess;
import java.util.ArrayList;
import java.util.Collection;

public class ChessMoveCalculator {
    public  ChessBoard board;
    public ChessPosition position;
    ChessMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    protected void addMoveIfValid(Collection<ChessMove> moves, int row, int col) {
        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            ChessPosition movingPosition = new ChessPosition(row, col);
            if (board.empty(movingPosition) || board.notSameTeam(this.position, movingPosition)) {
                moves.add(new ChessMove(this.position, movingPosition, null));
            }
        }
    }
    protected void addContinuingLinearMove(Collection<ChessMove> moves, int[][] directions, int row, int col) {
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

    }




}



