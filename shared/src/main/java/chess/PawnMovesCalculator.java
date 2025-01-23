package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends ChessMoveCalculator{
    private final int col;
    private final int row;
    private final ChessPosition position;
    private final ChessBoard board;
    private final ChessPiece piece;




    public PawnMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece piece) {
        super(position, board, piece); // Call the base class constructor
        this.col = position.getColumn();
        this.row = position.getRow();
        this.position = position;
        this.board = board;
        this.piece = piece;
    }



    //what do I do for thisp promotion
    //Do I just return all the move, do I need to check is the front position empty?
//    public Collection<ChessMove> PawnMoves(ChessBoard board, ChessPosition myPosition) {

    public Collection<ChessMove> PawnMoves() {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        ChessGame.TeamColor teamColor = piece.getTeamColor();

        //for white pawn
        if (teamColor == ChessGame.TeamColor.WHITE) {

            if (row == 8) {
                return chessMoves;
            }

            ChessPosition FrontPosition = new ChessPosition(row + 1, col);
            ChessPosition DiagonalLeft = new ChessPosition(row + 1, col - 1);
            ChessPosition DiagonalRight = new ChessPosition(row + 1, col + 1);



            //normal move && since if pawn can promote then there is no option for no promoitng, since it will skip if pawn reach the row 7
            if (board.empty(FrontPosition) && FrontPosition.getRow() != 8) {
                if (row == 2) {
                    ChessPosition DoubleForward = new ChessPosition(row + 2, col);
                    if(board.empty(DoubleForward)){
                        chessMoves.add(new ChessMove(position, DoubleForward, null));
                    }
                }
                chessMoves.add(new ChessMove(position, FrontPosition, null));
            }

            //will skip if it is about to get promotion
            if (col - 1 > 0 && DiagonalLeft.getRow() < 8) { // Diagonal left
                if (!board.empty(DiagonalLeft) && !board.sameTeamPos(DiagonalLeft, position)) {
                    chessMoves.add(new ChessMove(position, DiagonalLeft, null));
                }
            }
            if (col + 1 < 9 && DiagonalLeft.getRow() < 8) { // Diagonal right
                if (!board.empty(DiagonalRight) && !board.sameTeamPos(DiagonalRight, position)) {
                    chessMoves.add(new ChessMove(position, DiagonalRight, null));
                }
            }



            //normal farword move;

            //promotion
//            if (board.empty(FrontPosition) && (FrontPosition.getRow() == 8)) {
//                chessMoves.add(new ChessMove(position, FrontPosition, ChessPiece.PieceType.QUEEN));
//                chessMoves.add(new ChessMove(position, FrontPosition, ChessPiece.PieceType.ROOK));
//                chessMoves.add(new ChessMove(position, FrontPosition, ChessPiece.PieceType.BISHOP));
//                chessMoves.add(new ChessMove(position, FrontPosition, ChessPiece.PieceType.KNIGHT));
//            }

            if (row == 7) {
                if (board.empty(FrontPosition)) {
                    addPromotionMoves(chessMoves, FrontPosition);
                }
                if (!board.empty(DiagonalRight) && !board.sameTeamPos(DiagonalRight, position)) {
                    addPromotionMoves(chessMoves, DiagonalRight);
                }
                if (!board.empty(DiagonalLeft) && !board.sameTeamPos(DiagonalLeft, position)) {
                    addPromotionMoves(chessMoves, DiagonalLeft);
                }
            }

            return chessMoves;
        }
        else {
            if (row == 1) {
                return chessMoves;
            }

            ChessPosition BackwardPosition = new ChessPosition(row -1, col);
            ChessPosition DiagonalRight = new ChessPosition(row - 1, col + 1);
            ChessPosition DiagonalLeft = new ChessPosition(row - 1, col - 1);

            //normal move
            if (board.empty(BackwardPosition) && row != 2) {
                if (row == 7) {
                    ChessPosition DoubleBackward = new ChessPosition(row -2, col);
                    if(board.empty(DoubleBackward)){
                        chessMoves.add(new ChessMove(position, DoubleBackward, null));
                    }
                }
                chessMoves.add(new ChessMove(position, BackwardPosition, null));
            }


            if (col - 1 >= 1 && DiagonalLeft.getRow() > 1) { // Diagonal left
                if (!board.empty(DiagonalLeft) && !board.sameTeamPos(DiagonalLeft, position)) {
                    chessMoves.add(new ChessMove(position, DiagonalLeft, null));
                }
            }
            if (col + 1 <= 8 && DiagonalRight.getRow() > 1) { // Diagonal right
                if (!board.empty(DiagonalRight) && !board.sameTeamPos(DiagonalRight, position)) {
                    chessMoves.add(new ChessMove(position, DiagonalRight, null));
                }
            }



            //normal farword move;

            //promotion
            if (row == 2) {
                if (board.empty(BackwardPosition)) {
                    addPromotionMoves(chessMoves, BackwardPosition);
                }
                if (!board.empty(DiagonalRight) && !board.sameTeamPos(DiagonalRight, position)) {
                    addPromotionMoves(chessMoves, DiagonalRight);
                }
                if (!board.empty(DiagonalLeft) && !board.sameTeamPos(DiagonalLeft, position)) {
                    addPromotionMoves(chessMoves, DiagonalLeft);
                }
            }
            return chessMoves;
        }
    }
    private void addPromotionMoves(Collection<ChessMove> moves, ChessPosition targetPosition) {
        moves.add(new ChessMove(position, targetPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(position, targetPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(position, targetPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(position, targetPosition, ChessPiece.PieceType.KNIGHT));
    }
}

//        if ((col-1 >0) && (row+1<8)) {
//            ChessPosition DiagonalLeft = new ChessPosition(row+1, col-1);
//            ChessPosition Left = new ChessPosition(row, col-1);
//            //make sure there is enemy piece on the left
//            if (!board.empty(Left) && !board.sameTeamPos(Left, position)) {
//                //check if there is piece on
//                if(board.empty(DiagonalLeft) || !board.sameTeamPos(DiagonalLeft, position)) {
//                    chessMoves.add(new ChessMove(position, DiagonalLeft, null));
//                }
//            }
//            else if (!board.empty(DiagonalLeft) && !board.sameTeamPos(DiagonalLeft, position))
//            {
//                chessMoves.add(new ChessMove(position, DiagonalLeft, null));
//            }
//        }
//
//        if ((col+1 <8) && (row+1<8)) {
//            ChessPosition DiagonalRight = new ChessPosition(row+1, col+1);
//            ChessPosition Right = new ChessPosition(row, col+1);
//            //make sure there is enemy piece on the right
//            if (!board.empty(Right) && !board.sameTeamPos(Right, position)) {
//                //check if there is piece on
//                if(board.empty(DiagonalRight) || !board.sameTeamPos(DiagonalRight, position)) {
//                    chessMoves.add(new ChessMove(position, DiagonalRight, null));
//                }
//            }
//            else if (!board.empty(DiagonalRight) && !board.sameTeamPos(DiagonalRight, position))
//            {
//                chessMoves.add(new ChessMove(position, DiagonalRight, null));
//            }
//        }


