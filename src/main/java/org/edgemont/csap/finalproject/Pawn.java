package org.edgemont.csap.finalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * This is the internal pawn script.
 *
 * @author Anderson Zhang
 * @version 1.0
 */

public class Pawn extends ChessPiece
{
    private boolean canMove2 = true;
    public boolean justMoved2 = false;

    /**
     * pawn constructor
     * @param color
     * @param x
     * @param iconFile
     * @throws IOException
     */
    public Pawn(PlayerType color, Position x, String iconFile) throws IOException {
        super(color, "org.edgemont.csap.finalproject.Pawn", x, iconFile);
    }

    /** Pawns can initially move 1 or 2 steps forward. Can only move 1 step forward afterward.
     * However, the en passant move is possible under the following conditions:
     * - the enemy pawn advanced two squares on the previous turn;
     * - the capturing pawn attacks the square that the enemy pawn passed over.
     * The capturing pawn can move diagonally forward to the square that the enemy pawn passed
     * @return a list of legal moves allowed.
     */
    public ArrayList<Position> getLegalMoves() throws IOException {
        ArrayList<Position> legals = new ArrayList<Position>();
        int direction = this.getColor() == PlayerType.WHITE ? -1 : 1;
        ChessPositionCollection posCollection = ChessBoard.getInstance().getPositionCollection();
        Position fwd1Step = posCollection.getPosition(pos.getX() + direction, pos.getY());
        if (fwd1Step != null && fwd1Step.getPiece() == null) legals.add(fwd1Step);
        if(canMove2) {
            Position fwd2Step = posCollection.getPosition(pos.getX() + (direction*2), pos.getY());
            assert fwd1Step != null;
            if (fwd1Step.getPiece() == null && fwd2Step.getPiece() == null) legals.add(fwd2Step);
        }

        Position diagPos = posCollection.getPosition(pos.getX() + direction, pos.getY() + direction);
        if (isDiagonalPosLegal(diagPos, posCollection)) legals.add(diagPos);

        Position diagPos2 = posCollection.getPosition(pos.getX() + direction, pos.getY() - direction);
        if (isDiagonalPosLegal(diagPos2, posCollection)) legals.add(diagPos2);

        return legals;
    }

    private boolean isDiagonalPosLegal(Position diagPos, ChessPositionCollection posCollection) {
        if (diagPos != null) {
            Position enpStep = posCollection.getPosition(pos.getX(), diagPos.getY());
            return diagPos.getPiece() != null && diagPos.getPiece().getColor() != getColor()
                    || (diagPos.getPiece() == null && enpStep.isOccupied()
                    && enpStep.getPiece().getColor() != getColor() && enpStep.getPiece() instanceof Pawn
                    && ((Pawn) enpStep.getPiece()).justMoved2);
        }
        return false;
    }

    /**
     * Moves a piece.
     * @param d
     * @throws IOException
     */
    public Stack<ChessMoveBreakdown> move(Position d) throws IOException {
        justMoved2 = Math.abs(d.getX()-pos.getX()) == 2;
        Stack<ChessMoveBreakdown> moves = super.move(d);
        canMove2 = false;
        return moves;
    }

    /**
     * The system of promoting pawns in this twisted version of Chess is completely random. You cannot choose which piece you want to promote to
     * @return
     * @throws IOException
     */
    public ChessPiece promote() throws IOException {
        if(pos.getX() == 7 && this.getColor() == PlayerType.BLACK || pos.getX() == 0 && this.getColor() == PlayerType.WHITE) {
            int whichOne = (int)(Math.random() * 4);
            String iconPrefix = "images/" + (this.getColor() == PlayerType.BLACK ? "b" : "w");
            ChessPiece[] potentials = {new Queen(this.getColor(), null, iconPrefix + "queen.png"),
                    new Knight(this.getColor(), null, iconPrefix + "knight.png"),
                    new Bishop(this.getColor(), null, iconPrefix + "bishop.png"),
                    new Rook(this.getColor(), null, iconPrefix + "rook.png")};
            return potentials[whichOne];
        }
        else return null;
    }

    @Override
    public ChessPiece cloneForOpponent() throws CloneNotSupportedException, IOException {
        Pawn p = (Pawn)super.cloneForOpponent();
        p.justMoved2 = false;
        p.canMove2 = false;
        return p;
    }
}