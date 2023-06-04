import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * ChessPositionCollection is a singleton class that defines all positions of the ChessBoard
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ChessPositionCollection {    
    private final Position[][] positions;

    
    public ChessPositionCollection() throws IOException {
        //row is x, and column (file) is y. Java starts the board/array from upper left corner, i.e. (0,0) is black 8a
        this.positions = new Position[8][8];
        // initialize all 8x8 positions
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                this.positions[i][j] = new Position(i, j); // position x / y starts with 0-7
            }
        }
    }

    public int getRowCount() { return 8;}
    public int getColCount() { return 8;}
        
    public Position getPosition(int x, int y) {
        if (x >= getRowCount() || x < 0 || y >= getColCount() || y < 0) return null;
        return this.positions[x][y];
    }
}
