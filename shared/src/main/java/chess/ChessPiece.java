package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor teamColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
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

        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.pieceType == PieceType.KING){
            KingMoveCalculator KingMoves = new KingMoveCalculator(board, myPosition);
            return KingMoves.KingMoves();
        }
        else if (this.pieceType == PieceType.QUEEN){
            QueenMoveCalculator QueenMoves = new QueenMoveCalculator(board, myPosition);
            return QueenMoves.QueenMoves();
        }
        else if (this.pieceType == PieceType.PAWN){
            PawnMoveCalculator PawnMoves = new PawnMoveCalculator(board, myPosition);
            return PawnMoves.PawnMoves();
        }
        else if(this.pieceType == PieceType.BISHOP){
            BishopMoveCalculator BishopMoves = new BishopMoveCalculator(board, myPosition);
            return BishopMoves.BishopMoves();
        }
        else if (this.pieceType == PieceType.KNIGHT){
            KnightMoveCalculator KnightMoves = new KnightMoveCalculator(board, myPosition);
            return KnightMoves.KnightMoves();
        }
        else if (this.pieceType == PieceType.ROOK){
            RookMoveCalculator RookMoves = new RookMoveCalculator(board, myPosition);
            return RookMoves.RookMoves();
        }
        return this.pieceMoves(board, myPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", pieceType=" + pieceType +
                '}';
    }
}

