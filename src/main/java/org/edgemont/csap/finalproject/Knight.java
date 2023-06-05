package org.edgemont.csap.finalproject;

import org.edgemont.csap.finalproject.ChessPiece;
import org.edgemont.csap.finalproject.ChessPositionCollection;

import java.io.IOException;
import java.util.ArrayList;

public class Knight extends ChessPiece
{
    private final boolean move2 = true;
    
    public Knight(PlayerType color, Position x, String iconFile) throws IOException {
        super(color, "org.edgemont.csap.finalproject.Knight", x, iconFile);
    }
    
    public ArrayList<Position> getLegalMoves() throws IOException {
        ArrayList<Position> legals = new ArrayList<Position>();
        ChessPositionCollection posCollection = ChessBoard.getInstance().getPositionCollection();
        Position p = posCollection.getPosition(pos.getX() + 1, pos.getY() + 2);
        if (isLegalPos(p)) legals.add(p);

        p = posCollection.getPosition(pos.getX() + 1, pos.getY() - 2);
        if (isLegalPos(p)) legals.add(p);
        p = posCollection.getPosition(pos.getX() + 2, pos.getY() +1);
        if (isLegalPos(p)) legals.add(p);
        p = posCollection.getPosition(pos.getX() + 2, pos.getY() - 1);
        if (isLegalPos(p)) legals.add(p);
        p = posCollection.getPosition(pos.getX() - 1, pos.getY() - 2);
        if (isLegalPos(p)) legals.add(p);
        p = posCollection.getPosition(pos.getX() - 1, pos.getY() + 2);
        if (isLegalPos(p)) legals.add(p);
        p = posCollection.getPosition(pos.getX() - 2, pos.getY() - 1);
        if (isLegalPos(p)) legals.add(p);
        p = posCollection.getPosition(pos.getX() - 2, pos.getY() +1);
        if (isLegalPos(p)) legals.add(p);
        return legals;
    }
}
