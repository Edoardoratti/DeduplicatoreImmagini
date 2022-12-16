package deduplicatore;

import deduplicatore.gui.MainFrame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.SIFT;
import org.opencv.imgcodecs.Imgcodecs;


/*
 * @author Edoardo Ratti
 * @version
 *
 * Classe conentente i codici sorgente per controllare la similitudine
 * tra immagini.
 */
public class Deduplicatore extends MainFrame {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static final String[] EXTENSIONS = new String[]{
        "jpg", "png", "webp", "PNG"
    };

    static final FilenameFilter FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {

                if (name.endsWith("." + ext)) {
                    return true;
                }
            }
            return false;
        }
    };
    
    //tolleranza percentuale della scansione
    public static final int TOLLERANCE = 30; 
    //contrassegna un elemento di misuration come cancellato
    public static final int DELETED_ELEM = -2;
    //path della cartella principale
    public final String ROOTPATH;
    //lista predisposta a conenere le path assolute di tutte
    //le cartelle ricorsivamente
    public List<File> directories = new ArrayList<>();
    //misurazione calcolate dall'analisi
    public int[][] misuration;
    //lista predisposta a contenere tutte le immagini presenti in ROOTPATH
    public List<File> images;
    //si tratta del quantitativo di immagini
    public int size;
    //numero di immagini simili a un'immagine in base alla tolleranza
    public int[] similar;
    //percentuale di processo
    public float percentage = 0;

    public List<File> getImages() {
        List<File> fileslist = new ArrayList<>();
        for (File dir : directories) {
            File[] temp = dir.listFiles(FILTER);
            fileslist.addAll(Arrays.asList(temp));
        }
        return fileslist;
    }

    public void getListSize(List<File> images) {
        this.size = images.size();
    }

    public void getDirectories(String path) {
        File root = new File(path);
        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                directories.add(f);
                getDirectories(f.getAbsolutePath());
            }
        }
    }

    public void removeRedundantSeries() {
        countBetterSimilitude();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (misuration[i][j] >= TOLLERANCE && similar[i] >= similar[j]) {
                    deleteRow(j);
                }
            }
        }
    }

    public void deleteRow(int i) {
        for (int j = 0; j < size; j++) {
            misuration[i][j] = DELETED_ELEM;
        }
    }

    public void countBetterSimilitude() {
        similar = new int[size];
        for (int i = 0; i < size; i++) {
            int simCnt = 0;
            for (int j = 0; j < size; j++) {
                if (misuration[i][j] >= TOLLERANCE) {
                    simCnt += misuration[i][j];
                }
            }
            similar[i] = simCnt;
        }
    }
    
    public void removeRedundantFiles(){
        for (int i = 0; i < size; i++) {
            if(IntStream.of(misuration[i]).anyMatch(x -> x == -1)){
                for (int j = 0; j < size; j++) {
                    if(misuration[i][j] != -1){
                        if(misuration[i][j] < TOLLERANCE){
                            misuration[i][j] = DELETED_ELEM;
                        }else{
                            for (int k = 0; k < size; k++) {
                                if(misuration[i][j] > misuration[k][j] && i != k && misuration[k][j] != -1){
                                    misuration[k][j] = DELETED_ELEM;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static int compareImage(String p1, String p2) {
        SIFT detector = SIFT.create(0, 3);

        Mat img1 = Imgcodecs.imread(p1);
        Mat img2 = Imgcodecs.imread(p2);
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        detector.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);

        double max = Math.max(descriptors2.rows(), descriptors1.rows());
        double min = Math.min(descriptors2.rows(), descriptors1.rows());
        System.out.println("Testing  " + min / max * 100);
        return (int) ((min / max) * 100);
    }

    public void analyseImage() {
        getListSize(images);
        int numberOfOperation = (size * (size - 1)) / 2;
        misuration = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (images.get(i).equals(images.get(j))) {
                    misuration[i][j] = -1; //valore non valevole nel contesto per segnalare la base
                } else if (misuration[j][i] == 0) {
                    long timer = System.currentTimeMillis();
                    misuration[i][j] = compareImage(images.get(i).toString(), images.get(j).toString());
                    misuration[j][i] = misuration[i][j];
                    timer = System.currentTimeMillis() - timer;
                    float increment = (float) ((float) timer / (float) ((float) timer * (float) numberOfOperation)) * (float) 100;
                    //System.out.println("timer" + timer + "totTimer, " + (timer * numberOfOperation) + "Increment, " + increment);
                    percentage = percentage + increment;
                    //System.out.println("Percentuale " + percentage);
                    raisePercChange();
                    
                }
            }
        }
        removeRedundantSeries();
        removeRedundantFiles();
        for (int[] i : misuration) {
            for (int j : i) {
                System.out.printf("%03d ", j);
            }
            System.out.println("");
        }
    }

    private void raisePercChange() {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "c", 0, percentage);
        PropertyChangeListener listener = this.getPropertyChangeListeners()[0];
        listener.propertyChange(event);
    }

    public void outReport() {
        String report = "";

        for (int i = 0; i < misuration.length; i++) {
            if (IntStream.of(misuration[i]).anyMatch(x -> x == -1)) {
                report += images.get(i).getName() + System.lineSeparator() + System.lineSeparator();       
                for (int j = 0; j < misuration.length; j++) {
                    int misure = misuration[i][j];
                    if (misure >= Deduplicatore.TOLLERANCE) {
                        report += "\t-" + images.get(j).getName() + System.lineSeparator();
                    }
                }
                report += System.lineSeparator();
            }
        }
        Path outFile = Paths.get("log.txt");
        byte[] b = report.getBytes();
        try {
            Files.write(outFile, b);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error out report file");
        }
    }

    public void startProgram(Deduplicatore d) {
        try {
            d.getDirectories(d.ROOTPATH);
            images = d.getImages();
            analyseImage();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Path non valevole");
        }
    }

    public Deduplicatore(String root) {
        ROOTPATH = root;
        directories.add(new File(root));
    }
}
