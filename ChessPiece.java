import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Only here to make a foundation for any of the chess pieces that will be implemented later
 *
 * @author anderson zhang
 * @version 1.0
 */
public abstract class ChessPiece {
    private String fakeType;
    private PlayerType color; 
    public Position pos;
    private BufferedImage icon;

    public ChessPiece(PlayerType c, String ft, Position p, String iconFile) throws IOException {
        color = c;
        fakeType = ft;
        pos = p;
        System.out.println(iconFile);
        icon = ImageIO.read(Objects.requireNonNull(getClass().getResource(iconFile)));
    }

    /**
     * All the following methods are accessor and mutator methods.
     * @return
     */
    public String fakeType() {return fakeType;}
    public PlayerType getColor() {return color;}
    public BufferedImage getIcon() {return icon;}
    public Position getPos() {return pos;}
    public void setPos(Position p) { pos = p;}
    public void setFakeType(String d) {fakeType = d;}
    public void setPlayerType(PlayerType x) {color = x;}
    public void setImage(BufferedImage p) {icon = p;}


    public void disp(Graphics g) {
        int x = pos.getX();
        int y = pos.getY();

        g.drawImage(icon, x, y, null);
    }

    public abstract ArrayList<Position> getLegalMoves() throws IOException;

    public boolean canMove(Position d) throws IOException {
        return getLegalMoves().contains(d);
    }

    public void move(Position d) throws IOException {
        this.move(d, true);
    }

    /**
     * checkLegal is for the small chance of castling being legal. it is involved in forcing the rook to move to a certain position during castling
     * @param d
     * @param checkLegal
     * @throws IOException
     */
    public void move(Position d, boolean checkLegal) throws IOException {
        // If pawn, the caller should've changed the piece object
        // Assume that the move is legal, and canMove is already checked
        if (checkLegal) {
            ArrayList<Position> legals = getLegalMoves();
            if(legals.size() < 1 || !legals.contains(d)) {
                throw new IOException("Not a legal move");
            }
        }
        pos.setPiece(null);  // clear out current position
        pos = d;  // set the destination position
        d.setPiece(this);
    }

    public ChessPiece transform() throws IOException {
        return null;
    }

    protected boolean isLegalPos(Position p) {
        return (p != null && (p.getPiece() == null || p.getPiece() != null && p.getPiece().getColor() != this.color));
    }

}
