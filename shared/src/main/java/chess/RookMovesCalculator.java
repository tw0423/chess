package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends ChessMoveCalculator {
    private int col;
    private int row;
    private ChessPosition currentPosition;
    private ChessBoard board;
    private final ChessPiece rook;



    public RookMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        super(position, board, piece); // Call the base class constructor
        this.col = position.getColumn();
        this.row = position.getRow();
        this.currentPosition = position;
        this.board = board;
        this.rook = piece;

    }

    public Collection<ChessMove> rookMoves() {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        int [][] directions = {

                {1, 0},// up

                {0, 1}, //right

                {-1, 0}, //down

                {0, -1}, //left
        };

        for (int[] direction:directions){
            int movingRow = row;
            int movingCol = col;
            while(true){
                movingRow += direction[0];
                movingCol += direction[1];
                if(movingRow < 1 || movingCol <1 || movingRow > 8 || movingCol > 8){
                    break;
                }

                ChessPosition movingPosition = new ChessPosition(movingRow, movingCol);

                if(board.empty(movingPosition)){
                    chessMoves.add(new ChessMove(currentPosition, movingPosition, null));
                }
                else if(!board.sameTeamPos(movingPosition,currentPosition)){
                    chessMoves.add(new ChessMove(currentPosition, movingPosition, null));
                    break;
                }
                else{
                    break;
                }
            }
        }
    return chessMoves;
    }
}
