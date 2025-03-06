package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;
    private ChessPiece piece;
    public PawnMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
        this.piece = board.getPiece(position);
    }
    public Collection<ChessMove> pawnMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition farwordPosition = new ChessPosition(row +1, col);
            ChessPosition doubleFarword = new ChessPosition(row+2, col);
            ChessPosition rightFarword = new ChessPosition(row+1, col+1);
            ChessPosition leftFarword = new ChessPosition(row+1, col-1);

            if(row+1<=7 && this.board.empty(farwordPosition)){
                moves.add(new ChessMove(this.position,farwordPosition,null ));
                if(row == 2 && this.board.empty(doubleFarword)){
                    moves.add(new ChessMove(this.position,doubleFarword,null ));
                }
            }
            if(row+1<=7 && col+1 <= 8 && this.board.notSameTeam(this.position, rightFarword)){
                moves.add(new ChessMove(this.position,rightFarword,null ));
            }
            if(row+1<=7 && col-1 >= 1 && this.board.notSameTeam(this.position, leftFarword)){
                moves.add(new ChessMove(this.position,leftFarword,null ));
            }
            if(row == 7){
                if(board.empty(farwordPosition)){
                    addPromotion(moves,farwordPosition);
                }
                if(col+1<=8 && board.notSameTeam(this.position, rightFarword)){
                    addPromotion(moves,rightFarword);
                }
                if(col-1 >= 1 && board.notSameTeam(this.position, leftFarword)){
                    addPromotion(moves,leftFarword);
                }
            }
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition chessPosition = new ChessPosition(row -1, col);
            ChessPosition doubleBackward = new ChessPosition(row-2, col);
            ChessPosition rightBackward = new ChessPosition(row-1, col+1);
            ChessPosition leftBackward = new ChessPosition(row-1, col-1);

            if(row- 1 >= 2 && this.board.empty(chessPosition)){
                moves.add(new ChessMove(this.position,chessPosition,null ));
                if(row == 7 && this.board.empty(doubleBackward)){
                    moves.add(new ChessMove(this.position,doubleBackward,null ));
                }
            }
            if(row-1>=2 && col+1 <= 8 && this.board.notSameTeam(this.position, rightBackward)){
                moves.add(new ChessMove(this.position,rightBackward,null ));
            }
            if(row-1>=2 && col-1 >= 1 && this.board.notSameTeam(this.position, leftBackward)){
                moves.add(new ChessMove(this.position,leftBackward,null ));
            }

            if(row == 2){
                if(board.empty(chessPosition)){
                    addPromotion(moves,chessPosition);
                }
                if(col+1<=8 && board.notSameTeam(this.position, rightBackward)){
                    addPromotion(moves,rightBackward);
                }
                if(col-1 >= 1 && board.notSameTeam(this.position, leftBackward)){
                    addPromotion(moves,leftBackward);
                }
            }
        }
        return moves;

    }

    public void addPromotion(Collection<ChessMove> moves, ChessPosition movingPosition) {
        moves.add(new ChessMove(this.position,movingPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(this.position,movingPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(this.position,movingPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(this.position,movingPosition, ChessPiece.PieceType.KNIGHT));


    }
}
