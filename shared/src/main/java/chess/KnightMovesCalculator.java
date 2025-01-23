package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends ChessMoveCalculator {
    private int col;
    private int row;
    private ChessPosition currentPosition;
    private ChessBoard board;
    private final ChessPiece knightPiece;


    public KnightMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        super(position, board, piece); // Call the base class constructor
        this.col = position.getColumn();
        this.row = position.getRow();
        this.currentPosition = position;
        this.board = board;
        this.knightPiece = piece;

    }
    public Collection<ChessMove> KnightMoves() {
        Collection<ChessMove> chessMoves = new ArrayList<>();

        int [][]directions =
                {
                        {2 ,1 }, //upper right (up)
                        {1, 2}, //upper right (down)
                        {-1, 2},//lower right
                        {-2, 1}, //lower right
                        {-2, -1}, //lower left
                        {-1, -2}, //lower left
                        {1, -2}, //upper left
                        {2, -1} //upper left
                };

        for (int[] direction : directions) {
            int movingCol = col + direction[1];
            int movingRow = row + direction[0];
            if(movingCol >= 1 && movingRow >=1 && movingCol <= 8 && movingRow <= 8 ) {
                ChessPosition movingPosition = new ChessPosition(movingRow, movingCol);

                //check the spot is empty
                if(board.empty(movingPosition)) {
                    chessMoves.add(new ChessMove(currentPosition,movingPosition, null));
                }
                else if (!board.sameTeamPos(movingPosition, currentPosition)) {
                    chessMoves.add(new ChessMove(currentPosition,movingPosition, null));
                }

            }
            else{
                continue;
            }


        }
        return chessMoves;
    }
}
