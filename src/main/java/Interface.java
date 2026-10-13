
import javax.swing.*;

public class Interface extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Interface.class.getName());

    private static final String[] shipnames = {"Carrier", "Battleship", "Cruiser",
        "Submarine", "Destroyer"};
    private static final int[] shipsizes = {5, 4, 3, 3, 2};
    private JButton[][] field = new JButton[10][10];

    private Ship[] fleet = new Ship[5];
    private boolean[] placed = new boolean[5];

    private int selectedShip = -1;
    private boolean vertical = true;
    private int nextShip = 0;

    private Board lastPlacedBoard = null;
    private int lastPlacedShip = -1;

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

    /**
     * Creates new form Interface
     */
    public Interface() {
        initComponents();

        java.awt.CardLayout layout
                = (java.awt.CardLayout) getContentPane().getLayout();

        layout.show(getContentPane(), "card3");

        setLocationRelativeTo(null);
    }

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

    private void placeThisShip(int r, int c) {

        if (selectedShip < 0 || selectedShip >= placed.length) {
            jLabel1.setText("Select a ship first.");
            return;
        }

        if (placed[selectedShip]) {
            jLabel1.setText("You've placed this ship.");
            return;
        }

        Ship ship = new Ship(shipnames[selectedShip], shipsizes[selectedShip]);
        ship.setStartPoint(new java.awt.Point(c, r));
        ship.setvertical(vertical);
        Board board = game.getCurrent().getBoard();

        if (!board.placeShip(selectedShip, ship)) {
            jLabel1.setText("Invalid placement.");
            return;
        }

        fleet[selectedShip] = ship;
        placed[selectedShip] = true;
        
        lastPlacedShip = selectedShip;
    lastPlacedBoard = board;

        colorField();

    }

    private void makeGrid() {
        //create button array to interact
        board1.removeAll();

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

        if (selectedShip >= 0 && selectedShip < placed.length && !placed[selectedShip]
                && mouser >= 0 && mousec >= 0) {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayPanel1 = new javax.swing.JPanel();
        board1 = new javax.swing.JPanel();
        fleet1 = new javax.swing.JPanel();
        orientationButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        finishDisplay = new javax.swing.JButton();
        startPanel = new javax.swing.JPanel();
        startButton1 = new javax.swing.JButton();
        mainTitle1 = new javax.swing.JLabel();

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

        jLabel1.setText("jLabel1");

        finishDisplay.setText("Finish");
        finishDisplay.addActionListener(this::finishDisplayActionPerformed);

        javax.swing.GroupLayout displayPanel1Layout = new javax.swing.GroupLayout(displayPanel1);
        displayPanel1.setLayout(displayPanel1Layout);
        displayPanel1Layout.setHorizontalGroup(
            displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(displayPanel1Layout.createSequentialGroup()
                .addGroup(displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(undoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orientationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(displayPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fleet1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addGroup(displayPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(finishDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
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
                        .addComponent(fleet1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(orientationButton)
                        .addGap(18, 18, 18)
                        .addComponent(undoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(displayPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(finishDisplay))
                        .addGap(4, 4, 4))))
        );

        getContentPane().add(displayPanel1, "card2");

        startPanel.setPreferredSize(new java.awt.Dimension(300, 200));

        startButton1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        startButton1.setText("START");
        startButton1.addActionListener(this::startButton1ActionPerformed);

        mainTitle1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 36)); // NOI18N
        mainTitle1.setText("BATTLESHIP");

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(187, 187, 187)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainTitle1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                        .addComponent(startButton1)
                        .addGap(50, 50, 50)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(mainTitle1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(startPanel, "card3");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        // TODO add your handling code here:
         if (lastPlacedShip == -1 || lastPlacedBoard == null) {
        jLabel1.setText("Nothing to undo.");
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

        jLabel1.setText("Ship placement undone.");

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
        // TODO add your handling code here:
        game = new Game();
        java.util.Arrays.fill(placed, false);
        java.util.Arrays.fill(fleet, null);

        selectedShip = -1;
        vertical = true;
        mouser = -1;
        mousec = -1;
        orientationButton.setText("Vertical");
        jLabel1.setText("Select a ship to begin.");
        finishDisplay.setEnabled(true);

        makeGrid();
        shipList();

        java.awt.CardLayout layout
                = (java.awt.CardLayout) getContentPane().getLayout();

        layout.show(getContentPane(), "card2");
    }//GEN-LAST:event_startButton1ActionPerformed

    private void finishDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishDisplayActionPerformed
        // TODO add your handling code here:
        if (!allShipsPlaced()) {
            jLabel1.setText("You've not placed all five ships.");
            return;
        }
        if (game.getCurrent() == game.getPlayer1()) {

            game.nextTurn();

            // Reset GUI placement state for Player 2.
            java.util.Arrays.fill(placed, false);
            java.util.Arrays.fill(fleet, null);

            selectedShip = -1;
            mouser = -1;
            mousec = -1;
            vertical = true;

            orientationButton.setText("Vertical");
            jLabel1.setText("Player 2: Select a ship to begin.");

            // Rebuild the board and ship list for Player 2.
            makeGrid();
            shipList();

        } else {
            // Player 2 has finished.
            jLabel1.setText("Both players have placed their ships.");

            finishDisplay.setEnabled(false);

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Setup complete! Both players are ready."
            );
        }
    }//GEN-LAST:event_finishDisplayActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Interface().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel board1;
    private javax.swing.JPanel displayPanel1;
    private javax.swing.JButton finishDisplay;
    private javax.swing.JPanel fleet1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel mainTitle1;
    private javax.swing.JButton orientationButton;
    private javax.swing.JButton startButton1;
    private javax.swing.JPanel startPanel;
    private javax.swing.JButton undoButton;
    // End of variables declaration//GEN-END:variables
}
