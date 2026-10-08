
public class Game {

    private Player player1;
    private Player player2;
    private Player current;

    public Game() {
        player1 = new Player("player1");
        player2 = new Player("player2");
        current = player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrent() {
        return current;
    }

    public void fire(int r, int c) {
        Player opponent;
        if (current == player1) {
            opponent = player2;
        } else {
            opponent = player1;
        }

        String result = opponent.getBoard().shootResult(r, c);
        switch (result) {
            case "invalid" -> {
                System.out.println("Invalid position.");
            }
            case "alreadyshoot" -> {
                System.out.println("This position has already been shot.");
            }
            case "sunk" -> {
                if (opponent.getBoard().allSunk()) {
                    System.out.println("Game over.");
                } else {
                    nextTurn();
                }
            }
            case "hit" -> {
                System.out.println("Hit.");
                nextTurn();
            }
            case "miss" -> {
                System.out.println("Miss.");
                nextTurn();
            }
        }
    }

    public void nextTurn() {
        
        if (current == player1) {
            current = player2;
        } else {
            current = player1;
        }
    }

    public Player winner() {
        if (player1.getBoard().allSunk()) {
            return player2;
        } else if (player2.getBoard().allSunk()) {
            return player1;
        }
        return null;
    }
}
