package org.edgemont.csap.finalproject;

import java.io.IOException;

public class ChessMoveBreakdown {
    private String moveType;
    private ChessPiece piece;
    private ChessPiece transformedPiece;

    public ChessMoveBreakdown(String moveType) {
        this.moveType = moveType;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public ChessPiece getTransformedPiece() {
        return transformedPiece;
    }

    public void setTransformedPiece(ChessPiece transformedPiece) {
        this.transformedPiece = transformedPiece;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Position getTgtPos() {
        return tgtPos;
    }

    public void setTgtPos(Position tgtPos) {
        this.tgtPos = tgtPos;
    }

    private Position pos;
    private Position tgtPos;

    public static ChessMoveBreakdown moveChess(ChessPiece pz, Position p, Position tgt) {
        ChessMoveBreakdown t = new ChessMoveBreakdown("MOVE");
        t.setPiece(pz);
        t.setPos(p);
        t.setTgtPos(tgt);
        // real action
        p.setPiece(null);  // clear out current position
        pz.setPos(tgt);  // set the destination position
        tgt.setPiece(pz);  // set the piece in destination position

        return t;
    }

    public static ChessMoveBreakdown captureChess(Position pos) {
        assert pos.getPiece() != null;
        ChessPiece pz = pos.getPiece();
        ChessMoveBreakdown t = new ChessMoveBreakdown("CAPTURE");
        t.setPiece(pz);
        t.setPos(pos);
        // real action
        pz.setPos(null);  // the piece has no position, it's captured.
        pos.setPiece(null);
        return t;
    }

    public static ChessMoveBreakdown promoteChess(Position pos, ChessPiece newPiece) throws IOException {
        ChessPiece origPz = pos.getPiece();
        assert origPz != null && newPiece != null;
        ChessMoveBreakdown t = new ChessMoveBreakdown("PROMOTE");
        t.setPiece(origPz);
        t.setTransformedPiece(newPiece);
        t.setPos(pos);
        //real action
        origPz.setPos(null); // old piece loses position
        pos.setPiece(newPiece); // set new piece
        newPiece.setPos(pos);
        ChessBoard.getInstance().replace(origPz, newPiece);
        return t;
    }

    public void undo() throws IOException {
        if (this.moveType.equals("MOVE")) {
            // reset the original position
            this.getPiece().setPos(this.getPos());
            this.getPos().setPiece(this.getPiece());
            // clear out the target
            this.getTgtPos().setPiece(null);
        }
        else if (this.moveType.equals("PROMOTE")){
            // set the old piece back to position
            this.getPiece().setPos(this.getPos());
            this.getPos().setPiece(this.getPiece());
            // clear promoted piece
            this.getTransformedPiece().setPos(null);
            ChessBoard.getInstance().replace(this.getTransformedPiece(), this.getPiece());
        }
        else {  // capture
            assert this.moveType.equals("CAPTURE");
            this.getPiece().setPos(this.getPos());  //restore the pz and pos
            this.getPos().setPiece(this.getPiece());
        }

    }
}
