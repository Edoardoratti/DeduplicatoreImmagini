package deduplicatore.gui;

import deduplicatore.Deduplicatore;
import java.awt.Desktop;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static javax.swing.SwingUtilities.updateComponentTreeUI;

/**
 *
 * @author edoardo.ratti
 * @version 30.09.22
 *
 */
public class MainFrame extends JFrame {

    private Deduplicatore dec;
    private int buttonState = 0;
    private boolean threadSuspended = false;
    private String root;

    public MainFrame() {
        initComponents();
        this.setSize(1100, 700);
    }
                        
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        imageMenuPanel1 = new deduplicatore.gui.ImageMenuPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Inserisci percorso cartella immagini");

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        this.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                percentageChange(evt);
            }
        });
        imageMenuPanel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                downloadImage(evt);
            }
        });

        jLabel2.setText("");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Deduplicator");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(imageMenuPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(jButton1)
                                                                .addGap(35, 35, 35)
                                                                .addComponent(jLabel2))
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(62, 62, 62)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1)
                                                        .addComponent(jLabel2)))
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(imageMenuPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                         

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        Runnable analizzator = new Runnable() {
            public void run() {
                root = jTextField1.getText();
                root = "E://306//Immagini//Test//test//Test";
                //root = "E://306//Immagini//Test";
                //root = "E://306//Immagini//Test//test";
                dec = new Deduplicatore(root);
                dec.startProgram(dec);
                imageMenuPanel1.setDeduplicatore(dec);
                imageMenuPanel1.displaySeries();
                jButton1.setText("Start");
                buttonState = 0;
            }
        };
        Thread t = new Thread(analizzator);

        switch (buttonState) {
            case 0:
                imageMenuPanel1.switchJToolBarButtons(false);
                t.start();
                jButton1.setText("Stop"); //start statement
                buttonState++;
                break;
            case 1:
                jButton1.setText("Resume"); //stop statement
                switchSuspend();
                buttonState++;
                
//                synchronized (t) {
//                    while (threadSuspended) {
//                        try {
//                            t.wait();
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }   
                t.suspend();
                break;

            case 2:
                jButton1.setText("Stop"); //resume statement
                switchSuspend();
                buttonState--;
                t.resume();
                break;
            default:
                break;
        }

    }//GEN-LAST:event_jButton1ActionPerformed
    
    public synchronized void switchSuspend() {
        threadSuspended = !threadSuspended;

        if (!threadSuspended) {
            notify();
        }
    }
    
    private void percentageChange(java.beans.PropertyChangeEvent evt) {
        float perc = 0;
        jLabel2.setText(perc + "%");
        Object percentage = evt.getNewValue();
        if (percentage instanceof Float) {
            perc = (Float)percentage;
            System.out.println("Percentuale: " + Math.round(perc) + "%");
            jLabel2.setText(perc +  ""); // genera errore
            jLabel2.updateUI();
            updateComponentTreeUI(this);
            //repaint();
        }
    }
    
    private void downloadImage(java.beans.PropertyChangeEvent evt){
        try {
            Object obj = evt.getNewValue();
            
            if(obj instanceof Path){
                Path o = Paths.get(obj.toString());
//                System.out.println("File to Copy: " + o);
                 Desktop.getDesktop().open(new File(o.toString()));
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unexitstent path");
        }
    }

    public static void main(String args[]) throws IOException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private deduplicatore.gui.ImageMenuPanel imageMenuPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
