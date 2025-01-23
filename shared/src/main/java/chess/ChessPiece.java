package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.PAWN){
            PawnMovesCalculator PawnMoves = new PawnMovesCalculator(myPosition, board, this);
            return PawnMoves.PawnMoves();
        }
        else if (this.type == PieceType.QUEEN){
            QueenMovesCalculator QueenMoves = new QueenMovesCalculator(myPosition, board, this);
            return QueenMoves.QueenMoves();
        }
        else if (this.type == PieceType.KING){
            KingMovesCalculator KingMoves = new KingMovesCalculator(myPosition, board, this);
            return KingMoves.KingMoves();
        }
        else if (this.type == PieceType.BISHOP){
            BishopMovesCalculator BishopMoves = new BishopMovesCalculator(myPosition, board, this);
            return BishopMoves.BishopMoves();
        }
        else if (this.type == PieceType.ROOK){
            RookMovesCalculator RookMoves = new RookMovesCalculator(myPosition, board, this);
            return RookMoves.rookMoves();
        }
        else if (this.type == PieceType.KNIGHT){
            KnightMovesCalculator KnightMoves = new KnightMovesCalculator(myPosition, board, this);
            return KnightMoves.KnightMoves();
        }


//        else if (this.type == PieceType.KNIGHT){
//            return
//        }
        return null;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}



