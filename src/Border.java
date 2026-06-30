import java.awt.*;
import java.awt.geom.Path2D;

public class Border {
    private Polygon topBorder;
    private Polygon bottomBorder;
    private boolean funky;

    public Border() {
        topBorder = new Polygon();
        bottomBorder = new Polygon();
        funky = false;
    }

    public void rebuild(int w, int h) {
        topBorder.reset();
        topBorder.addPoint(0, (int)(h*6.0/25.0));
        topBorder.addPoint((int)(w/40.0), (int)(h*6.0/25.0));
        topBorder.addPoint((int)(w/40.0), (int)(h/25.0));
        topBorder.addPoint((int)(w*39.0/40.0), (int)(h/25.0));
        topBorder.addPoint((int)(w*39.0/40.0), (int)(h*6.0/25.0));
        topBorder.addPoint(w, (int)(h*6.0/25.0));
        topBorder.addPoint(w, 0);
        topBorder.addPoint(0, 0);
        bottomBorder.reset();
        bottomBorder.addPoint(0, (int)(h*19.0/25.0));
        bottomBorder.addPoint((int)(w/40.0), (int)(h*19.0/25.0));
        bottomBorder.addPoint((int)(w/40.0), (int)(h*24.0/25.0));
        bottomBorder.addPoint((int)(w*39.0/40.0), (int)(h*24.0/25.0));
        bottomBorder.addPoint((int)(w*39.0/40.0), (int)(h*19.0/25.0));
        bottomBorder.addPoint(w, (int)(h*19.0/25.0));
        bottomBorder.addPoint(w, h);
        bottomBorder.addPoint(0, h);
    }

    public void flash() {
        funky = true;
    }

    public void restore() {
        funky = false;
    }

    public void draw(Graphics2D g) {
        if (funky) {
            g.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillPolygon(topBorder);
        g.fillPolygon(bottomBorder);
    }

    public boolean intersects(Ball b) {
        return (topBorder.intersects(b.getBounds()) || bottomBorder.intersects(b.getBounds()));
    }
}
