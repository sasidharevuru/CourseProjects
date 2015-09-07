/*
Author Name : Sasidhar Evuru
Netd Id		: sxe140630
*/
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <opencv2/calib3d/calib3d.hpp>
#include <opencv2/core/core.hpp>	
#include <opencv2/highgui/highgui.hpp>
#include "opencv/cv.h"
#include <iostream>
#include <time.h>
#include <stdint.h>


using namespace cv;
using namespace std;

void calcBoardCornerPositions(Size boardSize, float squareSize, vector<Point3f>& corners)
{
	corners.clear();
	for (int i = 0; i < boardSize.height; ++i)
		for (int j = 0; j < boardSize.width; ++j)
			corners.push_back(Point3f(float(j*squareSize), float(i*squareSize), 0));
}

int main(int argc, char* argv[])
{

	VideoCapture videoref(0); // opening the video file to read.

	if (!videoref.isOpened()) // checking if video is opened.
	{
		cout << "unable to open the video file" << endl;
		return -1;
	}

	videoref.set(CV_CAP_PROP_POS_MSEC, 500);

	Mat frame;

	int no_of_frames = 0;

	vector<vector<Point2f> > imagePoints;
	vector<vector<Point3f> > objectPoints(1);
	vector<Point2f> pointBuf;
	vector<Mat> rvecs;
	vector<Mat> tvecs;
	Mat cameraMatrix;
	Mat distCoeffs;
	int erosion_type = MORPH_ELLIPSE;
	int erosion_size = 4;
	int dilation_type = MORPH_ELLIPSE;
	int dilation_size = 6;
	Mat binaryframe;
	Mat erodedframe;
	Mat dilatedframe;
	cameraMatrix = Mat::eye(3, 3, CV_64F);
	distCoeffs = Mat::zeros(8, 1, CV_64F);
	int count = 0;
	while (true)
	{


		videoref >> frame; // getting the frame from video.
		Size sizeofchessboard(6, 4);

		bool chessboardfound;

		imshow("before count", frame);

		chessboardfound = findChessboardCorners(frame, sizeofchessboard, pointBuf, CV_CALIB_CB_ADAPTIVE_THRESH | CV_CALIB_CB_FAST_CHECK | CV_CALIB_CB_NORMALIZE_IMAGE);



		double fx;
		double fy;
		double cx;
		double cy;

		if (count < 10 && chessboardfound)
		{
			/*Mat viewGray;
			cvtColor(frame, viewGray, CV_BGR2GRAY);
			cornerSubPix(viewGray, pointBuf, Size(13, 9),
			Size(-1, -1), TermCriteria(CV_TERMCRIT_EPS + CV_TERMCRIT_ITER, 30, 0.1));*/

			imagePoints.push_back(pointBuf);



			calcBoardCornerPositions(sizeofchessboard, 30, objectPoints[0]);

			objectPoints.resize(imagePoints.size(), objectPoints[0]);


			double rms = calibrateCamera(objectPoints, imagePoints, frame.size(), cameraMatrix, distCoeffs, rvecs, tvecs, CV_CALIB_FIX_K4 | CV_CALIB_FIX_K5);

			count++;

			cout << "count is :" << count << endl;

		}
		else
		{
			fx = cameraMatrix.at<double>(0, 0);
			fy = cameraMatrix.at<double>(1, 1);
			cx = cameraMatrix.at<double>(0, 2);
			cy = cameraMatrix.at<double>(1, 2);
			Mat hsvframe;
			cvtColor(frame, hsvframe, CV_BGR2HSV); // creating a new frame with the hsv format of the given BGR frame.

			inRange(hsvframe, Scalar(110, 50, 50), Scalar(130, 255, 255), binaryframe); // marking the red color and the scalar are for detecting the 

			Mat element = getStructuringElement(erosion_type,
				Size(2 * erosion_size + 1, 2 * erosion_size + 1),
				Point(erosion_size, erosion_size));

			erode(binaryframe, erodedframe, element); // The eroded frame is kept in the erodedframe.

			// Apply the dilation operation

			Mat element1 = getStructuringElement(dilation_type,
				Size(2 * dilation_size + 1, 2 * dilation_size + 1),
				Point(dilation_size, dilation_size));

			dilate(erodedframe, dilatedframe, element1); // The dilated frame is kept in the dilatedframe.

			std::vector< std::vector<cv::Point> > contours;
			std::vector<cv::Point> points;
			cv::findContours(dilatedframe, contours, CV_RETR_LIST, CV_CHAIN_APPROX_NONE);
			double width;
			double height;
			for (size_t i = 0; i < contours.size(); i++) {
				for (size_t j = 0; j < contours[i].size(); j++) {
					cv::Point p = contours[i][j];
					points.push_back(p); // saving the points 
				}

				if (points.size() > 0)
				{
					cv::Rect brect = cv::boundingRect(cv::Mat(points).reshape(2));
					cv::rectangle(frame, brect.tl(), brect.br(), cv::Scalar(100, 100, 200), 2, CV_AA);
					width = brect.br().x - brect.tl().x;
					height = brect.br().y - brect.tl().y;

					int z = 320;
					double adjfocallengthx = fx + cx;
					double adjfocallengthy = fy + cy;

					double focalxbymm = adjfocallengthx / z;
					double focalybymm = adjfocallengthy / z;

					double originalimagewidth = width / focalxbymm;
					double originalimageheight = height / focalybymm;

					points.clear();  // clearing the points once the rectangle is formed.

					stringstream ss;
					ss << originalimagewidth;
					string str = ss.str(); // converting the integer value to the string.
					str = "Original width :: " + str; // concatenating the string
					Point textOrg(5, 50);
					putText(frame, str, textOrg, FONT_HERSHEY_PLAIN, 2, Scalar::all(255), 3, 8); // putting a tex label on the image.


					ss << originalimageheight;
					str = ss.str();
					str = "Original height :: " + str;
					Point textOrg1(5, 100);
					putText(frame, str, textOrg1, FONT_HERSHEY_PLAIN, 2, Scalar::all(255), 3, 8); // putting a tex label on the image.

					imshow("Grabbed frame", frame);

				}

			}




		}

		cvWaitKey(2000); // waiting for till 0.02 sec
	}
}

