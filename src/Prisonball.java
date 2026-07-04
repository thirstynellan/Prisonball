import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Sound effect credits:
// https://pixabay.com/users/u_u4pf5h7zip-50459877/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=345983


public class Prisonball extends JPanel implements KeyListener {

    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private List<Wall> walls;
    private Border border;
    private List<Ball> balls;
    private boolean dirty;
    private int width, height;
    private int leftScore, rightScore;
    private int timeRemaining;
    private Timer tim;
    private Font textFont;
    private int timerHack;
    private Clip leftBounceSound, rightBounceSound;
    private Clip[] brickBreakSound;
    private int brickSoundIndex;
    private boolean firstTime;
    private Thread openingMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            if (firstTime) {
                firstTime = false;
                tim.stop();
                JOptionPane.showMessageDialog(Prisonball.this, "Left paddle: W=up, S=down\nRight paddle: arrow keys\nPause: Spacebar\n\nClick OK to start!", "Prisonball", JOptionPane.INFORMATION_MESSAGE);
                tim.restart();
            }
        }
    });

    public Prisonball() {
        addKeyListener(this);
        dirty = true;
        firstTime = true;
        width = height = 0;
        leftPaddle = new Paddle(Team.LEFT);
        rightPaddle = new Paddle(Team.RIGHT);
        walls = new ArrayList<>();
        balls = new ArrayList<>();
        border = new Border();
        loadSoundEffects();
        tim = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPaddle.move(height);
                rightPaddle.move(height);
                balls.forEach(b -> b.move(width, height));
                checkBallBorderIntersection();
                checkBallPaddleIntersection();
                checkBallWallIntersection();
                checkForWraparound();
                checkForEndgame();
                timerHack++;
                if (timerHack > 100) {
                    timeRemaining--;
                    timerHack = 0;
                }
                if (timeRemaining == 20) {
                    border.flash();
                } else if (timeRemaining == 19) {
                    border.restore();
                }
                repaint();
            }
        });
        tim.setInitialDelay(100);
        startGame();
    }

    private void startGame() {
        timeRemaining = 180;
        timerHack = 0;
        leftScore = rightScore = 0;
        walls.clear();
        walls.add(new Wall(11/40.0, 1/25.0, Color.RED, 10));
        walls.add(new Wall(15/40.0, 1/25.0, Color.CYAN, 20));
        walls.add(new Wall(19/40.0, 1/25.0, Color.GREEN, 30));
        walls.add(new Wall(23/40.0, 1/25.0, Color.CYAN, 20));
        walls.add(new Wall(27/40.0, 1/25.0, Color.RED, 10));
        balls.clear();
        balls.add(new Ball());
        balls.add(new Ball());
        balls.add(new Ball());
        dirty = true;
        tim.start();
    }

    @Override
    public void paintComponent(Graphics g1) {
        requestFocusInWindow();
        Graphics2D g = (Graphics2D)g1;
        if (width != getWidth() || height != getHeight()) {
            dirty = true;
        }
        if (dirty) {
            width = getWidth();
            height = getHeight();
            walls.forEach(w -> w.inflate(width, height));
            balls.forEach(b -> b.inflate(width, height));
            leftPaddle.inflate(width, height);
            rightPaddle.inflate(width, height);
            border.rebuild(width, height);
            textFont = findThePerfectFontSize(g);
            dirty = false;
        }
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,width, height);
        walls.forEach(w -> w.draw(g));
        balls.forEach(b -> b.draw(g));
        leftPaddle.draw(g);
        rightPaddle.draw(g);
        border.draw(g);
        g.setColor(Color.DARK_GRAY);
        g.setFont(textFont);
        int textY = (int)(height/28.0)-1;
        g.drawString("SCORE: "+String.format("%05d", leftScore), (int)(width/40), textY);
        String rightScoreString = "SCORE: "+String.format("%05d", rightScore);
        g.drawString(rightScoreString, (int)(width*(39.0/40.0))-g.getFontMetrics().stringWidth(rightScoreString), textY);
        String timerString = "TIME: " + (timeRemaining / 60) + ":" + String.format("%02d",timeRemaining % 60);
        g.drawString(timerString, (width-g.getFontMetrics().stringWidth(timerString))/2, textY);
        if (firstTime) {
            openingMessage.start();
        }
    }


    public static void main(String args[]) {
        JFrame window = new JFrame("Prisonball");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 800);
        window.setContentPane(new Prisonball());
        window.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                rightPaddle.moveUp();
                break;
            case KeyEvent.VK_DOWN:
                rightPaddle.moveDown();
                break;
            case KeyEvent.VK_W:
                leftPaddle.moveUp();
                break;
            case KeyEvent.VK_S:
                leftPaddle.moveDown();
                break;
            case KeyEvent.VK_SPACE:
                pauseGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                rightPaddle.stopMovingUp();
                break;
            case KeyEvent.VK_DOWN:
                rightPaddle.stopMovingDown();
                break;
            case KeyEvent.VK_W:
                leftPaddle.stopMovingUp();
                break;
            case KeyEvent.VK_S:
                leftPaddle.stopMovingDown();
                break;
        }
    }

    private void pauseGame() {
        tim.stop();
        JOptionPane.showMessageDialog(this, "GAME PAUSED", "PrisonBall", JOptionPane.INFORMATION_MESSAGE);
        tim.restart();
    }

    private void checkBallBorderIntersection() {
        for (var b : balls) {
            if (border.intersects(b)) {
                //dirty hack
                var bounds = b.getBounds();
                if (bounds.y < height/25) {
                    b.forceDown();
                    b.bounceVertically();
                } else if (bounds.y > height*(23.0/25.0)) {
                    b.forceUp();
                    b.bounceVertically();
                } else {
                    b.bounceHorizontally();
                }
            }
        }
    }

    private void checkBallPaddleIntersection() {
        for (var b : balls) {
            if (leftPaddle.checkForIntersection(b)) {
                leftBounceSound.setFramePosition(0);
                leftBounceSound.start();
            }
            if (rightPaddle.checkForIntersection(b)) {
                rightBounceSound.setFramePosition(0);
                rightBounceSound.start();
            }
        }
    }

    private void checkForWraparound() {
        for (var b : balls) {
            double x = b.getBounds().x;
            if (x < 0 && b.isMovingLeft()) {
                b.setX(1);
            } else if (x > width && b.isMovingRight()) {
                b.setX(0);
            }
        }
    }

    private void checkBallWallIntersection() {
        for (var b : balls) {
            for (var w : walls) {
                if (w.checkForIntersection(b, width)) {
                    b.bounceHorizontally();
                    playBrickBreakSound();
                    if (b.getTeam() == Team.RIGHT) {
                        rightScore += w.getPointValue();
                    } else if (b.getTeam() == Team.LEFT) {
                        leftScore += w.getPointValue();
                    }
                    //bricks destroyed by neutral balls don't yield points
                    if (w.needsRegeneration()) {
                        w.regenerate();
                        w.inflate(width, height);
                    }
                }
            }
        }
    }

    private Font findThePerfectFontSize(Graphics2D g) {
        int size = 0;
        FontMetrics fm;
        do {
            size++;
            Font f = new Font(Font.SANS_SERIF, Font.BOLD, size);
            fm = g.getFontMetrics(f);
        } while (fm.getMaxAscent() < height/30.0);
        return new Font(Font.SANS_SERIF, Font.BOLD, size-1);
    }

    private void checkForEndgame() {
        if (timeRemaining == 0) {
            tim.stop();
            int playAgain = JOptionPane.showConfirmDialog(this, "GAME OVER! Play again?", "Prisonball", JOptionPane.YES_NO_OPTION);
            if (playAgain == JOptionPane.YES_OPTION) {
                startGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void playBrickBreakSound() {
        brickBreakSound[brickSoundIndex].setFramePosition(0);
        brickBreakSound[brickSoundIndex].start();
        brickSoundIndex = (brickSoundIndex + 1) % brickBreakSound.length;
    }

    private void loadSoundEffects() {
        brickSoundIndex = 0;
        try {
            var audioIn = AudioSystem.getAudioInputStream(new File("left_paddle.wav").toURI().toURL());
            leftBounceSound = AudioSystem.getClip();
            leftBounceSound.open(audioIn);
            audioIn = AudioSystem.getAudioInputStream(new File("right_paddle.wav").toURI().toURL());
            rightBounceSound = AudioSystem.getClip();
            rightBounceSound.open(audioIn);
            brickBreakSound = new Clip[6];
            for (int i=1; i<=6; i++) {
                audioIn = AudioSystem.getAudioInputStream(new File("brick_break" + i + ".wav").toURI().toURL());
                brickBreakSound[i-1] = AudioSystem.getClip();
                brickBreakSound[i-1].open(audioIn);
            }
        } catch (Exception e) {
            System.err.println("error loading sound effects");
        }
    }

}