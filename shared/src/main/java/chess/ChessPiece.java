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
            KingMoveCalculator kingMoves = new KingMoveCalculator(board, myPosition);
            return kingMoves.kingMoves();
        }
        else if (this.pieceType == PieceType.QUEEN){
            QueenMoveCalculator queenMoves = new QueenMoveCalculator(board, myPosition);
            return queenMoves.queenMoves();
        }
        else if (this.pieceType == PieceType.PAWN){
            PawnMoveCalculator pawnMoves = new PawnMoveCalculator(board, myPosition);
            return pawnMoves.pawnMoves();
        }
        else if(this.pieceType == PieceType.BISHOP){
            BishopMoveCalculator bishopMoves = new BishopMoveCalculator(board, myPosition);
            return bishopMoves.bishopMoves();
        }
        else if (this.pieceType == PieceType.KNIGHT){
            KnightMoveCalculator knightMoves = new KnightMoveCalculator(board, myPosition);
            return knightMoves.knightMoves();
        }
        else if (this.pieceType == PieceType.ROOK){
            RookMoveCalculator rookMoves = new RookMoveCalculator(board, myPosition);
            return rookMoves.rookMoves();
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

