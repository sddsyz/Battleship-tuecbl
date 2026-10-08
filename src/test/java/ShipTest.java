
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {

    private Ship ship1 = new Ship("Carrier", 5);
    private Ship ship2 = new Ship("Cruiser", 3);

    @Test
    void sizeIsCorrect() {
        assertEquals(5, ship1.getSize());
        assertEquals(3, ship2.getSize());
    }

    @Test
    void typeIsCorrect() {
        assertEquals("Carrier", ship1.getType());
        assertEquals("Cruiser", ship2.getType());
    }

    @Test
    void isntSunkWhenHitLessThanSize() {
        for (int i = 1; i < 5; i++) {
            ship1.hit();
        }
        assertFalse(ship1.sunk());
        
        for (int i = 1; i < 3; i++) {
            ship2.hit();
        }
        assertFalse(ship2.sunk());
    }
    
    @Test
    void isSunkWhenHitEqualSize() {
        for (int i = 1; i <= 5; i++) {
            ship1.hit();
        }
        assertTrue(ship1.sunk());
        
        for (int i = 1; i <= 3; i++) {
            ship2.hit();
        }
        assertTrue(ship2.sunk());
    }
}
