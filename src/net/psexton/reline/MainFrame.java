/*
 * MainFrame.java, part of the reline project
 * Created on May 14, 2014, 12:02:03 PM
 */

package net.psexton.reline;

/**
 *
 * @author PSexton
 */
public class MainFrame extends javax.swing.JFrame {
    private final Model model = new Model();

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        intervalLabel = new javax.swing.JLabel();
        interval = new javax.swing.JSpinner();
        runStop = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reline");

        intervalLabel.setText("Interval (seconds):");

        interval.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(15), Integer.valueOf(1), null, Integer.valueOf(1)));

        runStop.setText("Run");
        runStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runStopActionPerformed(evt);
            }
        });

        jScrollPane1.setEnabled(false);

        console.setColumns(20);
        console.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        console.setRows(5);
        console.setEnabled(false);
        jScrollPane1.setViewportView(console);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(intervalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(interval, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                        .addComponent(runStop, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(interval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intervalLabel)
                    .addComponent(runStop))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runStopActionPerformed
        boolean isStarting = runStop.isSelected();

        // Flip component enabling
        interval.setEnabled(!isStarting);
        intervalLabel.setEnabled(!isStarting);
        jScrollPane1.setEnabled(isStarting);
        console.setEnabled(isStarting);
        
        // Either start or stop the app
        if(isStarting) {
            // start
            model.startMonitor((Integer) interval.getModel().getValue(), console);
            runStop.setText("Stop");
        }
        else {
            // stop
            model.stopMonitor();
            runStop.setText("Run");
        }
    }//GEN-LAST:event_runStopActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea console;
    private javax.swing.JSpinner interval;
    private javax.swing.JLabel intervalLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton runStop;
    // End of variables declaration//GEN-END:variables
}
