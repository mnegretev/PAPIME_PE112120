package mx.mnegretev.stargazer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CvTools {
    public static void drawFancyText(Mat img, String text, Point textPose, int textMargin, int triangleHeight)
    {
        int[] baseLine = new int[1];
        Size textSize = Imgproc.getTextSize(text, Core.FONT_HERSHEY_SIMPLEX, 1.5, 2, baseLine);
        Point textOrigin = new Point(textPose.x - textSize.width/3, textPose.y + triangleHeight + textSize.height + textMargin);
        int textBaseLine = Math.max(baseLine[0], textMargin);
        Point rectStart = new Point(textOrigin.x - textMargin, textOrigin.y + textBaseLine);
        Point rectEnd = new Point(textOrigin.x + textSize.width + textMargin, textOrigin.y - textSize.height - textMargin);
        Point triangle1 = new Point(textPose.x - triangleHeight, textPose.y + triangleHeight);
        Point triangle2 = new Point(textPose.x, textPose.y);
        Point triangle3 = new Point(textPose.x + triangleHeight, textPose.y + triangleHeight);
        MatOfPoint triangle = new MatOfPoint(triangle1, triangle2, triangle3);

        Imgproc.rectangle(img, rectStart, rectEnd, new Scalar(255,255,255), -1);
        Imgproc.fillConvexPoly(img, triangle, new Scalar(255,255,255));
        Imgproc.putText(img, text, textOrigin, Core.FONT_HERSHEY_SIMPLEX, 1.5, new Scalar(0,0,0), 2);
    }

    public static void drawUpperText(Mat img, String text)
    {
        Imgproc.rectangle(img, new Point(0,0), new Point(img.cols(), 70), new Scalar(0,0,0), -1);
        Imgproc.putText(img, text, new Point(10, 60), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),1 );
    }

    public static void drawLowerText(Mat img, String text)
    {
        Imgproc.rectangle(img, new Point(0, img.rows() - 120), new Point(img.rows(), img.cols()), new Scalar(0,0,0),-1);
        Imgproc.putText(img, text, new Point(5,img.rows()-60), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,255),2);
    }
}
