package deduplicatore;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.*;
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
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    IplImage[] images;
    static final String[] EXTENSIONS = new String[]{
        "jpg", "png", "webp"
    };
    
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

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
    
    public void getEveryImages(String path){
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                getEveryImages( f.getAbsolutePath() );
                System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {// controllo estensione
                System.out.println( "File:" + f.getAbsoluteFile() );
                ////ACTION
            }
        }
        //return images;
    }
    
    public void searchForDirectory(){
        
    }
    
    public void compareImage(){
        
    }
    
    public static void main(String[] args) {
        
        Path directoryPath;
        
        try{
            directoryPath = Paths.get(args[0]);
            byte[] b = Files.readAllBytes(directoryPath);
            System.out.println(b);
        }catch(IOException e){
            throw new IllegalArgumentException("Errore durante la lettura del file");
        }
    }
    
}