package org.edgemont.csap.finalproject;

import java.awt.Graphics;
import java.awt.Color;


public class Position implements Cloneable
{
    private int x, y;
    private ChessPiece piece;

    public Position(int x1, int y1) {
        x = x1;
        y = y1;
        piece = null;
    }

    public Position(int x1, int y1, ChessPiece p) {
        x = x1;
        y = y1;
        piece = p;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ChessPiece getPiece() {return piece;}

    public void setPiece(ChessPiece p) {piece = p;}

    public void setX(int val) { x = val;}

    public void setY(int val) { y = val;}

    public boolean isOccupied() {
        return (piece != null);
    }

    public void addComponent(Graphics g, int color) {
        if(color % 2 == 0) g.setColor(new Color(255,255,255));
        else g.setColor(new Color(0, 0, 0));
        g.fillRect(x,y,50,50);
        piece.disp(g);
    }

    @Override
    public Position clone() {
        try {
            Position clone = (Position) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Position(" + x + ", " + y + ", piece:" + getPiece() + ")";
    }
}
