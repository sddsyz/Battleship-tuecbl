
import java.awt.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Ship ship1 = new Ship("Carrier", 5);
    private Ship ship2 = new Ship("Battleship", 4);
    private Ship ship3 = new Ship("Cruiser", 3);
    private Ship ship4 = new Ship("Submarine", 3);
    private Ship ship5 = new Ship("Destroyer", 2);
    private Board board = new Board();

    @Test
    void shipIsPlaced() {
        ship1.setStartPoint(new Point(4, 4));
        ship1.setvertical(false);
        assertTrue(board.validPlace(ship1));

        ship1.setvertical(true);
        assertTrue(board.validPlace(ship1));
    }

    @Test
    void shipOutsideBoard() {
        ship2.setStartPoint(new Point(4, 7));
        ship2.setvertical(true);
        assertFalse(board.validPlace(ship2));
        //outside the bottom

        ship5.setStartPoint(new Point(6, 9));
        ship5.setvertical(true);
        assertFalse(board.validPlace(ship5));
        //outside the bottom

        ship4.setStartPoint(new Point(8, 4));
        ship4.setvertical(false);
        assertFalse(board.validPlace(ship4));
        //outside the right

        ship5.setStartPoint(new Point(9, 3));
        ship5.setvertical(false);
        assertFalse(board.validPlace(ship5));
        //outside the right
    }

    @Test
    void shipIsOverlap() {
        ship1.setStartPoint(new Point(3, 3));
        ship5.setStartPoint(new Point(3, 2));
        assertTrue(board.placeShip(0, ship1));
        assertFalse(board.placeShip(1, ship5));
        //|/- overlap

        ship5.setStartPoint(new Point(2, 6));
        ship5.setvertical(false);
        assertFalse(board.placeShip(1, ship5));
        //+ overlap
    }

    @Test
    void missShoot() {
        ship1.setStartPoint(new Point(3, 3));
        board.placeShip(0, ship1);
        //display a ship

        assertEquals("miss", board.shootResult(2, 2));
        assertEquals("miss", board.shootResult(4, 2));
        assertEquals("miss", board.shootResult(8, 3));
        //hit empty square
    }

    @Test
    void alreadyShoot() {
        board.shootResult(5, 5);
        //first shoot
        assertEquals("alreadyshoot", board.shootResult(5, 5));
        //second shoot
    }

    @Test
    void hitShootAndSinkShoot() {
        ship4.setStartPoint(new Point(3, 3));
        board.placeShip(0, ship4);
        //display a ship

        assertEquals("hit", board.shootResult(3, 3));
        assertEquals("hit", board.shootResult(4, 3));
        assertEquals("sunk", board.shootResult(5, 3));
    }
    
    @Test
    void invalidShoot(){
    assertEquals("invalid", board.shootResult(3, 10));
    assertEquals("invalid", board.shootResult(10, 2));
    assertEquals("invalid", board.shootResult(-3, 1));
    }
}
