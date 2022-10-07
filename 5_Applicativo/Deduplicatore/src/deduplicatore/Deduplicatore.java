package deduplicatore;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.*;


/*
 * @author Edoardo Ratti
 * @version
 *
 * Classe conentente i codici sorgente per controllare la similitudine
 * tra immagini.
 */

public class Deduplicatore {
    
    //static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); };
    
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
    
    static short[][] misuration;
    
    static int size;

    public List<File> getEveryImages(){
        List<File> fileslist = new ArrayList<>();
        
        for (File dir : directories ) {
            File[] temp = dir.listFiles(FILTER);
            fileslist.addAll(Arrays.asList(temp));
        }
        return fileslist;
    }
    
    public static void getSize(List<File> images){
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
    
    public static void compareImage(List<File> images){
        getSize(images);
        System.out.println(size);
        misuration = new short[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(images.get(i).equals(images.get(j))){
                    misuration[i][j] = 100;
                }
            }
        }
        for(short[] i : misuration){
            for(short j : i){
                System.out.printf("%03d ", j);
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        try{
            Deduplicatore d = new Deduplicatore("E:\\306\\Immagini");
            d.getDirectories(d.rootPath);
            List<File> images = d.getEveryImages();
            for(File ls : images){
                System.out.println(ls);
            }
            compareImage(images);
        }catch(NullPointerException e){
            throw new IllegalArgumentException("Errore durante la lettura del file");         
        }
    }
    
    public Deduplicatore(String root){
        rootPath = root;
        directories.add(new File(root));
    }
    
}