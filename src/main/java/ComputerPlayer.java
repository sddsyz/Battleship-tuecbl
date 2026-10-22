
import java.awt.Point;
import java.util.Random;

public class ComputerPlayer {

    private final Random random = new Random();
    private boolean[][] shots = new boolean[10][10];

    public Point randomTarget() {
        int a, b;

        do {
            a = random.nextInt(10);
            b = random.nextInt(10);
        } while (shots[a][b]);

        shots[a][b] = true;
        return new Point(a, b);
    }
    
}
