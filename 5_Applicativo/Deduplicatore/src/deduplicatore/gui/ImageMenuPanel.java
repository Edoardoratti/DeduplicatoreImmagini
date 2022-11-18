package deduplicatore.gui;

import deduplicatore.Deduplicatore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import javax.swing.border.LineBorder;

/**
 * Pannello che mostra le analisi sulle immagini
 *
 * @author edoardo.ratti
 */
public class ImageMenuPanel extends JPanel {
    
    Deduplicatore dec;
    
    public ImageMenuPanel() {
        initComponents();
    }
    
    public void setDeduplicatore(Deduplicatore d){
        dec = d;
    }

    public void displaySeries() {
        updateComponentTreeUI(this.getParent());
        seriesPanel.removeAll();
        filesPanel.removeAll();

        for (int i = 0; i < dec.misuration.length; i++) {
            if (dec.misuration[i][0] != -2) {
                JButton button = new JButton(dec.images.get(i).getName());
                seriesPanel.add(button);

                button.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        seriesButtonPressedEvent(evt);
                    }
                });
            }
        }

        //seriesScrollPane.setViewportBorder(new LineBorder(Color.RED));
    }

    private void seriesButtonPressedEvent(java.awt.event.ActionEvent evt){
            updateComponentTreeUI(this.getParent());
            
            //Index finding
            String name = ((JButton)evt.getSource()).getText();
            File file = new File(name);
            int index = 0;
            for (File f : dec.images) {
                if(f.getName().equals(file.getName())){
                    index = dec.images.lastIndexOf(f);
                }
            }
            //buttons creation
            filesPanel.removeAll();
            for (int i = 0; i < dec.misuration.length; i++) {
                int misure = dec.misuration[index][i];
                if (misure >= Deduplicatore.TOLLERANCE) {
                    JButton button = new JButton(dec.images.get(i).getName());
                    filesPanel.add(button);
                }
            }
        try {
            //image display
            ScaleImage(dec.images.get(index));
//            BufferedImage img = ImageIO.read(dec.images.get(index));
//            Image dimg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
//            imageLabel.setIcon(new ImageIcon(dimg));
        } catch (IOException ex) {
            Logger.getLogger(ImageMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ScaleImage(File f) throws IOException{
        BufferedImage img = ImageIO.read(f);
        Image dimg = null;
        int min = Math.min(img.getHeight(), img.getWidth());
        System.out.println(img.getHeight());
        System.out.println(img.getWidth());
        if(min == img.getHeight()){
            dimg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getWidth(), Image.SCALE_SMOOTH);
        }else{
            
            dimg = img.getScaledInstance(imageLabel.getHeight(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        }
        imageLabel.setIcon(new ImageIcon(dimg));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        seriesPanelContainer = new javax.swing.JPanel();
        seriesSpinner = new javax.swing.JSpinner();
        seriesScrollPane = new javax.swing.JScrollPane();
        seriesPanel = new javax.swing.JPanel();
        filesPanelContainer = new javax.swing.JPanel();
        filesSearchField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        filesPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        reportButton = new javax.swing.JButton();
        imgdeleteButton = new javax.swing.JButton();
        imgdlButton = new javax.swing.JButton();
        picturePanel = new javax.swing.JPanel();
        imgnameLabel = new javax.swing.JLabel();
        imagePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();

        seriesPanelContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        seriesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        seriesPanel.setLayout(new javax.swing.BoxLayout(seriesPanel, javax.swing.BoxLayout.Y_AXIS));
        seriesScrollPane.setViewportView(seriesPanel);

        javax.swing.GroupLayout seriesPanelContainerLayout = new javax.swing.GroupLayout(seriesPanelContainer);
        seriesPanelContainer.setLayout(seriesPanelContainerLayout);
        seriesPanelContainerLayout.setHorizontalGroup(
            seriesPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seriesSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
            .addGroup(seriesPanelContainerLayout.createSequentialGroup()
                .addComponent(seriesScrollPane)
                .addContainerGap())
        );
        seriesPanelContainerLayout.setVerticalGroup(
            seriesPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seriesPanelContainerLayout.createSequentialGroup()
                .addComponent(seriesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seriesScrollPane)
                .addContainerGap())
        );

        filesPanelContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        filesSearchField.setText("jTextField1");
        filesSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filesSearchFieldActionPerformed(evt);
            }
        });

        filesPanel.setLayout(new javax.swing.BoxLayout(filesPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(filesPanel);

        javax.swing.GroupLayout filesPanelContainerLayout = new javax.swing.GroupLayout(filesPanelContainer);
        filesPanelContainer.setLayout(filesPanelContainerLayout);
        filesPanelContainerLayout.setHorizontalGroup(
            filesPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(filesSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        filesPanelContainerLayout.setVerticalGroup(
            filesPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filesPanelContainerLayout.createSequentialGroup()
                .addComponent(filesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        reportButton.setText("Report");
        jToolBar1.add(reportButton);

        imgdeleteButton.setText("Delete");
        jToolBar1.add(imgdeleteButton);

        imgdlButton.setText("Download");
        jToolBar1.add(imgdlButton);

        picturePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        imgnameLabel.setText("jLabel1");

        imagePanel.setLayout(new java.awt.BorderLayout());
        imagePanel.add(imageLabel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout picturePanelLayout = new javax.swing.GroupLayout(picturePanel);
        picturePanel.setLayout(picturePanelLayout);
        picturePanelLayout.setHorizontalGroup(
            picturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imgnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
            .addGroup(picturePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        picturePanelLayout.setVerticalGroup(
            picturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(picturePanelLayout.createSequentialGroup()
                .addComponent(imgnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(seriesPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filesPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(picturePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seriesPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(filesPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addComponent(picturePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void filesSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filesSearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filesSearchFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel filesPanel;
    private javax.swing.JPanel filesPanelContainer;
    private javax.swing.JTextField filesSearchField;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton imgdeleteButton;
    private javax.swing.JButton imgdlButton;
    private javax.swing.JLabel imgnameLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel picturePanel;
    private javax.swing.JButton reportButton;
    private javax.swing.JPanel seriesPanel;
    private javax.swing.JPanel seriesPanelContainer;
    private javax.swing.JScrollPane seriesScrollPane;
    private javax.swing.JSpinner seriesSpinner;
    // End of variables declaration//GEN-END:variables

}
