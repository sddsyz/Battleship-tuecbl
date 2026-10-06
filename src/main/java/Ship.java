
import java.awt.Point;

public class Ship {

    private String type;
    private int size;
    private int shot;
    private Point startPoint;
    private boolean vertical;

    public Ship(String type, int size) {
        this.size = size;
        this.type = type;
        this.shot = 0;
        this.startPoint = null;
        this.vertical = true;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public Point getstartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public boolean isvertical() {
        return vertical;
    }

    public void setvertical(boolean vertical) {
        this.vertical = vertical;
    }

    public void hit() {
        shot++;
    }

    public boolean sunk() {
        return shot >= size;
    }
}
