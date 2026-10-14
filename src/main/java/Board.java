
import java.awt.Point;

public class Board {

    private static final int bsize = 10;
    //board bsize 10*10
    private static final int fleet = 5;
    //5 ships for each player
    private Ship[] ships;
    private boolean[][] shots;
    private String[][] shotResults;

    public Board() {
        ships = new Ship[fleet];
        //array of all ships(include occupied spaces)
        shots = new boolean[bsize][bsize];
        //position of enermy's hit
        shotResults = new String[bsize][bsize];
    }

    public boolean placeShip(int index, Ship ship) {

        if (ship == null) {
            return false;
        }

        if (index < 0 || index >= ships.length) {
            return false;
        }

        if (ship.getstartPoint() == null) {
            return false;
        }

        if (!validPlace(ship)) {
            return false;
        }

        if (ships[index] != null) {
            return false;
        }
        ships[index] = ship;

        return true;
    }

    public Ship removeShip(int index) {
        if (index < 0 || index >= ships.length) {
            return null;
        }

        Ship removed = ships[index];
        ships[index] = null;
        return removed;
    }

    public String shootResult(int r, int c) {
        if (r < 0 || r >= bsize || c < 0 || c >= bsize) {
            return "invalid";
        }
        //outside the board

        if (shots[r][c]) {
            return "alreadyshoot";
        }
        //has been shot

        shots[r][c] = true;

        Ship ship = findShip(r, c);
        if (ship != null) {
            ship.hit();
            if (ship.sunk()) {
                shotResults[r][c] = "sunk";
                return "sunk";
            } else {
                shotResults[r][c] = "hit";
                return "hit";
            }
        }
        shotResults[r][c] = "miss";
        return "miss";
    }

    public String getShotResult(int r, int c) {
        if (r < 0 || r >= bsize || c < 0 || c >= bsize) {
            return "invalid";
        }

        return shotResults[r][c];
    }

    public boolean validPlace(Ship ship) {
        int r = ship.getstartPoint().y;
        int c = ship.getstartPoint().x;

        for (int i = 0; i < ship.getSize(); i++) {

            if (r < 0 || c < 0 || r >= bsize || c >= bsize) {
                return false;
            }
            //position outside the board

            if (hasShip(r, c)) {
                return false;
            }
            //position occupied by others

            if (ship.isvertical()) {
                r++;
            } else {
                c++;
            }
        }
        return true;
    }

    public boolean hasShip(int r, int c) {
        for (int n = 0; n < ships.length; n++) {

            if (ships[n] == null) {
                continue;
            }

            int nr = ships[n].getstartPoint().y;
            int nc = ships[n].getstartPoint().x;

            for (int i = 0; i < ships[n].getSize(); i++) {
                if (nr == r && nc == c) {
                    return true;
                }
                if (ships[n].isvertical()) {
                    nr++;
                } else {
                    nc++;
                }
            }
        }
        return false;
    }

    public Ship findShip(int r, int c) {

        for (int n = 0; n < ships.length; n++) {

            if (ships[n] == null) {
                continue;
            }

            int nr = ships[n].getstartPoint().y;
            int nc = ships[n].getstartPoint().x;

            for (int i = 0; i < ships[n].getSize(); i++) {
                if (nr == r && nc == c) {
                    return ships[n];
                }
                if (ships[n].isvertical()) {
                    nr++;
                } else {
                    nc++;
                }
            }
        }
        return null;

    }

    public boolean allSunk() {
        for (int n = 0; n < ships.length; n++) {

            if (ships[n] == null) {
                return false;
            }
            if (!ships[n].sunk()) {
                return false;
            }
        }
        return true;
    }
}
