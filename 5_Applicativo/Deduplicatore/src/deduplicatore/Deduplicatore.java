package deduplicatore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;
import org.opencv.imgcodecs.Imgcodecs;


/*
 * @author Edoardo Ratti
 * @version
 *
 * Classe conentente i codici sorgente per controllare la similitudine
 * tra immagini.
 */

public class Deduplicatore {
    
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    
    static final String[] EXTENSIONS = new String[]{
        "jpg", "png", "webp","PNG"
    };
    final String rootPath;
    
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
    public static final int TOLLERANCE = 30;
    
    public List <File> directories = new ArrayList<>();
    
    public int[][] misuration;
    
    public List<File> images;
    
    public int size;
    
    public int[] similar;

    public List<File> getImages(){
        List<File> fileslist = new ArrayList<>();
        for (File dir : directories ) {
            File[] temp = dir.listFiles(FILTER);
            fileslist.addAll(Arrays.asList(temp));
        }
        return fileslist;
    }
    
    public void getListSize(List<File> images){
        this.size = images.size();
    }
    
    public void getDirectories(String path){
        File root = new File(path);
        File[] list = root.listFiles();

        for (File f : list){
            if (f.isDirectory()){
                directories.add(f);
                getDirectories(f.getAbsolutePath());
            }
        }
    }
    
    public void removeRedundantSeries(){
        countBetterSimilitude();
        for (int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(misuration[i][j] >= TOLLERANCE && similar[i] >= similar[j]){
                    deleteRow(j);
                }
            }
        }
    }
    
    public void deleteRow(int i){
        for (int j = 0; j < size; j++){
            misuration[i][j] = -2;
        }
    }
    
    public void countBetterSimilitude(){
        similar = new int[size];
        for (int i = 0; i < size; i++){
            int simCnt = 0;
            for(int j = 0; j < size; j++){
                if(misuration[i][j] >= TOLLERANCE){
                    simCnt++;
                }
            }
            similar[i] = simCnt;
        }
    }
    
    public void analyseImage(){
        getListSize(images);
        misuration = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(images.get(i).equals(images.get(j))){
                    misuration[i][j] = -1; //valore non valevole nel contesto per segnalare la base
                }else{
                    if(misuration[j][i] == 0){
                        misuration[i][j] = compareImage(images.get(i).toString(),images.get(j).toString());
                        misuration[j][i] = misuration[i][j];
                    }
                }
            }
        }
        removeRedundantSeries();
        for(int[] i : misuration){
            for(int j : i){
                System.out.printf("%03d ", j);
            }
            System.out.println("");
        }
    }
    
    public void startProgram(Deduplicatore d){
        try{
            d.getDirectories(d.rootPath);
            images = d.getImages();
            analyseImage();
        }catch(NullPointerException e){
            throw new IllegalArgumentException("Path non valevole");  
        }
    }
    
    public static void main(String[] args) throws Exception {
        try{
            Deduplicatore d = new Deduplicatore("E:\\306\\Immagini\\Test\\test");
            d.getDirectories(d.rootPath);
            d.images = d.getImages();
//            for(File ls : images){
//                System.out.println(ls); // stampa delle path
//            }
            d.analyseImage();
            
            String p1 = "E:\\306\\Immagini\\Test\\beach.jpg";
            String p2 = "E:\\306\\Immagini\\Test\\beachCopia.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\beachModified.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\beachModified1.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\beachMirror.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\fddf.png";
            //String p2 = "E:\\306\\Immagini\\Test\\blackSpray.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\pastel.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\pastelFull.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\sand.jpg";
            //compareImage(p1, p2);
            
        }catch(NullPointerException e){
            throw new IllegalArgumentException("Errore durante l'analisi");  
        }
    }
    
    public Deduplicatore(String root){
        rootPath = root;
        directories.add(new File(root));
    }
    
    public static int compareImage(String p1, String p2){
        int retVal = 0;
        int totalVal = 0;
        long startTime = System.currentTimeMillis();
       
        SIFT detector = SIFT.create(0, 3);
        
        Mat img1 = Imgcodecs.imread(p1);
        Mat img2 = Imgcodecs.imread(p2);
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();  
        
        detector.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);
        
        
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);//BRUTEFORCE_HAMMING
//        MatOfDMatch matches = new MatOfDMatch();
//        matcher.match(descriptors1, descriptors2 ,matches);
//        DMatch[] match = matches.toArray();
        
//        if (descriptors2.cols() == descriptors1.cols()) {
            
            // Check matches of key points
//            double max_dist = 0;
//            double min_dist = Integer.MAX_VALUE;
//            System.out.println("Descriptor.rows: " + descriptors1.rows());
//            System.out.println("Match.length: " + match.length);
//            for (int i = 0; i < descriptors1.rows(); i++) { 
//             double dist = match[i].distance;
//                if( dist < min_dist ) min_dist = dist;
//                if( dist > max_dist ) max_dist = dist;
//            }
//            System.out.println("max_dist=" + max_dist + ", min_dist=" + min_dist);
//
//            // Extract good images (distances are under 10)
//            for (int i = 0; i < descriptors1.rows(); i++) {
//                totalVal++;
//                if (match[i].distance <= (min_dist + max_dist) / 2) {// 300
//                    retVal++;
//                }
//            }
        double max = Math.max(descriptors2.rows(),descriptors1.rows() );
        double min = Math.min(descriptors2.rows(),descriptors1.rows() );
        //System.out.println("matching count=" + ((double)retVal / (double)totalVal) * 100);
        System.out.println("Testing  " + min / max * 100);
        //}

        //System.out.println("Mat desc1: " + descriptors1.size());

        //long estimatedTime = System.currentTimeMillis() - startTime;
        //System.out.println("estimatedTime=" + estimatedTime + "ms");
        //return match.length;
        return (int)((min / max) * 100);
    }
//    public int SendImageCompare(String fileSource, String fileDestination) throws IOException{
//
//        URL url = new URL(fileSource);
//        InputStream in = new BufferedInputStream(url.openStream());
//        OutputStream out = new BufferedOutputStream(new FileOutputStream("./Image/image.jpg"));
//
//
//        for ( int i; (i = in.read()) != -1; ) {
//            out.write(i);
//        }
//        in.close();
//        out.close();
//
//
//        int retVal = 0;
//        long startTime = System.currentTimeMillis();
//
//        String filename1 = "./Image/image.jpg";
//        String filename2 = fileDestination;
//
//
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//        // Load images to compare
//        Mat img1;
//        img1 = Imgcodecs.imread(filename1, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//        Mat img2 = Imgcodecs.imread(filename2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//
//
//        // Declare key point of images
//        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
//        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
//        Mat descriptors1 = new Mat();
//        Mat descriptors2 = new Mat();
//
//
//        // Definition of ORB key point detector and descriptor extractors
//        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB); 
//        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
//
//
//        // Detect key points
//        detector.detect(img1, keypoints1);
//        detector.detect(img2, keypoints2);
//
//        // Extract descriptors
//        extractor.compute(img1, keypoints1, descriptors1);
//        extractor.compute(img2, keypoints2, descriptors2);
//
//
//        // Definition of descriptor matcher
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
//
//
//        // Match points of two images
//        MatOfDMatch matches = new MatOfDMatch();
//      //  System.out.println("Type of Image1= " + descriptors1.type() + ", Type of Image2= " + descriptors2.type());
//      //  System.out.println("Cols of Image1= " + descriptors1.cols() + ", Cols of Image2= " + descriptors2.cols());
//
//        // Avoid to assertion failed
//        // Assertion failed (type == src2.type() && src1.cols == src2.cols && (type == CV_32F || type == CV_8U)
//        if (descriptors2.cols() == descriptors1.cols()) {
//         matcher.match(descriptors1, descriptors2 ,matches);
//
//         // Check matches of key points
//         DMatch[] match = matches.toArray();
//         double max_dist = 0; double min_dist = 100;
//
//         for (int i = 0; i < descriptors1.rows(); i++) { 
//          double dist = match[i].distance;
//             if( dist < min_dist ) min_dist = dist;
//             if( dist > max_dist ) max_dist = dist;
//         }
//         System.out.println("max_dist=" + max_dist + ", min_dist=" + min_dist);
//
//            // Extract good images (distances are under 10)
//         for (int i = 0; i < descriptors1.rows(); i++) {
//          if (match[i].distance <= 10) {
//           retVal++;
//          }
//         }
//         System.out.println("matching count=" + retVal);
//        }
//
//        long estimatedTime = System.currentTimeMillis() - startTime;
//        System.out.println("estimatedTime=" + estimatedTime + "ms");
//
//        return retVal;
//    }
}