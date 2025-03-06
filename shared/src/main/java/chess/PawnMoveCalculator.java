package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends ChessMoveCalculator {
    private int row;
    private int col;
    private ChessPiece piece;
    public PawnMoveCalculator(ChessBoard board, chessPosition position) {
        super(board, position);
        this.row = position.getRow();
        this.col = position.getColumn();
        this.piece = board.getPiece(position);
    }
    public Collection<chessMove> pawnMoves() {
        Collection<chessMove> moves = new ArrayList<>();
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            chessPosition FarwordPosition = new chessPosition(row +1, col);
            chessPosition DoubleFarword = new chessPosition(row+2, col);
            chessPosition RightFarword = new chessPosition(row+1, col+1);
            chessPosition LeftFarword = new chessPosition(row+1, col-1);

            if(row+1<=7 && this.board.empty(FarwordPosition)){
                moves.add(new chessMove(this.position,FarwordPosition,null ));
                if(row == 2 && this.board.empty(DoubleFarword)){
                    moves.add(new chessMove(this.position,DoubleFarword,null ));
                }
            }
            if(row+1<=7 && col+1 <= 8 && this.board.notSameTeam(this.position, RightFarword)){
                moves.add(new chessMove(this.position,RightFarword,null ));
            }
            if(row+1<=7 && col-1 >= 1 && this.board.notSameTeam(this.position, LeftFarword)){
                moves.add(new chessMove(this.position,LeftFarword,null ));
            }
            if(row == 7){
                if(board.empty(FarwordPosition)){
                    addPromotion(moves,FarwordPosition);
                }
                if(col+1<=8 && board.notSameTeam(this.position, RightFarword)){
                    addPromotion(moves,RightFarword);
                }
                if(col-1 >= 1 && board.notSameTeam(this.position, LeftFarword)){
                    addPromotion(moves,LeftFarword);
                }
            }
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            chessPosition BackwardPosition = new chessPosition(row -1, col);
            chessPosition DoubleBackward = new chessPosition(row-2, col);
            chessPosition RightBackward = new chessPosition(row-1, col+1);
            chessPosition LeftBackward = new chessPosition(row-1, col-1);

            if(row- 1 >= 2 && this.board.empty(BackwardPosition)){
                moves.add(new chessMove(this.position,BackwardPosition,null ));
                if(row == 7 && this.board.empty(DoubleBackward)){
                    moves.add(new chessMove(this.position,DoubleBackward,null ));
                }
            }
            if(row-1>=2 && col+1 <= 8 && this.board.notSameTeam(this.position, RightBackward)){
                moves.add(new chessMove(this.position,RightBackward,null ));
            }
            if(row-1>=2 && col-1 >= 1 && this.board.notSameTeam(this.position, LeftBackward)){
                moves.add(new chessMove(this.position,LeftBackward,null ));
            }

            if(row == 2){
                if(board.empty(BackwardPosition)){
                    addPromotion(moves,BackwardPosition);
                }
                if(col+1<=8 && board.notSameTeam(this.position, RightBackward)){
                    addPromotion(moves,RightBackward);
                }
                if(col-1 >= 1 && board.notSameTeam(this.position, LeftBackward)){
                    addPromotion(moves,LeftBackward);
                }
            }
        }
        return moves;

    }

    public void addPromotion(Collection<chessMove> moves, chessPosition MovingPosition) {
        moves.add(new chessMove(this.position,MovingPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new chessMove(this.position,MovingPosition, ChessPiece.PieceType.ROOK));
        moves.add(new chessMove(this.position,MovingPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new chessMove(this.position,MovingPosition, ChessPiece.PieceType.KNIGHT));


    }
}
