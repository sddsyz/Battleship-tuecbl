
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

    public String fire(int r, int c) {
        Player opponent;
        if (current == player1) {
            opponent = player2;
        } else {
            opponent = player1;
        }

        String result = opponent.getBoard().shootResult(r, c);
        switch (result) {
            case "invalid" -> {
                return "Invalid position";
            }
            case "alreadyshoot" -> {
                return "This position has already been shot";
            }
            case "sunk" -> {
                if (opponent.getBoard().allSunk()) {
                    return "Game over";
                } else {
                    nextTurn();
                    return "Sunk";
                }
            }
            case "hit" -> {                
                nextTurn();
                return "Hit";
            }
            case "miss" -> {
                nextTurn();
                return "Miss";
            }
        }
        return "Unknown";
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
