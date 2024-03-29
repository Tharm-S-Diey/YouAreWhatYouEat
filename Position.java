import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;


public class Position
{
    private int x, y;
    private ChessPiece piece;
    private JPanel squareDisp;

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
}
