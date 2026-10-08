
import java.awt.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game = new Game();

    @Test
    void firstTurn() {
        assertEquals(game.getPlayer1(), game.getCurrent());
        //player1 start turn1
    }

    @Test
    void switchTurn() {
        //means current player done a legal shoot

        game.fire(0, 0);
        assertEquals(game.getPlayer2(), game.getCurrent());
    }

    @Test
    void notSwitchTurn() {
        //means current player done a illegal shoot

        game.fire(-1, 0);
        assertEquals(game.getPlayer1(), game.getCurrent());
        //P1 shoot outside board

        game.fire(0, 0);
        game.fire(0, 0);
        game.fire(0, 0);
        assertEquals(game.getPlayer1(), game.getCurrent());
        //P1's 2nd shoot isn't valid
    }

    @Test
    void shootResult() {
        Ship ship1 = new Ship("Carrier", 5);
        ship1.setStartPoint(new Point(2, 2));
        game.getPlayer2().getBoard().placeShip(0, ship1);
        //display a ship for P2

        assertEquals("hit", game.getPlayer2().getBoard().shootResult(2, 2));
    }
}
