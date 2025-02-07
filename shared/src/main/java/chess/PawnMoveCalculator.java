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
    public Collection<ChessMove> PawnMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition FarwordPosition = new ChessPosition(row +1, col);
            ChessPosition DoubleFarword = new ChessPosition(row+2, col);
            ChessPosition RightFarword = new ChessPosition(row+1, col+1);
            ChessPosition LeftFarword = new ChessPosition(row+1, col-1);

            if(row+1<=7 && this.board.empty(FarwordPosition)){
                moves.add(new ChessMove(this.position,FarwordPosition,null ));
                if(row == 2 && this.board.empty(DoubleFarword)){
                    moves.add(new ChessMove(this.position,DoubleFarword,null ));
                }
            }
            if(row+1<=7 && col+1 <= 8 && this.board.notSameTeam(this.position, RightFarword)){
                moves.add(new ChessMove(this.position,RightFarword,null ));
            }
            if(row+1<=7 && col-1 >= 1 && this.board.notSameTeam(this.position, LeftFarword)){
                moves.add(new ChessMove(this.position,LeftFarword,null ));
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
            ChessPosition BackwardPosition = new ChessPosition(row -1, col);
            ChessPosition DoubleBackward = new ChessPosition(row-2, col);
            ChessPosition RightBackward = new ChessPosition(row-1, col+1);
            ChessPosition LeftBackward = new ChessPosition(row-1, col-1);

            if(row- 1 >= 2 && this.board.empty(BackwardPosition)){
                moves.add(new ChessMove(this.position,BackwardPosition,null ));
                if(row == 7 && this.board.empty(DoubleBackward)){
                    moves.add(new ChessMove(this.position,DoubleBackward,null ));
                }
            }
            if(row-1>=2 && col+1 <= 8 && this.board.notSameTeam(this.position, RightBackward)){
                moves.add(new ChessMove(this.position,RightBackward,null ));
            }
            if(row-1>=2 && col-1 >= 1 && this.board.notSameTeam(this.position, LeftBackward)){
                moves.add(new ChessMove(this.position,LeftBackward,null ));
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

    public void addPromotion(Collection<ChessMove> moves, ChessPosition MovingPosition) {
        moves.add(new ChessMove(this.position,MovingPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(this.position,MovingPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(this.position,MovingPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(this.position,MovingPosition, ChessPiece.PieceType.KNIGHT));


    }
}
