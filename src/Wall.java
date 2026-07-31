import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Wall {
    private int pointsPerBrick;
    private Color color;
    private List<Brick> bricks;
    private Point2D.Double pos;


    public Wall(double x, double y, Color c, int ppb) {
        pos = new Point2D.Double(x,y);
        color = c;
        pointsPerBrick = ppb;
        bricks = new ArrayList<>();
        regenerate();
    }

    public void regenerate() {
        for (int i=0; i<23; i++) {
            for (int j=0; j<4; j++) {
                bricks.add(new Brick(pos.x + j*Brick.WIDTH, pos.y + i*Brick.HEIGHT));
            }
        }
    }

    public void inflate(int w, int h) {
        bricks.forEach(b -> b.inflate(w,h));
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        bricks.forEach(b -> b.draw(g));
    }

    public int getPointValue() {
        return pointsPerBrick;
    }

    public boolean checkForIntersection(Ball b, int screenWidth) {
        double left = pos.x * screenWidth;
        double right = (pos.x+4*Brick.WIDTH) * screenWidth;
        boolean result = false;
        if (b.getBounds().getMaxX() > left && b.getBounds().getMinX() < right) {
            for (var brick : bricks) {
                if (brick.intersects(b)) {
                    bricks.remove(brick);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean needsRegeneration() {
        return bricks.isEmpty();
    }
}
