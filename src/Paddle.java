import java.awt.*;

public class Paddle {
    private Blade[] blades;
    private double velocity;
    private Team team;
    private static final double INCREMENT = 1/100.0;

    public Paddle(Team t) {
        team = t;
        blades = new Blade[2];
        if (team == Team.LEFT) {
            blades[0] = new Blade(team, 1/40.0, 9/25.0);
            blades[1] = new Blade(team, 6/40.0, 9/25.0);
        } else {
            blades[0] = new Blade(team, 33.5/40.0, 9/25.0);
            blades[1] = new Blade(team, 38.5/40.0, 9/25.0);
        }
        velocity = 0;
    }

    public void moveUp() {
        velocity = -INCREMENT;
    }

    public void moveDown() {
        velocity = INCREMENT;
    }

    public void stopMovingDown() {
        if (velocity > 0) {
            velocity = 0;
        }
    }

    public void stopMovingUp() {
        if (velocity < 0) {
            velocity = 0;
        }
    }

    public void move(int screenHeight) {
        blades[0].moveBy(velocity, screenHeight);
        blades[1].moveBy(velocity, screenHeight);
    }

    public void draw(Graphics2D g) {
        g.setColor(team.getColor());
        blades[0].draw(g);
        blades[1].draw(g);
    }

    public void inflate(int w, int h) {
        blades[0].inflate(w,h);
        blades[1].inflate(w,h);
    }

    public boolean checkForIntersection(Ball b) {
        if ((team == Team.RIGHT && b.isMovingRight()) ||
                (team == Team.LEFT && b.isMovingLeft())) {
            if (blades[0].intersects(b) || blades[1].intersects(b)) {
                b.setTeam(team);
                b.bounceHorizontally(blades[0]);
                return true;
            }
        }
        return false;
    }



}
