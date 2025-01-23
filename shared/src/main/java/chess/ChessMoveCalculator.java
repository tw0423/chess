package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessMoveCalculator{
    private final ChessPosition myPosition;
    private final ChessBoard myBoard;

    public ChessMoveCalculator(ChessPosition position, ChessBoard board) {
        myPosition = position;
        myBoard = board;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }


}



