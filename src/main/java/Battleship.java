
import javax.swing.*;
import java.awt.Point;

public class Battleship extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Battleship.class.getName());

    //eachone's ships and board basic data
    private static final String[] shipnames = {"Carrier", "Battleship", "Cruiser",
        "Submarine", "Destroyer"};
    private static final int[] shipsizes = {5, 4, 3, 3, 2};
    private JButton[][] field = new JButton[10][10];
    private Ship[] fleet = new Ship[5];

    //variable for place ship phase
    private boolean[] placed = new boolean[5];
    private int selectedShip = -1;
    private boolean vertical = true;
    private int nextShip = 0;

    //for undo method
    private Board lastPlacedBoard = null;
    private int lastPlacedShip = -1;

    //show current mouse for coloring preview
    private int mouser;
    private int mousec;

    private boolean allShipsPlaced() {
        for (boolean isPlaced : placed) {
            if (!isPlaced) {
                return false;
            }
        }
        return true;
    }

    private Game game = new Game();

    public Battleship() {
        initComponents();
        java.awt.CardLayout layout = (java.awt.CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "card3");
        setLocationRelativeTo(null);
    }

    //list of ships to be placed
    private void shipList() {
        fleet1.removeAll();
        fleet1.setLayout(new java.awt.GridLayout(5, 1));
        for (int i = 0; i < 5; i++) {
            final int a = i;
            JButton s = new JButton(shipnames[a] + "--" + shipsizes[a]);
            s.addActionListener(e -> {
                if (!placed[a]) {
                    selectedShip = a;
                }
            });
            fleet1.add(s);
        }
    }

    //place one valid ship
    private void placeThisShip(int r, int c) {

        if (selectedShip < 0 || selectedShip >= placed.length) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a ship first.",
                    "Invalid selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (placed[selectedShip]) {
            JOptionPane.showMessageDialog(
                    this,
                    "You've placed this ship.",
                    "Invalid selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        //record data
        Ship ship = new Ship(shipnames[selectedShip], shipsizes[selectedShip]);
        ship.setStartPoint(new java.awt.Point(c, r));
        ship.setvertical(vertical);
        Board board = game.getCurrent().getBoard();

        if (!board.placeShip(selectedShip, ship)) {
            JOptionPane.showMessageDialog(
                    this,
                    "You can't place ship overlapped.",
                    "Invalid placement",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        fleet[selectedShip] = ship;
        placed[selectedShip] = true;

        lastPlacedShip = selectedShip;
        lastPlacedBoard = board;

        colorField();

    }

    //decide game mode
    private String mode() {
        return (String) modeSelect.getSelectedItem();
    }

    //for PVE
    private ComputerPlayer ai = new ComputerPlayer();
    private java.util.Random rd = new java.util.Random();

    //computer place ship
    private void aiPlace() {
        Board board = game.getPlayer2().getBoard();

        for (int i = 0; i < 5; i++) {
            boolean a = false;

            while (!a) {
                Ship ship = new Ship(shipnames[i], shipsizes[i]);

                int r = rd.nextInt(10);
                int c = rd.nextInt(10);
                boolean isVertical = rd.nextBoolean();

                ship.setStartPoint(new java.awt.Point(c, r));
                ship.setvertical(isVertical);

                a = board.placeShip(i, ship);
            }
        }
    }

    private void makeGrid() {
        //create button array to interact
        board1.removeAll();
        //reset area before each player start

        board1.setLayout(new java.awt.GridLayout(10, 10));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton cell = new JButton();
                final int a = i;
                final int b = j;
                //follow step can't work without declair final
                cell.addActionListener(e -> placeThisShip(a, b));

                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        mouser = a;
                        mousec = b;
                        colorField();
                    }
                });

                field[i][j] = cell;
                board1.add(cell);
            }
        }
        board1.revalidate();
        board1.repaint();
        colorField();
    }

    //distinguish empty square and ship square
    private void colorField() {
        Board board = game.getCurrent().getBoard();

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                field[r][c].setBackground(
                        java.awt.Color.WHITE
                );

                if (board.findShip(r, c) != null) {
                    field[r][c].setBackground(
                            java.awt.Color.GREEN
                    );
                }
            }
        }

        //valid to be placed ship
        if (selectedShip >= 0 && selectedShip < placed.length && !placed[selectedShip]
                && mouser >= 0 && mousec >= 0) {
            //for showing the preview of square occupied when placing the ship
            Ship preview = new Ship(
                    shipnames[selectedShip],
                    shipsizes[selectedShip]
            );

            preview.setStartPoint(
                    new java.awt.Point(mousec, mouser)
            );
            preview.setvertical(vertical);

            int length = shipsizes[selectedShip];
            for (int i = 0; i < length; i++) {
                int r;
                int c;

                if (vertical) {
                    r = mouser + i;
                    c = mousec;
                } else {
                    r = mouser;
                    c = mousec + i;
                }

                if (r >= 0 && r < 10 && c >= 0 && c < 10) {
                    field[r][c].setBackground(java.awt.Color.GREEN);
                }
            }
            board1.repaint();

        }
    }

    //for fire phase
    private JButton[][] player1Cells = new JButton[10][10];
    private JButton[][] player2Cells = new JButton[10][10];
    private boolean gameFinished = false;

    //create 2 fire area
    private void createGameCells() {
        gameBoard1.removeAll();
        gameBoard2.removeAll();

        gameBoard1.setLayout(new java.awt.GridLayout(10, 10));
        gameBoard2.setLayout(new java.awt.GridLayout(10, 10));

        player1Cells = new JButton[10][10];
        player2Cells = new JButton[10][10];

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                final int row = r;
                final int col = c;

                JButton p1Cell = new JButton();
                JButton p2Cell = new JButton();

                player1Cells[r][c] = p1Cell;
                player2Cells[r][c] = p2Cell;

                // player2 shoots at player1's board.
                p1Cell.addActionListener(e -> {
                    if (!gameFinished && game.getCurrent() == game.getPlayer2()
                            && "human vs human".equals(mode())) {
                        fireAt(row, col);
                    }
                });

                // player1 shoots at player2's board.
                p2Cell.addActionListener(e -> {
                    if (!gameFinished && game.getCurrent() == game.getPlayer1()) {
                        fireAt(row, col);
                    }
                });

                gameBoard1.add(p1Cell);
                gameBoard2.add(p2Cell);
            }
        }

        gameBoard1.revalidate();
        gameBoard2.revalidate();
        gameBoard1.repaint();
        gameBoard2.repaint();
    }

    private void refreshGameWindow() {
        Board p1Board = game.getPlayer1().getBoard();
        Board p2Board = game.getPlayer2().getBoard();

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {

                // text reminder for fire result
                player1Cells[r][c].setText(shotSymbol(p1Board, r, c));
                player2Cells[r][c].setText(shotSymbol(p2Board, r, c));

                player1Cells[r][c].setBackground(java.awt.Color.WHITE);
                player2Cells[r][c].setBackground(java.awt.Color.WHITE);

                //transparent button to show result "O" and "X"
                player1Cells[r][c].setOpaque(true);
                player2Cells[r][c].setOpaque(true);
            }
        }

        if (gameFinished) {
            currentTurn.setText("Game Finished");
        } else {
            currentTurn.setText("Turn: " + game.getCurrent().getName());
        }

        gameBoard1.repaint();
        gameBoard2.repaint();
    }

    private String shotSymbol(Board board, int r, int c) {
        //replace all squares a ship occupied by "#" when sunk
        Ship ship = board.findShip(r, c);
        if (ship != null && ship.sunk()) {
            return "#";
        }

        String result = board.getShotResult(r, c);

        if ("hit".equals(result)) {
            return "X";
        }
        if ("miss".equals(result)) {
            return "O";
        }
        return "";
    }

    private void fireAt(int r, int c) {
        if (gameFinished) {
            return;
        }

        Player shooter = game.getCurrent();
        String result = game.fire(r, c);

        shotResult.setText(shooter.getName() + ": " + result);

        if (end()) {
            return;
        }

        if ("human vs computer".equals(mode()) && game.getCurrent() == game.getPlayer2()) {
            Point shot = ai.randomTarget();
            String rs = game.fire(shot.y, shot.x);
            shotResult.setText("Player2: " + rs);
            if (end()) {
                return;
            }
            refreshGameWindow();
        }
    }

    private boolean end() {
        if (game.winner() != null) {
            gameFinished = true;
            winner.setText(game.winner().getName() + " WIN");
            endGame.setLocationRelativeTo(this);
            endGame.setVisible(true);
            refreshGameWindow();
            return true;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        endGame = new javax.swing.JDialog();
        winner = new javax.swing.JLabel();
        newGame = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        displayPanel1 = new javax.swing.JPanel();
        board1 = new javax.swing.JPanel();
        fleet1 = new javax.swing.JPanel();
        orientationButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        finishDisplay = new javax.swing.JButton();
        startPanel = new javax.swing.JPanel();
        startButton1 = new javax.swing.JButton();
        mainTitle1 = new javax.swing.JLabel();
        modeSelect = new javax.swing.JComboBox<>();
        firePanel = new javax.swing.JPanel();
        gameBoard2 = new javax.swing.JPanel();
        gameBoard1 = new javax.swing.JPanel();
        currentTurn = new javax.swing.JLabel();
        shotResult = new javax.swing.JLabel();
        title1 = new javax.swing.JLabel();
        title2 = new javax.swing.JLabel();

        endGame.setSize(new java.awt.Dimension(200, 200));

        winner.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        winner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        winner.setText("WIN");

        newGame.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        newGame.setText("New Game");
        newGame.addActionListener(this::newGameActionPerformed);

        exit.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(this::exitActionPerformed);

        javax.swing.GroupLayout endGameLayout = new javax.swing.GroupLayout(endGame.getContentPane());
        endGame.getContentPane().setLayout(endGameLayout);
        endGameLayout.setHorizontalGroup(
            endGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(endGameLayout.createSequentialGroup()
                .addGroup(endGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(endGameLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(winner, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(endGameLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(endGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newGame))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        endGameLayout.setVerticalGroup(
            endGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(endGameLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(winner)
                .addGap(19, 19, 19)
                .addComponent(newGame)
                .addGap(18, 18, 18)
                .addComponent(exit)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battleship");
        getContentPane().setLayout(new java.awt.CardLayout());

        displayPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        displayPanel1.setPreferredSize(new java.awt.Dimension(600, 400));

        board1.setBackground(new java.awt.Color(255, 255, 255));
        board1.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout board1Layout = new javax.swing.GroupLayout(board1);
        board1.setLayout(board1Layout);
        board1Layout.setHorizontalGroup(
            board1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        board1Layout.setVerticalGroup(
            board1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        fleet1.setBackground(new java.awt.Color(255, 255, 255));
        fleet1.setPreferredSize(new java.awt.Dimension(50, 200));

        javax.swing.GroupLayout fleet1Layout = new javax.swing.GroupLayout(fleet1);
        fleet1.setLayout(fleet1Layout);
        fleet1Layout.setHorizontalGroup(
            fleet1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        fleet1Layout.setVerticalGroup(
            fleet1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        orientationButton.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        orientationButton.setText("Vertical");
        orientationButton.addActionListener(this::orientationButtonActionPerformed);

        undoButton.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        undoButton.setText("Undo");
        undoButton.addActionListener(this::undoButtonActionPerformed);

        finishDisplay.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        finishDisplay.setText("Finish");
        finishDisplay.addActionListener(this::finishDisplayActionPerformed);

        javax.swing.GroupLayout displayPanel1Layout = new javax.swing.GroupLayout(displayPanel1);
        displayPanel1.setLayout(displayPanel1Layout);
        displayPanel1Layout.setHorizontalGroup(
            displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, displayPanel1Layout.createSequentialGroup()
                .addGroup(displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, displayPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(finishDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, displayPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fleet1, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, displayPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orientationButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                            .addComponent(undoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(45, 45, 45)
                .addComponent(board1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        displayPanel1Layout.setVerticalGroup(
            displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(displayPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(displayPanel1Layout.createSequentialGroup()
                        .addComponent(board1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(displayPanel1Layout.createSequentialGroup()
                        .addComponent(fleet1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(orientationButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(undoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finishDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );

        getContentPane().add(displayPanel1, "card2");

        startPanel.setPreferredSize(new java.awt.Dimension(300, 200));

        startButton1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        startButton1.setText("START");
        startButton1.addActionListener(this::startButton1ActionPerformed);

        mainTitle1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 36)); // NOI18N
        mainTitle1.setText("BATTLESHIP");

        modeSelect.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        modeSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "human vs human", "human vs computer" }));
        modeSelect.addActionListener(this::modeSelectActionPerformed);

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addContainerGap(296, Short.MAX_VALUE)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                        .addComponent(mainTitle1)
                        .addGap(315, 315, 315))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                        .addComponent(startButton1)
                        .addGap(369, 369, 369))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                        .addComponent(modeSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(281, 281, 281))))
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(mainTitle1)
                .addGap(18, 18, 18)
                .addComponent(modeSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(startButton1)
                .addContainerGap(212, Short.MAX_VALUE))
        );

        getContentPane().add(startPanel, "card3");

        gameBoard2.setBackground(new java.awt.Color(255, 255, 255));
        gameBoard2.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout gameBoard2Layout = new javax.swing.GroupLayout(gameBoard2);
        gameBoard2.setLayout(gameBoard2Layout);
        gameBoard2Layout.setHorizontalGroup(
            gameBoard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        gameBoard2Layout.setVerticalGroup(
            gameBoard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        gameBoard1.setBackground(new java.awt.Color(255, 255, 255));
        gameBoard1.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout gameBoard1Layout = new javax.swing.GroupLayout(gameBoard1);
        gameBoard1.setLayout(gameBoard1Layout);
        gameBoard1Layout.setHorizontalGroup(
            gameBoard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        gameBoard1Layout.setVerticalGroup(
            gameBoard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        currentTurn.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        currentTurn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentTurn.setText("Turn");

        shotResult.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        shotResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        shotResult.setText("No Result");

        title1.setText("PLAYER1");

        title2.setText("PLAYER2");

        javax.swing.GroupLayout firePanelLayout = new javax.swing.GroupLayout(firePanel);
        firePanel.setLayout(firePanelLayout);
        firePanelLayout.setHorizontalGroup(
            firePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(firePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(firePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gameBoard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(title1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(firePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gameBoard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(title2))
                .addGap(28, 28, 28))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, firePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(shotResult, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(firePanelLayout.createSequentialGroup()
                .addGap(335, 335, 335)
                .addComponent(currentTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        firePanelLayout.setVerticalGroup(
            firePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(firePanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(firePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(title1)
                    .addComponent(title2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(firePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gameBoard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gameBoard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentTurn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(shotResult)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(firePanel, "card4");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        // TODO add your handling code here:
        if (lastPlacedShip == -1 || lastPlacedBoard == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "You can only undo your final placing.",
                    "Nothing to undo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Remove the ship from the board
        Ship removed = lastPlacedBoard.removeShip(lastPlacedShip);

        if (removed != null) {
            // Return the ship to the selection list
            fleet[lastPlacedShip] = null;
            placed[lastPlacedShip] = false;

            // Clear the undo history
            lastPlacedShip = -1;
            lastPlacedBoard = null;
            selectedShip = -1;

            // Refresh the board display
            colorField();

            // Refresh the ship selection list if necessary
            shipList();
            fleet1.revalidate();
            fleet1.repaint();
        }
    }//GEN-LAST:event_undoButtonActionPerformed

    private void orientationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orientationButtonActionPerformed
        vertical = !vertical;

        if (vertical) {
            orientationButton.setText("Vertical");
        } else {
            orientationButton.setText("Horizontal");
        }
        // switch orientation
    }//GEN-LAST:event_orientationButtonActionPerformed

    private void startButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButton1ActionPerformed
        // reset all things before a new game
        game = new Game();
        ai = new ComputerPlayer();
        gameFinished = false;
        java.util.Arrays.fill(placed, false);
        java.util.Arrays.fill(fleet, null);

        selectedShip = -1;
        vertical = true;
        mouser = -1;
        mousec = -1;
        orientationButton.setText("Vertical");
        finishDisplay.setEnabled(true);

        makeGrid();
        shipList();

        java.awt.CardLayout layout
                = (java.awt.CardLayout) getContentPane().getLayout();

        layout.show(getContentPane(), "card2");
    }//GEN-LAST:event_startButton1ActionPerformed

    private void finishDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishDisplayActionPerformed
        // switch phase after placing all ships
        if (!allShipsPlaced()) {
            JOptionPane.showMessageDialog(
                    this,
                    "You've not placed all five ships.",
                    "Invalid finish",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        switch (mode()) {
            case "human vs human" -> {
                if (game.getCurrent() == game.getPlayer1()) {

                    game.nextTurn();

                    // reset all for Player2.
                    java.util.Arrays.fill(placed, false);
                    java.util.Arrays.fill(fleet, null);

                    selectedShip = -1;
                    mouser = -1;
                    mousec = -1;
                    vertical = true;

                    orientationButton.setText("Vertical");

                    makeGrid();
                    shipList();

                    return;
                }
                game.nextTurn();
            }
            case "human vs computer" -> {
                aiPlace();
                JOptionPane.showMessageDialog(
                        this,
                        "AI opponent secretly placed its ships, now start your fire.",
                        "Hint",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        }

        //reset all
        gameFinished = false;
        lastPlacedShip = -1;
        lastPlacedBoard = null;
        // switch to fire phase
        createGameCells();
        refreshGameWindow();
        java.awt.CardLayout layout = (java.awt.CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "card4");
    }//GEN-LAST:event_finishDisplayActionPerformed

    private void newGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameActionPerformed
        // return to start window
        endGame.setVisible(false);

        java.awt.CardLayout layout = (java.awt.CardLayout) getContentPane().getLayout();

        layout.show(getContentPane(), "card3");
    }//GEN-LAST:event_newGameActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // close all
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void modeSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modeSelectActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Battleship().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel board1;
    private javax.swing.JLabel currentTurn;
    private javax.swing.JPanel displayPanel1;
    private javax.swing.JDialog endGame;
    private javax.swing.JButton exit;
    private javax.swing.JButton finishDisplay;
    private javax.swing.JPanel firePanel;
    private javax.swing.JPanel fleet1;
    private javax.swing.JPanel gameBoard1;
    private javax.swing.JPanel gameBoard2;
    private javax.swing.JLabel mainTitle1;
    private javax.swing.JComboBox<String> modeSelect;
    private javax.swing.JButton newGame;
    private javax.swing.JButton orientationButton;
    private javax.swing.JLabel shotResult;
    private javax.swing.JButton startButton1;
    private javax.swing.JPanel startPanel;
    private javax.swing.JLabel title1;
    private javax.swing.JLabel title2;
    private javax.swing.JButton undoButton;
    private javax.swing.JLabel winner;
    // End of variables declaration//GEN-END:variables
}
