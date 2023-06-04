import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ResizeImageExample extends JPanel {

    private BufferedImage originalImage;
    private BufferedImage resizedImage;

    public ResizeImageExample() {
        try {
            // Load the original image from file
            originalImage = ImageIO.read(getClass().getResource("src/images/image.jpg"));

            // Resize the image
            int newWidth = 200; // New width of the image
            int newHeight = 200; // New height of the image
            resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the resized image on the graphics surface
        if (resizedImage != null) {
            int x = (getWidth() - resizedImage.getWidth()) / 2;
            int y = (getHeight() - resizedImage.getHeight()) / 2;
            g.drawImage(resizedImage, x, y, null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Resize Image Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.add(new ResizeImageExample());
            frame.setVisible(true);
        });
    }
}