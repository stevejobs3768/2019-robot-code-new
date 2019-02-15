package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import java.util.*;
public class VisionController implements RobotController{

    private UsbCamera camera1;
    //private UsbCamera camera2;
    private VideoSink server;
    private int curCam;
   // private CvSink cvSink;
   // private CvSource outputStream; 
   // private GripPipeline pipeline;
    public VisionController(RobotProperties properties) {
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        camera1.setResolution(640, 480);
        //camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        //camera2.setResolution(640, 480);
    }

    @Override
    public String getName() {
        return "VisionController";
    }

    //TODO: Wilson you can't define one class inside another
    /*public static class Line {
        public final double x1, y1, x2, y2;

        public Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public double lengthSquared() {
            return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
        }

        public double length() {
            return Math.sqrt(lengthSquared());
        }

        public double angle() {
            return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        }
    }
    private ArrayList<Line> lines;*/
                
    @Override
    public boolean performAction(RobotProperties properties) {

        return true;
    }
}
