package passoff.chess.piece;

import chess.chessMove;
import chess.ChessPiece;
import chess.chessPosition;
import org.junit.jupiter.api.Test;
import passoff.chess.TestUtilities;

import java.util.ArrayList;

public class PawnMoveTests {

    @Test
    public void pawnMiddleOfBoardWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |P| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 4),
                new int[][]{{5, 4}}
        );
    }

    @Test
    public void pawnMiddleOfBoardBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |p| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 4),
                new int[][]{{3, 4}}
        );
    }


    @Test
    public void pawnInitialMoveWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |P| | | |
                        | | | | | | | | |
                        """,
                new chessPosition(2, 5),
                new int[][]{{3, 5}, {4, 5}}
        );
    }

    @Test
    public void pawnInitialMoveBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | |p| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(7, 3),
                new int[][]{{6, 3}, {5, 3}}
        );
    }


    @Test
    public void pawnPromotionWhite() {
        validatePromotion("""
                        | | | | | | | | |
                        | | |P| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(7, 3),
                new int[][]{{8, 3}}
        );
    }


    @Test
    public void edgePromotionBlack() {
        validatePromotion("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | |p| | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(2, 3),
                new int[][]{{1, 3}}
        );
    }


    @Test
    public void pawnPromotionCapture() {
        validatePromotion("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | |p| | | | | | |
                        |N| | | | | | | |
                        """,
                new chessPosition(2, 2),
                new int[][]{{1, 1}, {1, 2}}
        );
    }


    @Test
    public void pawnAdvanceBlockedWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |n| | | | |
                        | | | |P| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 4),
                new int[][]{}
        );
    }

    @Test
    public void pawnAdvanceBlockedBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |p| | | | |
                        | | | |r| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(5, 4),
                new int[][]{}
        );
    }


    @Test
    public void pawnAdvanceBlockedDoubleMoveWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | |p| |
                        | | | | | | | | |
                        | | | | | | |P| |
                        | | | | | | | | |
                        """,
                new chessPosition(2, 7),
                new int[][]{{3, 7}}
        );
    }

    @Test
    public void pawnAdvanceBlockedDoubleMoveBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | |p| | | | | |
                        | | |p| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(7, 3),
                new int[][]{}
        );
    }


    @Test
    public void pawnCaptureWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | |r| |N| | | |
                        | | | |P| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 4),
                new int[][]{{5, 3}, {5, 4}}
        );
    }

    @Test
    public void pawnCaptureBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |p| | | | |
                        | | | |n|R| | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 4),
                new int[][]{{3, 5}}
        );
    }

    @Test
    public void pawnMoveFromEdgeWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | |P|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 8),
                new int[][]{{5, 8}}
        );
    }

    @Test
    public void pawnMoveFromEdgeBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | |p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(5, 8),
                new int[][]{{4, 8}}
        );
    }

    @Test
    public void pawnCaptureFromEdgeWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | |r| |
                        | | | | | | | |P|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(4, 8),
                new int[][]{{5, 8}, {5, 7}}
        );
    }

    @Test
    public void pawnCaptureFromEdgeBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | |p|
                        | | | | | | |R| |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(5, 8),
                new int[][]{{4, 8}, {4, 7}}
        );
    }

    @Test
    public void pawnCaptureFromStartWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | |r| | | | | |
                        | | |r| | | | | |
                        | | | |P| | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(2, 4),
                new int[][]{{3, 3}, {3, 4}, {4, 4}}
        );
    }

    @Test
    public void pawnCaptureFromStartBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | |p| | | | |
                        | | |R| | | | | |
                        | | |R| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(7, 4),
                new int[][]{{6, 4}, {5, 4}, {6, 3}}
        );
    }

    @Test
    public void captureAndPromoteWhite() {
        validatePromotion("""
                        | | |r| | | | | |
                        | | | |P| | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(7, 4),
                new int[][]{{8, 3}, {8, 4}}
        );
    }

    @Test
    public void captureAndPromoteBlack() {
        validatePromotion("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |p| | | | |
                        | | |R| | | | | |
                        """,
                new chessPosition(2, 4),
                new int[][]{{1, 3}, {1, 4}}
        );
    }

    @Test
    public void pawnCannotCaptureBackwardWhite() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | |P| | | | | |
                        | | |r|r| | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(3, 3),
                new int[][]{{4, 3}}
        );
    }

    @Test
    public void pawnCannotCaptureBackwardBlack() {
        TestUtilities.validateMoves("""
                        | | | | | | | | |
                        | | |R|R| | | | |
                        | | |p| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new chessPosition(6, 3),
                new int[][]{{5, 3}}
        );
    }


    private static void validatePromotion(String boardText, chessPosition startingPosition, int[][] endPositions) {
        var board = TestUtilities.loadBoard(boardText);
        var testPiece = board.getPiece(startingPosition);
        var validMoves = new ArrayList<chessMove>();
        for (var endPosition : endPositions) {
            var end = new chessPosition(endPosition[0], endPosition[1]);
            validMoves.add(new chessMove(startingPosition, end, ChessPiece.PieceType.QUEEN));
            validMoves.add(new chessMove(startingPosition, end, ChessPiece.PieceType.BISHOP));
            validMoves.add(new chessMove(startingPosition, end, ChessPiece.PieceType.ROOK));
            validMoves.add(new chessMove(startingPosition, end, ChessPiece.PieceType.KNIGHT));
        }

        TestUtilities.validateMoves(board, testPiece, startingPosition, validMoves);
    }

}
