//import java.io.File
//
//import opencv_cookbook.OpenCVUtils._
//import org.bytedeco.javacpp.DoublePointer
//import org.bytedeco.opencv.global.opencv_core._
//import org.bytedeco.opencv.global.opencv_imgcodecs._
//import org.bytedeco.opencv.global.opencv_imgproc._
//import org.bytedeco.opencv.opencv_core._
//
//object Ex2TemplateMatching {
//
//  val image1 = loadAndShowOrExit(new File("data/church01.jpg"), IMREAD_GRAYSCALE)
//  val image2 = loadAndShowOrExit(new File("data/church02.jpg"), IMREAD_GRAYSCALE)
//
//  // define a template
//  val target = new Mat(image1, new Rect(120, 40, 30, 30))
//  show(target, "Template")
//
//
//  // define search region
//  val roi = new Mat(image2,
//    // here top half of the image
//    new Rect(0, 0, image2.cols, image2.rows / 2))
//
//  // perform template matching
//  val result = new Mat()
//  matchTemplate(
//    roi, // search region
//    target, // template
//    result, // result
//    CV_TM_SQDIFF)
//  // similarity measure
//
//  // find most similar location
//  val minVal = new DoublePointer(1)
//  val maxVal = new DoublePointer(1)
//  val minPt  = new Point()
//  val maxPt  = new Point()
//  minMaxLoc(result, minVal, maxVal, minPt, maxPt, null)
//
//  println(s"minPt = ${minPt.x}, ${minPt.y}")
//
//
//  // draw rectangle at most similar location
//  // at minPt in this case
//  rectangle(roi, new Rect(minPt.x, minPt.y, target.cols, target.rows), new Scalar(255, 255, 255, 0))
//
//  show(roi, "Best match")
//}




//public static int compareImage2(String p1, String p2){
//        float threshold = 400f;
//        int nOctaves = 4;
//        int retVal = 0;
//        int totalVal = 0;
//        long startTime = System.currentTimeMillis();
//        
//        //KAZE detector = KAZE.create(false, false, threshold, nOctaves);
//        //AKAZE detect = AKAZE.create(AKAZE.DESCRIPTOR_KAZE, 0, nOctaves, threshold);
//        SIFT detector = SIFT.create(0, 3);
//        
//        Mat img1 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\beach.jpg", Imgcodecs.IMREAD_GRAYSCALE);
//        //Mat img2 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\beachModified.jpg", Imgcodecs.IMREAD_GRAYSCALE);
//        //Mat img2 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\beachModified1.jpg", Imgcodecs.IMREAD_GRAYSCALE);
//        //Mat img2 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\beachMirror.jpg", Imgcodecs.IMREAD_GRAYSCALE);
//        //Mat img2 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\fddf.png", Imgcodecs.IMREAD_GRAYSCALE);
//        //Mat img2 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\d.jpg", Imgcodecs.IMREAD_GRAYSCALE);
//        Mat img2 = Imgcodecs.imread("E:\\306\\Immagini\\Test\\f.jpg", Imgcodecs.IMREAD_GRAYSCALE);
//        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
//        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
//        Mat descriptors1 = new Mat();
//        Mat descriptors2 = new Mat();  
//        
//        detector.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
//        detector.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);
//        
//        
//        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);//BRUTEFORCE_HAMMING
//        MatOfDMatch matches = new MatOfDMatch();
//        matcher.match(descriptors1, descriptors2 ,matches);
//        DMatch[] match = matches.toArray();
//        
//        if (descriptors2.cols() == descriptors1.cols()) {
//            
//            // Check matches of key points
//            double max_dist = 0;
//            double min_dist = Integer.MAX_VALUE;
//            System.out.println("Descriptor.rows: " + descriptors1.rows());
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
//                if (match[i].distance <= 300) {
//                    retVal++;
//            }
//        }
//        System.out.println("matching count=" + ((double)retVal / (double)totalVal) * 100);
//        System.out.println((double)match.length / (double)descriptors1.total() *100);
//        }
//
//        System.out.println("Mat desc1: " + descriptors1.size());
//
//        long estimatedTime = System.currentTimeMillis() - startTime;
//        System.out.println("estimatedTime=" + estimatedTime + "ms");
//        return retVal;
//    }
//
//import org.opencv.core.DMatch;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfDMatch;
//import org.opencv.core.MatOfKeyPoint;
//import org.opencv.features2d.DescriptorMatcher;
//import org.opencv.features2d.SIFT;
//import org.opencv.imgcodecs.Imgcodecs;

