package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.vision.VisionThread;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class VisionController implements RobotController {

    // finds and filters lines in an image
    private GripPipeline pipeline;

    // converts an image to black and white
    private bwGripPipeline bwpipeline;

    // line tracking algorithm
    private LineTrackingAlgo linetracker;

    // stores the results of pipeline
    private ArrayList<GripPipeline.Line> filteredLines;

    // camera1 is for sandstorm, camera2 is for alignment
    private UsbCamera camera1;
    private UsbCamera camera2;

    // sink determines which camer is used, source displays the video
    private CvSink cvSink;
    // private CvSink cvSinkBack;
    private CvSource cvSource;

    // vision code is carried out in the thread
    private Thread visionThread;

    // determines if line processing should be run
    private boolean isLongPress = false;
    private boolean lineIsOn = false;

    // private Boolean prevButton = false;
    // private Boolean bwIsRunning = true;

    // dimensions of the video
    private final int width = 160;
    private final int height = 120;
    private final int fps = 120;

    // determines when the robot should begin aligning
    boolean selfAlign = false;

    public VisionController(RobotProperties properties) {
        // moved to robotproperties
        /*
         * //adds button to smartdashboard SmartDashboard.putBoolean("selfAlign",
         * false); SmartDashboard.putBoolean("ReverseTurn", false);
         */
        // initializes pipelines
        /*
         * pipeline = new GripPipeline(); bwpipeline = new bwGripPipeline();
         * LineTrackingAlgo linetracker = new LineTrackingAlgo(properties);
         */
        // moved inside the thread //initializes both cameras

        /*
         * camera1 = CameraServer.getInstance().startAutomaticCapture(0);
         * camera1.setResolution(width, height); camera1.setFPS(fps);
         * 
         * camera2 = CameraServer.getInstance().startAutomaticCapture(1);
         * camera2.setResolution(width, height); camera2.setFPS(fps);
         * 
         * // initializes the source and sink cvSink =
         * 
         * CameraServer.getInstance().getVideo(camera1);// camera1 cvSource =
         * CameraServer.getInstance().putVideo("vision", width, height);
         */
        /*
         * cvSink = CameraServer.getInstance().getVideo(camera2);// camera2 cvSource =
         * CameraServer.getInstance().putVideo("visionDown", width, height);
         */

        visionThread = new Thread(() -> {
            // cvSinkBack = CameraServer.getInstance().getVideo(camera2);// camera2
            // cvSource = CameraServer.getInstance().putVideo("visionDown", width, height);
            pipeline = new GripPipeline();
            bwpipeline = new bwGripPipeline();
            LineTrackingAlgo linetracker = new LineTrackingAlgo(properties);
            // initializes both cameras

            camera1 = CameraServer.getInstance().startAutomaticCapture(0);
            camera1.setResolution(width, height);
            camera1.setFPS(fps);

            camera2 = CameraServer.getInstance().startAutomaticCapture(1);
            camera2.setResolution(width, height);
            camera2.setFPS(fps);

            cvSink = CameraServer.getInstance().getVideo(camera2);// camera1
            cvSource = CameraServer.getInstance().putVideo("vision", width, height);

            Mat source = new Mat();
            Mat output = new Mat();
            // Mat sourceBack = new Mat();
            // Mat outputBack = new Mat();

            try {
                while (!Thread.interrupted()) {
                    if (properties.joystick.getButtonTwo()) {
                        if (!isLongPress) {
                            lineIsOn = !lineIsOn;
                            isLongPress = true;
                        }
                        // System.out.println("lineIsOn" + lineIsOn);
                    } else {
                        isLongPress = false;
                    }
                    // grabs current frame from cvSink
                    if (cvSink.grabFrame(source) == 0) {
                        cvSource.notifyError(cvSink.getError());
                        continue;
                    }
                    output = source;
                    if (lineIsOn) {
                        // bwpipeline.process(source);
                        // output = bwpipeline.desaturateOutput();
                        // cvSource.putFrame(output);
                        /*
                         * if (cvSinkBack.grabFrame(sourceBack) == 0) {
                         * cvSource.notifyError(cvSinkFront.getError()); continue; }
                         */
                        // button on dashboard triggers the LineTrackingAlgo
                        selfAlign = SmartDashboard.getBoolean("selfAlign", false);

            //             /*
            //              * if (bwIsRunning) { // displays b+w video, this is the default setting
            //              * bwpipeline.process(source); output = bwpipeline.desaturateOutput();
            //              * cvSource.putFrame(output); } else {
            //              */

            //             pipeline.process(source);
            //             output = pipeline.cvResizeOutput();
            //             ArrayList<GripPipeline.Line> lines = pipeline.filterLinesOutput();
            //             if (!lines.isEmpty()) {
            //                 for (int i = 0; i < lines.size(); i++) { //
            //                     // System.out.println(i + " " + lines.get(i).angle() + " " +
            //                     // lines.get(i).length());
            //                     Imgproc.line(output, new Point(lines.get(i).x1 * 0.95, lines.get(i).y1 * 0.95),
            //                             new Point(lines.get(i).x2 * 0.95, lines.get(i).y2 * 0.95),
            //                             new Scalar(0, 100, 0));
            //                 } //
            //                   // System.out.println(linetracker.weightedAngle(lines));
            //             }
            //             output = linetracker.process(output, lines, width, height, selfAlign);

                        // bwpipeline.process(source);
                        // output = bwpipeline.desaturateOutput();
                    }
                    cvSource.putFrame(output);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("VisionProblem");
            }

        });

        // automatically starts thread
        visionThread.setDaemon(true);
        visionThread.start();
    }

    // for troubleshooting
    @Override
    public String getName() {
        return "VisionController";
    }

    @Override
    public boolean performAction(RobotProperties properties) {

        // restarts thread if it unexpectedly crashes. The program has been
        // tested to eliminated errors with memory so this is likely never run

        /*
         * if (!visionThread.isAlive()) { visionThread.start(); }
         */

        // switches view to camera2 and begins line tracing whenever button 2 is pressed
        // automatically reverts to camera1

        /*
         * if (properties.joystick.getButtonTwo() && !prevButton) { // prevButton =
         * !prevButton; bwIsRunning = false; cvSink =
         * CameraServer.getInstance().getVideo(camera2);
         * 
         * } else if (properties.joystick.getButtonTwo() && prevButton) { // prevButton
         * = !prevButton; bwIsRunning = true; cvSink =
         * CameraServer.getInstance().getVideo(camera1); } prevButton =
         * properties.joystick.getButtonTwo();
         */

        return true;
    }
}