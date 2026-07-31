import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Brick {
    private Point2D.Double pos;
    private Rectangle2D.Double displayedRectangle;
    public static final double WIDTH, HEIGHT;

    static {
        WIDTH = 1.0/80.0;
        HEIGHT = 1.0/25.0;
    }

    public Brick(double x, double y) {
        pos = new Point2D.Double(x,y);
        displayedRectangle = new Rectangle2D.Double();
    }

    public void inflate(int screenWidth, int screenHeight) {
        displayedRectangle.setRect(screenWidth * pos.x, screenHeight * pos.y, screenWidth*WIDTH, screenHeight*HEIGHT);
    }

    public void draw(Graphics2D g) {
        g.fill(displayedRectangle);
    }

    public boolean intersects(Ball b) {
        return (b.getBounds().intersects(displayedRectangle));
    }


}
