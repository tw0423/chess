package passoff.chess;

import chess.chessMove;
import chess.ChessPiece;
import chess.chessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


public class ChessMoveTests {
    private chessMove original;
    private chessMove equal;
    private chessMove startDifferent;
    private chessMove endDifferent;
    private chessMove promoteDifferent;
    @BeforeEach
    public void setUp() {
        original = new chessMove(new chessPosition(2, 6), new chessPosition(1, 5), null);
        equal = new chessMove(new chessPosition(2, 6), new chessPosition(1, 5), null);
        startDifferent = new chessMove(new chessPosition(2, 4), new chessPosition(1, 5), null);
        endDifferent = new chessMove(new chessPosition(2, 6), new chessPosition(5, 3), null);
        promoteDifferent = new chessMove(new chessPosition(2, 6), new chessPosition(1, 5),
                ChessPiece.PieceType.QUEEN);
    }

    @Test
    @DisplayName("Equals Testing")
    public void equalsTest() {
        Assertions.assertEquals(original, equal, "equals returned false for equal moves");
        Assertions.assertNotEquals(original, startDifferent, "equals returned true for different moves");
        Assertions.assertNotEquals(original, endDifferent, "equals returned true for different moves");
        Assertions.assertNotEquals(original, promoteDifferent, "equals returned true for different moves");
    }

    @Test
    @DisplayName("HashCode Testing")
    public void hashTest() {
        Assertions.assertEquals(original.hashCode(), equal.hashCode(),
                "hashCode returned different values for equal moves");
        Assertions.assertNotEquals(original.hashCode(), startDifferent.hashCode(),
                "hashCode returned the same value for different moves");
        Assertions.assertNotEquals(original.hashCode(), endDifferent.hashCode(),
                "hashCode returned the same value for different moves");
        Assertions.assertNotEquals(original.hashCode(), promoteDifferent.hashCode(),
                "hashCode returned the same value for different moves");
    }

    @Test
    @DisplayName("Combined Testing")
    public void hashSetTest() {
        Set<chessMove> set = new HashSet<>();
        set.add(original);

        Assertions.assertTrue(set.contains(original));
        Assertions.assertTrue(set.contains(equal));
        Assertions.assertEquals(1, set.size());
        set.add(equal);
        Assertions.assertEquals(1, set.size());

        Assertions.assertFalse(set.contains(startDifferent));
        set.add(startDifferent);
        Assertions.assertEquals(2, set.size());

        Assertions.assertFalse(set.contains(endDifferent));
        set.add(endDifferent);
        Assertions.assertEquals(3, set.size());

        Assertions.assertFalse(set.contains(promoteDifferent));
        set.add(promoteDifferent);
        Assertions.assertEquals(4, set.size());

    }

}