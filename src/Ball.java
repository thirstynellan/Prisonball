import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Ball {
    private Team team;
    private Rectangle2D.Double bounds;
    private Rectangle2D.Double displayedBounds;
    private Point2D.Double velocity;

    public Ball() {
        double x = (Math.random() < 0.5) ? 39/40.0 : 1/40.0;
        double y = 0.4 + Math.random() * 0.4;
        bounds = new Rectangle2D.Double(x,y,1/80.0,1/80.0);
        displayedBounds = new Rectangle2D.Double();
        team = Team.NEUTRAL;
        velocity = new Point2D.Double(-1/300.0, 1/300.0);
        if (x < 0.5) velocity.x *= -1;
        if (Math.random() < 0.5) velocity.y *= -1;
        //TODO THINK ABOUT THE INITIAL VELOCITY
    }

    public boolean isMovingRight() {
        return (velocity.x > 0);
    }

    public boolean isMovingLeft() {
        return (velocity.x < 0);
    }

    public void inflate(int w, int h) {
        displayedBounds.setRect(bounds.x*w, bounds.y*h, bounds.width*w, bounds.height*w);
    }

    public Rectangle2D.Double getBounds() {
        return displayedBounds;
    }

    public void setTeam(Team t) {
        team = t;
    }

    public Team getTeam() {
        return team;
    }

    public void draw(Graphics2D g) {
        g.setColor(team.getColor());
        g.fillOval((int)displayedBounds.x, (int)displayedBounds.y, (int)displayedBounds.width, (int)displayedBounds.height);
    }

//    public void bounce() {
//        velocity.x *= -1;
//        velocity.y *= -1;
//    }

    public void bounceVertically() {
        velocity.y *= -1;
    }

    public void bounceHorizontally() {
        velocity.x *= -1;
    }

    public void bounceHorizontally(Blade blade) {
        bounceHorizontally();
        velocity.y = blade.getBounceAngle(this);
    }

    public void move(int screenWidth, int screenHeight) {
        bounds.x += velocity.x;
        bounds.y += velocity.y;
        displayedBounds.x = bounds.x * screenWidth;
        displayedBounds.y = bounds.y * screenHeight;
    }

    public void setX(double x) {
        bounds.x = x;
    }
}
