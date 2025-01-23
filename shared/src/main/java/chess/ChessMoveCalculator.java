package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessMoveCalculator{
    private final ChessPosition myPosition;
    private final ChessBoard myBoard;
    private final Collection<ChessMove> myMoves;
    private final ChessPiece myChessPiece;



    public ChessMoveCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        this.myPosition = position;
        this.myBoard = board;
        var type = myBoard.getPiece(myPosition);
        this.myMoves = new ArrayList<>();
        this.myChessPiece = piece;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {


        return myMoves;
    }





}



