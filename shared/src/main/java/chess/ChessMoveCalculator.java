package chess;
import java.util.Collection;
import java.util.Objects;

public class ChessMoveCalculator {
    public  ChessBoard board;
    public ChessPosition position;
    ChessMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }

}



