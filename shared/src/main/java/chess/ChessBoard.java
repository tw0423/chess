package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int column = position.getColumn();
        board[row-1][column-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];

        for (int col = 0; col < 8; col++) {
            this.board[1][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            this.board[6][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        addRNBQKBNR(ChessGame.TeamColor.WHITE, 0);
        addRNBQKBNR(ChessGame.TeamColor.BLACK, 7);
    }

    public void addRNBQKBNR(ChessGame.TeamColor teamColor, int row) {
        this.board[row][0] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        this.board[row][1] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        this.board[row][2] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        this.board[row][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        this.board[row][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        this.board[row][5] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        this.board[row][6] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        this.board[row][7] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
    }

    public boolean empty(ChessPosition position) {
        int row = position.getRow();
        int column = position.getColumn();
        return board[row-1][column-1] == null;
    };

    public boolean notSameTeam(ChessPosition position1, ChessPosition position2) {
        ChessPiece piece1 = getPiece(position1);
        ChessPiece piece2 = getPiece(position2);
        if (piece1 == null || piece2 == null) {
            return false;
        }
        return piece1.getTeamColor() != piece2.getTeamColor();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
