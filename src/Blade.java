import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Blade {

    private Team team;
    private Point2D.Double pos;
    private Rectangle2D.Double displayedBounds;
    public static final double WIDTH, HEIGHT;

    static {
        WIDTH = 1.0/80.0;
        HEIGHT = 3.0/25.0;
    }

    public Blade(Team t, double x, double y) {
        team = t;
        pos = new Point2D.Double(x,y);
        displayedBounds = new Rectangle2D.Double();
    }

    public boolean intersects(Ball b) {
        return (displayedBounds.intersects(b.getBounds()));
    }

    public double getBounceAngle(Ball b) {
        double y = b.getBounds().y;
        double diff = y-displayedBounds.getCenterY();
        diff /= displayedBounds.getHeight();
        return diff/100.0;
    }

    public void inflate(int w, int h) {
        displayedBounds.setRect(w * pos.x, h * pos.y, WIDTH * w, HEIGHT * h);
    }

    public void draw(Graphics2D g) {
        g.fill(displayedBounds);
    }

    public void moveBy(double vel, int displayedHeight) {
        if (vel != 0) {
            pos.y += vel;
            if (pos.y < 1 / 25.0) pos.y = 1 / 25.0;
            if (pos.y > 21 / 25.0) pos.y = 21 / 25.0;
            displayedBounds.y = displayedHeight * pos.y;
        }
    }
}
