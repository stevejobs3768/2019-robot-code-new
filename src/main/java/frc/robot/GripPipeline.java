package frc.robot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

/**
* GripPipeline class.
*
* <p>An OpenCV pipeline generated by GRIP.
*
* @author GRIP
*/
public class GripPipeline {

	//Outputs
	private Mat cvResizeOutput = new Mat();
	private Mat desaturateOutput = new Mat();
	private Mat cvThresholdOutput = new Mat();
	private Mat cvErodeOutput = new Mat();
	private ArrayList<Line> findLinesOutput = new ArrayList<Line>();
	private ArrayList<Line> filterLinesOutput = new ArrayList<Line>();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process(Mat source0) {
		// Step CV_resize0:
		Mat cvResizeSrc = source0;
		Size cvResizeDsize = new Size(0, 0);
		double cvResizeFx = 0.25;
		double cvResizeFy = 0.25;
		int cvResizeInterpolation = Imgproc.INTER_LINEAR;
		cvResize(cvResizeSrc, cvResizeDsize, cvResizeFx, cvResizeFy, cvResizeInterpolation, cvResizeOutput);

		// Step Desaturate0:
		Mat desaturateInput = cvResizeOutput;
		desaturate(desaturateInput, desaturateOutput);

		// Step CV_Threshold0:
		Mat cvThresholdSrc = desaturateOutput;
		double cvThresholdThresh = 230.0;
		double cvThresholdMaxval = 255.0;
		int cvThresholdType = Imgproc.THRESH_BINARY;
		cvThreshold(cvThresholdSrc, cvThresholdThresh, cvThresholdMaxval, cvThresholdType, cvThresholdOutput);

		// Step CV_erode0:
		Mat cvErodeSrc = cvThresholdOutput;
		Mat cvErodeKernel = new Mat();
		Point cvErodeAnchor = new Point(-1, -1);
		double cvErodeIterations = 1;
		int cvErodeBordertype = Core.BORDER_CONSTANT;
		Scalar cvErodeBordervalue = new Scalar(-1);
		cvErode(cvErodeSrc, cvErodeKernel, cvErodeAnchor, cvErodeIterations, cvErodeBordertype, cvErodeBordervalue, cvErodeOutput);

		// Step Find_Lines0:
		Mat findLinesInput = cvErodeOutput;
		findLines(findLinesInput, findLinesOutput);

		// Step Filter_Lines0:
		ArrayList<Line> filterLinesLines = findLinesOutput;
		double filterLinesMinLength = 20.0;
		double[] filterLinesAngle = {0, 360.0};
		filterLines(filterLinesLines, filterLinesMinLength, filterLinesAngle, filterLinesOutput);

	}

	/**
	 * This method is a generated getter for the output of a CV_resize.
	 * @return Mat output from CV_resize.
	 */
	public Mat cvResizeOutput() {
		return cvResizeOutput;
	}

	/**
	 * This method is a generated getter for the output of a Desaturate.
	 * @return Mat output from Desaturate.
	 */
	public Mat desaturateOutput() {
		return desaturateOutput;
	}

	/**
	 * This method is a generated getter for the output of a CV_Threshold.
	 * @return Mat output from CV_Threshold.
	 */
	public Mat cvThresholdOutput() {
		return cvThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a CV_erode.
	 * @return Mat output from CV_erode.
	 */
	public Mat cvErodeOutput() {
		return cvErodeOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Lines.
	 * @return ArrayList<Line> output from Find_Lines.
	 */
	public ArrayList<Line> findLinesOutput() {
		return findLinesOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Lines.
	 * @return ArrayList<Line> output from Filter_Lines.
	 */
	public ArrayList<Line> filterLinesOutput() {
		return filterLinesOutput;
	}


	/**
	 * Resizes an image.
	 * @param src The image to resize.
	 * @param dSize size to set the image.
	 * @param fx scale factor along X axis.
	 * @param fy scale factor along Y axis.
	 * @param interpolation type of interpolation to use.
	 * @param dst output image.
	 */
	private void cvResize(Mat src, Size dSize, double fx, double fy, int interpolation,
		Mat dst) {
		if (dSize==null) {
			dSize = new Size(0,0);
		}
		Imgproc.resize(src, dst, dSize, fx, fy, interpolation);
	}

	/**
	 * Converts a color image into shades of grey.
	 * @param input The image on which to perform the desaturate.
	 * @param output The image in which to store the output.
	 */
	private void desaturate(Mat input, Mat output) {
		switch (input.channels()) {
			case 1:
				// If the input is already one channel, it's already desaturated
				input.copyTo(output);
				break;
			case 3:
				Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
				break;
			case 4:
				Imgproc.cvtColor(input, output, Imgproc.COLOR_BGRA2GRAY);
				break;
			default:
				throw new IllegalArgumentException("Input to desaturate must have 1, 3, or 4 channels");
		}
	}

	/**
	 * Apply a fixed-level threshold to each array element in an image.
	 * @param src Image to threshold.
	 * @param threshold threshold value.
	 * @param maxVal Maximum value for THRES_BINARY and THRES_BINARY_INV
	 * @param type Type of threshold to appy.
	 * @param dst output Image.
	 */
	private void cvThreshold(Mat src, double threshold, double maxVal, int type,
		Mat dst) {
		Imgproc.threshold(src, dst, threshold, maxVal, type);
	}

	/**
	 * Expands area of lower value in an image.
	 * @param src the Image to erode.
	 * @param kernel the kernel for erosion.
	 * @param anchor the center of the kernel.
	 * @param iterations the number of times to perform the erosion.
	 * @param borderType pixel extrapolation method.
	 * @param borderValue value to be used for a constant border.
	 * @param dst Output Image.
	 */
	private void cvErode(Mat src, Mat kernel, Point anchor, double iterations,
		int borderType, Scalar borderValue, Mat dst) {
		if (kernel == null) {
			kernel = new Mat();
		}
		if (anchor == null) {
			anchor = new Point(-1,-1);
		}
		if (borderValue == null) {
			borderValue = new Scalar(-1);
		}
		Imgproc.erode(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
	}

	public static class Line {
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
	/**
	 * Finds all line segments in an image.
	 * @param input The image on which to perform the find lines.
	 * @param lineList The output where the lines are stored.
	 */
	private void findLines(Mat input, ArrayList<Line> lineList) {
		final LineSegmentDetector lsd = Imgproc.createLineSegmentDetector();
		final Mat lines = new Mat();
		lineList.clear();
		if (input.channels() == 1) {
			lsd.detect(input, lines);
		} else {
			final Mat tmp = new Mat();
			Imgproc.cvtColor(input, tmp, Imgproc.COLOR_BGR2GRAY);
			lsd.detect(tmp, lines);
		}
		if (!lines.empty()) {
			for (int i = 0; i < lines.rows(); i++) {
				lineList.add(new Line(lines.get(i, 0)[0], lines.get(i, 0)[1],
					lines.get(i, 0)[2], lines.get(i, 0)[3]));
			}
		}
	}

	/**
	 * Filters out lines that do not meet certain criteria.
	 * @param inputs The lines that will be filtered.
	 * @param minLength The minimum length of a line to be kept.
	 * @param angle The minimum and maximum angle of a line to be kept.
	 * @param outputs The output lines after the filter.
	 */
	private void filterLines(List<Line> inputs,double minLength,double[] angle,
		List<Line> outputs) {
		outputs = inputs.stream()
				.filter(line -> line.lengthSquared() >= Math.pow(minLength,2))
				.filter(line -> (line.angle() >= angle[0] && line.angle() <= angle[1])
				|| (line.angle() + 180.0 >= angle[0] && line.angle() + 180.0 <= angle[1]))
				.collect(Collectors.toList());
	}




}

