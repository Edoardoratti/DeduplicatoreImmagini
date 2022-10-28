package deduplicatore;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
//import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
//import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;
//import org.opencv.imgproc.Imgproc;
//import org.bytedeco.opencv.opencv_core.*;
//import org.bytedeco.opencv.opencv_imgproc.*;
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
                    return (true);
                }
            }
            return (false);
        }
    };
    
    static List <File> directories = new ArrayList<>();
    
    static int[][] misuration;
    
    static int[][] series;
    
    static String[][] imageMatrix;
    
    static int size;

    public List<File> getImages(){
        List<File> fileslist = new ArrayList<>();
        
        for (File dir : directories ) {
            File[] temp = dir.listFiles(FILTER);
            fileslist.addAll(Arrays.asList(temp));
        }
        return fileslist;
    }
    
    public static void getListSize(List<File> images){
        size = images.size();
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
      
    public static void compareEqualImages(File file1, File file2) throws Exception {
        BufferedImage img1 = ImageIO.read(file1);
        BufferedImage img2 = ImageIO.read(file2);
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
           long diff = 0;
           for (int j = 0; j < h1; j++) {
              for (int i = 0; i < w1; i++) {
                 //Getting the RGB values of a pixel
                 int pixel1 = img1.getRGB(i, j);
                 Color color1 = new Color(pixel1, true);
                 int r1 = color1.getRed();
                 int g1 = color1.getGreen();
                 int b1 = color1.getBlue();
                 int pixel2 = img2.getRGB(i, j);
                 Color color2 = new Color(pixel2, true);
                 int r2 = color2.getRed();
                 int g2 = color2.getGreen();
                 int b2= color2.getBlue();
                 //sum of differences of RGB values of the two images
                 long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                 diff = diff+data;
              }
           }
           double avg = diff/(w1*h1*3);
           double percentage = (avg/255)*100;
           System.out.println("Difference: "+percentage);
      
   }
    
    public static boolean areEqualSeries(int a[], int b[]){
        List<Integer> c = Arrays.stream(a).boxed().toList();
        List<Integer> d = Arrays.stream(b).boxed().toList();
        
        for (int i : c){
            for(int j : d){
                if(i > 100){
                    
                }
                if(i == j){
                    d.remove(j);
                    break;
                }
            }
        }
        return d.isEmpty();
    }
    
    public static void analyseImage(List<File> images){
        getListSize(images);
        imageListToMatrix(images);
        misuration = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(images.get(i).equals(images.get(j))){
                    misuration[i][j] = i + 101; //valore non valevole nel contesto per segnalare la base
                }else{
                    int misure = compareImage(images.get(i).toString(),images.get(j).toString());
                    if(misure >= 20){
                        misuration[i][j] = misure;
                    }
                }
            }
        }
        swapBases();
    }
    
    public static void imageListToMatrix(List<File> images){
        imageMatrix = new String[size][size];
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < size; j++) {
                imageMatrix[i][j] = images.get(j).toString();
            }
        }
    }
    
    public static void swapBases(){
        int base = 0;
        for (int i = 1; i < size; i++) {
            String swap1 = imageMatrix[0][i];
            imageMatrix[0][i] = imageMatrix[base][i];
            imageMatrix[base][i] = swap1;
            int swap2 = misuration[0][i];
            misuration[0][i] = misuration[base][i];
            misuration[base][i] = swap2;
            base++;
        }
        for (int i = 1; i < size; i++) {
            
        }
        
        for (String s : imageMatrix[0]) {
            System.out.println(s);
        }
        
        for(int[] i : misuration){
            for(int j : i){
                System.out.printf("%03d ", j);
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) throws Exception {
        try{
            Deduplicatore d = new Deduplicatore("E:\\306\\Immagini\\Test");
            d.getDirectories(d.rootPath);
            List<File> images = d.getImages();
            for(File ls : images){
                System.out.println(ls);
            }
            analyseImage(images);            
            
            String p1 = "E:\\306\\Immagini\\Test\\beach.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\beachCopia.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\beachModified.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\beachModified1.jpg";
            String p2 = "E:\\306\\Immagini\\Test\\beachMirror.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\fddf.png";
            //String p2 = "E:\\306\\Immagini\\Test\\blackSpray.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\pastel.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\pastelFull.jpg";
            //String p2 = "E:\\306\\Immagini\\Test\\sand.jpg";
            compareImage(p1, p2);
            
        }catch(NullPointerException e){
            throw new IllegalArgumentException("Errore durante la lettura del file");         
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