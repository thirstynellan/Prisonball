import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Brick {
    private Wall wall;
    private Point2D.Double pos;
    //private Point displayedPos;
    private Rectangle2D.Double displayedRectangle;
    public static final double WIDTH, HEIGHT;
    //private static int displayedWidth, displayedHeight;

    static {
        WIDTH = 1.0/80.0;
        HEIGHT = 1.0/25.0;
    }

    public Brick(Wall w, double x, double y) {
        wall = w;
        pos = new Point2D.Double(x,y);
        //displayedPos = new Point();
        displayedRectangle = new Rectangle2D.Double();
    }

//    public static void inflateSize(int w, int h) {
//        displayedWidth = (int)(WIDTH * w);
//        displayedHeight = (int)(HEIGHT * h);
//    }

    public void inflate(int screenWidth, int screenHeight) {
        //displayedPos.x = (int)(screenWidth * pos.x);
        //displayedPos.y = (int)(screenHeight * pos.y);
        displayedRectangle.setRect(screenWidth * pos.x, screenHeight * pos.y, screenWidth*WIDTH, screenHeight*HEIGHT);
    }

    public void draw(Graphics2D g) {
        //g.fillRect(displayedPos.x, displayedPos.y, displayedWidth, displayedHeight);
        g.fill(displayedRectangle);
    }

    public boolean intersects(Ball b) {
        return (b.getBounds().intersects(displayedRectangle));
    }


}
