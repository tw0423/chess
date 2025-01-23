package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends ChessMoveCalculator {
    private int col;
    private int row;
    private ChessPosition currentPosition;
    private ChessBoard board;
    private final ChessPiece bishop;



    public BishopMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        super(position, board, piece); // Call the base class constructor
        this.col = position.getColumn();
        this.row = position.getRow();
        this.currentPosition = position;
        this.board = board;
        this.bishop = piece;

    }

    public Collection<ChessMove> BishopMoves() {
        Collection<ChessMove> chessMoves = new ArrayList<>();
            int [][] directions = {

            {1, 1},// upper right

            {-1, 1}, //lower right

            {-1, -1}, //lower left

            {1, -1}, //lower left
            };

            for (int[] direction : directions) {
                int movingRow = row;
                int movingCol = col;
                while (true)
                {
                    movingRow += direction[0];
                    movingCol += direction[1];

                    if(movingRow < 1 || movingCol <1 || movingRow > 8 || movingCol > 8){
                        break;
                    }

                    ChessPosition movingPosition = new ChessPosition(movingRow, movingCol);
                    if (board.empty(movingPosition))
                    {
                        chessMoves.add(new ChessMove(currentPosition, movingPosition,null ));
                    }
                    else if(!board.sameTeamPos(movingPosition, currentPosition)){
                        chessMoves.add(new ChessMove(currentPosition, movingPosition,null ));
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




