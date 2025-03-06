package passoff.chess;

import chess.*;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TestUtilities {
    public static void validateMoves(String boardText, chessPosition startPosition, int[][] endPositions) {
        var board = loadBoard(boardText);
        var testPiece = board.getPiece(startPosition);
        var validMoves = loadMoves(startPosition, endPositions);
        validateMoves(board, testPiece, startPosition, validMoves);
    }

    public static void validateMoves(ChessBoard board, ChessPiece testPiece, chessPosition startPosition,
                                     List<chessMove> validMoves) {
        var pieceMoves = new ArrayList<>(testPiece.pieceMoves(board, startPosition));
        validateMoves(validMoves, pieceMoves);
    }

    public static void validateMoves(List<chessMove> expected, List<chessMove> actual) {
        Comparator<chessMove> comparator = Comparator.comparingInt(TestUtilities::moveToInt);
        expected.sort(comparator);
        actual.sort(comparator);

        Assertions.assertEquals(expected, actual, "Wrong moves");
    }


    private static final Map<Character, ChessPiece.PieceType> CHAR_TO_TYPE_MAP = Map.of(
            'p', ChessPiece.PieceType.PAWN,
            'n', ChessPiece.PieceType.KNIGHT,
            'r', ChessPiece.PieceType.ROOK,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'b', ChessPiece.PieceType.BISHOP);

    public static ChessBoard loadBoard(String boardText) {
        var board = new ChessBoard();
        int row = 8;
        int column = 1;
        for (var c : boardText.toCharArray()) {
            switch (c) {
                case '\n' -> {
                    column = 1;
                    row--;
                }
                case ' ' -> column++;
                case '|' -> {
                }
                default -> {
                    ChessGame.TeamColor color = Character.isLowerCase(c) ? ChessGame.TeamColor.BLACK
                            : ChessGame.TeamColor.WHITE;
                    var type = CHAR_TO_TYPE_MAP.get(Character.toLowerCase(c));
                    var position = new chessPosition(row, column);
                    var piece = new ChessPiece(color, type);
                    board.addPiece(position, piece);
                    column++;
                }
            }
        }
        return board;
    }

    public static ChessBoard defaultBoard() {
        return loadBoard("""
                |r|n|b|q|k|b|n|r|
                |p|p|p|p|p|p|p|p|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                |P|P|P|P|P|P|P|P|
                |R|N|B|Q|K|B|N|R|
                """);
    }

    public static List<chessMove> loadMoves(chessPosition startPosition, int[][] endPositions) {
        var validMoves = new ArrayList<chessMove>();
        for (var endPosition : endPositions) {
            validMoves.add(new chessMove(startPosition,
                    new chessPosition(endPosition[0], endPosition[1]), null));
        }
        return validMoves;
    }

    private static int positionToInt(chessPosition position) {
        return 10 * position.getRow() + position.getColumn();
    }

    private static int moveToInt(chessMove move) {
        return 1000 * positionToInt(move.getStartPosition()) + 10 * positionToInt(move.getEndPosition()) +
                ((move.getPromotionPiece() != null) ? move.getPromotionPiece().ordinal() + 1 : 0);
    }
}
