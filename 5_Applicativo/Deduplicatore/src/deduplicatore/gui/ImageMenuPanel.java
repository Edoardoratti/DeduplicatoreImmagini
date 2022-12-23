package deduplicatore.gui;

import deduplicatore.Deduplicatore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import static javax.swing.SwingUtilities.updateComponentTreeUI;

/**
 * Pannello che mostra le analisi sulle immagini
 *
 * @author edoardo.ratti
 */
public class ImageMenuPanel extends JPanel {

    private Deduplicatore dec;
    private Component[] imageButtons;//Contiente i pulsanti relativi alle immagini specifici di una serie
    private Component[] serieButtons;//Contiene i pulsanti relativi alle serie
    private int buttonSelectedIndex;//Indice del pulsante premuto corrente
    private Component buttonSelected;//Pulsante corrente
    private String root = "";
    

    public ImageMenuPanel() {
        initComponents();
    }

    public void setDeduplicatore(Deduplicatore d) {
        dec = d;
    }
    
    public void displaySeries() {
        updateComponentTreeUI(this.getParent());
        seriesPanel.removeAll();
        filesPanel.removeAll();

        for (int i = 0; i < dec.misuration.length; i++) {
            if (IntStream.of(dec.misuration[i]).anyMatch(x -> x == -1)) {
                JButton button = new JButton(dec.images.get(i).getName());
                seriesPanel.add(button);

                //Creazione evento
                button.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        seriesButtonPressedEvent(evt);
                    }
                });
            }
        }
        SpinnerNumberModel model = new SpinnerNumberModel(0,0,seriesPanel.getComponentCount(),1);
        seriesSpinner.setModel(model);
        serieButtons = seriesPanel.getComponents();
    }
    

    private void seriesButtonPressedEvent(java.awt.event.ActionEvent evt) {
        updateComponentTreeUI(this.getParent());
        int index = getFileIndex(((JButton) evt.getSource()).getText());
        buttonSelectedIndex = index;
        switchJToolBarButtons(true);
        imgdeleteButton.setEnabled(false);
        filesPanel.removeAll();
        for (int i = 0; i < dec.misuration.length; i++) {
            int misure = dec.misuration[index][i];
            if (misure >= Deduplicatore.TOLLERANCE) {
                JButton button = new JButton(dec.images.get(i).getName());
                filesPanel.add(button);
                button.addActionListener(new java.awt.event.ActionListener() { //Creazione evento
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        showImage(evt);
                        buttonSelectedIndex = getFileIndex(((JButton) evt.getSource()).getText());
                        buttonSelected = button;
                        imgdeleteButton.setEnabled(true);
                    }
                });
            }
        }
        imageButtons = filesPanel.getComponents();//components array
        try {
            //image display
            scaleImage(dec.images.get(index));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Image not displayable");
        }
    }
    
    private void filterImages(){
        String text = filesSearchField.getText();
        for (Component button : imageButtons) {
            if (((JButton) button).getText().contains(text)) {
                button.setVisible(true);
            } else {
                button.setVisible(false);
            }
        }
    }
    
    private void filterSeries(){
        Object number = seriesSpinner.getValue();
        if((Integer)number == 0){
            for(Component button : serieButtons){
                button.setVisible(true);
            }
        }else{
            for (Component button : serieButtons) {
                button.setVisible(false);
            }
            serieButtons[(Integer)number - 1].setVisible(true);
        }
    }
    
    public void switchJToolBarButtons(boolean b) {
        reportButton.setEnabled(b);
        imgdeleteButton.setEnabled(b);
        imgdlButton.setEnabled(b);
    }

    private void showImage(java.awt.event.ActionEvent evt){
        try {
            scaleImage(dec.images.get(getFileIndex(((JButton) evt.getSource()).getText())));
        } catch (IOException ex) {
           throw new IllegalArgumentException("Immagine invalida");
        }
    }
    
    private int getFileIndex(String name){
            File file = new File(name);
            
            for (File f : dec.images) {
                if (f.getName().equals(file.getName())) {
                    return dec.images.lastIndexOf(f);
                }
            }
        
            return -1; //impossible case
    }
    

    private void scaleImage(File f) throws IOException {
        BufferedImage img = ImageIO.read(f);
        int w;
        int h;
        int max = Math.max(img.getHeight(), img.getWidth());
        if(max == img.getWidth()){
            w = imageLabel.getWidth();
            h = (int)((((float)img.getHeight()) / (float)img.getWidth()) * w);
        }else{
            h = imageLabel.getHeight();
            w = (int)((((float)img.getWidth()) / (float)img.getHeight()) * h);
        }
        Image dimg = img.getScaledInstance(Math.round(w), Math.round(h), Image.SCALE_DEFAULT);
        imageLabel.setIcon(new ImageIcon(dimg));
    }

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

        seriesSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        seriesSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                seriesSpinnerStateChanged(evt);
            }
        });

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

        filesSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filesSearchFieldKeyReleased(evt);
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
        reportButton.setEnabled(false);
        reportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(reportButton);

        imgdeleteButton.setText("Delete");
        imgdeleteButton.setEnabled(false);
        imgdeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgdeleteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(imgdeleteButton);

        imgdlButton.setEnabled(false);
        imgdlButton.setLabel("Open");
        imgdlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgdlButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(imgdlButton);

        picturePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        imagePanel.setPreferredSize(new java.awt.Dimension(486, 486));
        imagePanel.setLayout(new java.awt.BorderLayout());

        imageLabel.setPreferredSize(new java.awt.Dimension(486, 486));
        imagePanel.add(imageLabel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout picturePanelLayout = new javax.swing.GroupLayout(picturePanel);
        picturePanel.setLayout(picturePanelLayout);
        picturePanelLayout.setHorizontalGroup(
            picturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imgnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
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
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
            .addComponent(picturePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void raiseDownloadImage(Path p) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "c", 0, p);
        PropertyChangeListener listener = this.getPropertyChangeListeners()[0];
        listener.propertyChange(event);
    }

    private void seriesSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_seriesSpinnerStateChanged
        filterSeries();
    }//GEN-LAST:event_seriesSpinnerStateChanged

    private void filesSearchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filesSearchFieldKeyReleased
        filterImages();
    }//GEN-LAST:event_filesSearchFieldKeyReleased

    private void reportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportButtonActionPerformed
        dec.outReport();
    }//GEN-LAST:event_reportButtonActionPerformed

    private void imgdeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgdeleteButtonActionPerformed
        Path p = Paths.get(dec.images.get(buttonSelectedIndex).getAbsolutePath());
        try {
            Files.deleteIfExists(p);
            filesPanel.remove(buttonSelected);
            updateComponentTreeUI(this.getParent());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unexitstent path");
        }
    }//GEN-LAST:event_imgdeleteButtonActionPerformed

    private void imgdlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgdlButtonActionPerformed
        Path p = Paths.get(dec.images.get(buttonSelectedIndex).getAbsolutePath());
        try {
            Desktop.getDesktop().open(new File(p.toString()));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unexitstent path");
        }
    }//GEN-LAST:event_imgdlButtonActionPerformed


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
