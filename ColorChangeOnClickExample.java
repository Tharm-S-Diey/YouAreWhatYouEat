import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorChangeOnClickExample extends JPanel {

    private final Color defaultColor = Color.RED;
    private final Color clickedColor = Color.BLUE;
    private Color currentColor = defaultColor;

    public ColorChangeOnClickExample() {
        // Add a mouse listener to the component
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Change the color on mouse click
                if (currentColor == defaultColor) {
                    currentColor = clickedColor;
                } else {
                    currentColor = defaultColor;
                }
                repaint(); // Trigger a repaint to update the color
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(currentColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Color Change on Click Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.add(new ColorChangeOnClickExample());
            frame.setVisible(true);
        });
    }
}
