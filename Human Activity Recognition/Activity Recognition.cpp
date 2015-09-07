/*

Author Name : Sasidhar Evuru   Net ID :: sxe140630
*/

#include <opencv2/core/core.hpp>

#include <opencv2/highgui/highgui.hpp>

#include <opencv2/opencv.hpp>

#include <opencv2/imgproc/imgproc.hpp>

#include <opencv2/objdetect/objdetect.hpp>

#include <opencv2/calib3d/calib3d.hpp>

#include <vector>

#include <iostream>

#include <time.h>

#include <fstream>

#include <sstream>

#include <windows.h>

#include <fileapi.h>

#include <opencv2/nonfree/nonfree.hpp>

#include <opencv2/ml/ml.hpp>

#include "opencv2/video/tracking.hpp"

#include "opencv2/highgui/highgui.hpp"

#include "opencv2/imgproc/imgproc_c.h"

#include <time.h>

#include <stdio.h>

#include <ctype.h>



#include "opencv2/opencv.hpp"

#include <iostream>

#include <ctime>

#include <fstream>

#include <opencv2/nonfree/features2d.hpp>

#include<map>

using namespace std;

using namespace cv;







using namespace cv;

using namespace std;





// various tracking parameters (in seconds)

const double MHI_DURATION = 1;

// number of cyclic frame buffer used for motion detection



const int N = 7; // uisng the 4th shami







Ptr<DescriptorMatcher> matcher = DescriptorMatcher::create("FlannBased");

Ptr<DescriptorExtractor> extractor = new SurfDescriptorExtractor();

SurfFeatureDetector detector(1500);

//---dictionary size=number of cluster's centroids

int dictionarySize = 100;

TermCriteria tc(CV_TERMCRIT_ITER, 10, 0.001);

int retries = 1;

int flags = KMEANS_PP_CENTERS;

BOWKMeansTrainer bowTrainer(dictionarySize, tc, retries, flags);

BOWImgDescriptorExtractor bowDE(extractor, matcher);



// replacing the .avi iwth the .jpg

bool replace(std::string& str, const std::string& from, const std::string& to) {

	size_t start_pos = str.find(from);

	if (start_pos == std::string::npos)

		return false;

	str.replace(start_pos, from.length(), to);

	return true;

}





void collectclasscentroids() {

	IplImage * img;

	string line;

	ifstream myfile4("train.txt"); // reading the train file.

	while (getline(myfile4, line)) // reading the file line by line

	{

		istringstream iss(line);

		vector<string> tokens{ istream_iterator < string > {iss},

			istream_iterator < string > {} }; // breaking the string with space separation



		std::vector<String>::iterator it = tokens.begin();

		int classvalue = stoi(*it); // getting the class value

		++it;



		for (; it != tokens.end(); ++it)

		{

			string imageName = *it;			// getting the filepath.

			bool result = replace(imageName, ".avi", ".jpg"); // replacing the .avi with .jpg

			imageName = "generated\\" + imageName; // file path where the samhi image has to be extracted

			cout << "collectclasscentroids for " << imageName << endl;

			if (result == false)

			{

				cout << "cant do the replacement" << "\n";

				return;

			}

			img = cvLoadImage(imageName.c_str(), 0);

			vector<KeyPoint> keypoint;

			detector.detect(img, keypoint); // detecting teh key points in the image

			Mat features;

			extractor->compute(img, keypoint, features); // extracting the feautres

			bowTrainer.add(features); // adding the feautres to the trainer



		}

	}

	myfile4.close();

	return;

}





int main(int argc, char** argv)

{

	CvCapture* capture = 0;

	String line;



	ifstream myfile("train.txt"); // reading the train file.

	if (myfile.is_open())

	{

		while (getline(myfile, line)) // reading the file line by line

		{

			istringstream iss(line);

			vector<string> tokens{ istream_iterator < string > {iss},

				istream_iterator < string > {} };   // breaking the string with space separation



			std::vector<String>::iterator it = tokens.begin();

			int classvalue = stoi(*it); // getting the class value

			++it;



			for (; it != tokens.end(); ++it)

			{

				string filename = *it;

				capture = cvCaptureFromFile(filename.c_str());   // getting the file from the path specified.

				std::cout << "started processing " + filename << "\n";

				Mat Mhi4;

				Size framesize;

				int frameheight;

				int framewidth;

				int i = 0;

				if (capture)

				{

					//cvNamedWindow( "Motion", 1 );

					int framecount = 0; // frame count is extracted to maintinain the component in the MHI between 0 and 1.

					Mat image;

					Mat current;

					Mat diff;

					VideoCapture videoref(filename);

					int totalframecount = 0;

					VideoCapture cap(filename);

					Mat frame;

					do

					{

						cap >> frame;

						totalframecount++;  // counting the total no of frame sin a video.

					} while (!frame.empty());

					videoref >> image;



					Mat prev = image.clone(); // copying it into the roev image.

					cvtColor(prev, prev, CV_RGB2GRAY);



					framesize = prev.size();

					frameheight = framesize.height;

					framewidth = framesize.width;

					Mhi4 = Mat(frameheight, framewidth, CV_32FC1, Scalar(0, 0, 0)); // intializing the MHI with the image properties extracted

					for (;;)

					{

						videoref >> current;



						if (current.empty())

							break;



						cvtColor(current, current, CV_RGB2GRAY);

						if (framecount%N == 0)

						{

							absdiff(prev, current, diff); // getting the absolute difference between the prev frame and the current frame

							threshold(diff, diff, 25, 255, THRESH_BINARY); // thresholding the extracted diff

							updateMotionHistory(diff, Mhi4, (double)framecount / totalframecount, MHI_DURATION); // code to update the MHI. Here framecount/totalframecount varies from 0 to 1 from start to end of the video

							prev = current.clone(); // reassigning the current to the previpous.

						}





						if (cvWaitKey(1) >= 0)

							break;



						framecount++;





					}



					Mhi4.convertTo(Mhi4, CV_8UC1, 255, 0);

					char outputfilename[100];

					bool result = replace(filename, ".avi", ".jpg"); // replacing the .avi with .jog

					string outing = "generated//" + filename; // file name of the mhi

					sprintf(outputfilename, outing.c_str());

					imwrite(outputfilename, Mhi4); // writing to the created MHI





					cvReleaseCapture(&capture); // releasing the cature on the video.

				}

			}

		}

	}



	else

	{

		cout << "cant open the file" << "\n";

		return 0;

	}



	myfile.close();





	// Traingin the machine learning algorithm





	IplImage *img2;

	cout << "Vector quantization..." << endl;

	collectclasscentroids();

	cout << "done with class centroids";

	vector<Mat> descriptors = bowTrainer.getDescriptors();

	int count = 0;

	for (vector<Mat>::iterator iter = descriptors.begin(); iter != descriptors.end(); iter++)

	{

		count += iter->rows;

	}

	cout << "Clustering " << count << " features" << endl;



	//choosing cluster's centroids as dictionary's words

	Mat dictionary = bowTrainer.cluster();

	bowDE.setVocabulary(dictionary);

	cout << "extracting histograms in the form of BOW for each image " << endl;

	Mat labels(0, 1, CV_32FC1);

	Mat trainingData(0, dictionarySize, CV_32FC1);

	int k = 0;

	vector<KeyPoint> keypoint1;

	Mat bowDescriptor1;



	//extracting histogram in the form of bow for each image 







	ifstream myfile5("train.txt");

	if (myfile5.is_open())

	{

		while (getline(myfile5, line))

		{

			istringstream iss(line);

			vector<string> tokens{ istream_iterator < string > {iss},

				istream_iterator < string > {} };



			std::vector<String>::iterator it = tokens.begin();

			int classvalue = stoi(*it);

			++it;



			for (; it != tokens.end(); ++it)

			{



				string imageName = *it;

				bool result = replace(imageName, ".avi", ".jpg");

				imageName = "generated//" + imageName; // saving them in a different folder.

				if (result == false)

				{

					cout << "cant do the replacement" << "\n";

					return 0;

				}

				img2 = cvLoadImage(imageName.c_str(), 0);



				detector.detect(img2, keypoint1); // detecting the key points 



				bowDE.compute(img2, keypoint1, bowDescriptor1); // computing the descriptors for the key points



				trainingData.push_back(bowDescriptor1); // saving the bow descriptor



				labels.push_back((float)classvalue); // saving the Groud truth for comaprison

			}

		}

	}



	myfile5.close();









	CvSVMParams params;

	params.kernel_type = CvSVM::RBF;

	params.svm_type = CvSVM::C_SVC;

	params.gamma = 0.2;

	params.C = 200.50000000000000;

	params.term_crit = cvTermCriteria(CV_TERMCRIT_ITER, 100, 0.000001);

	CvSVM svm;







	printf("%s\n", "Training SVM classifier");



	bool res = svm.train(trainingData, labels, cv::Mat(), cv::Mat(), params);



	cout << "Processing evaluation data..." << endl;





	Mat groundTruth(0, 1, CV_32FC1);

	Mat evalData(0, dictionarySize, CV_32FC1);

	k = 0;

	vector<KeyPoint> keypoint2;

	Mat bowDescriptor2;





	Mat results(0, 1, CV_32FC1);;



	// creating the MHI for the test files

	int totalframecount = 200;

	VideoCapture videoref(0);

	int framecount = 0;

	Mat image;

	Mat current;

	Mat diff;

	videoref >> image;

	Mat prev = image.clone();

	Mat Mhi4;

	Size framesize;

	int frameheight;

	int framewidth;

	cvtColor(prev, prev, CV_RGB2GRAY);



	framesize = prev.size();

	frameheight = framesize.height;

	framewidth = framesize.width;

	Mhi4 = Mat(frameheight, framewidth, CV_32FC1, Scalar(0, 0, 0));

	String action = "recording";

	float response = 0;

	for (;;)

	{



		ostringstream convert;



		videoref >> current;



		if (current.empty())

			break;



		cvtColor(current, current, CV_RGB2GRAY);

		if (framecount%N == 0 && framecount <= totalframecount)

		{

			absdiff(prev, current, diff);

			threshold(diff, diff, 25, 255, THRESH_BINARY);

			updateMotionHistory(diff, Mhi4, (double)framecount / totalframecount, MHI_DURATION);

			prev = current.clone();

		}





		if (cvWaitKey(1) >= 0)

			break;



		framecount++;

		

		if (framecount >= totalframecount)

		{

			Mhi4.convertTo(Mhi4, CV_8UC1, 255, 0);

			detector.detect(Mhi4, keypoint2);

			bowDE.compute(Mhi4, keypoint2, bowDescriptor2);



			evalData.push_back(bowDescriptor2);

			response = svm.predict(bowDescriptor2);



			if (response == 1)

			{

				action = "walk";

			}

			else if (response == 2)

			{

				action = "run";

			}

			else if (response == 3)

			{

				action = "jump";

			}

			else if (response == 4)

			{

				action = "side";

			}

			else if (response == 5)

			{

				action = "bend";

			}

			else if (response == 6)

			{

				action = "wave1";

			}

			else if (response == 7)

			{

				action = "wave2";

			}

			else if (response == 8)

			{

				action = "One_leg";

			}

			else if (response == 9)

			{

				action = "jack";

			}

			else if (response == 10)

			{

				action = "skip";

			}





		}

		convert << response;

		putText(current, "class is :: ", cvPoint(30, 30),

			FONT_HERSHEY_COMPLEX_SMALL, 0.8, cvScalar(200, 200, 250), 1, CV_AA);



		putText(current, action, cvPoint(30, 60),

			FONT_HERSHEY_COMPLEX_SMALL, 0.8, cvScalar(200, 200, 250), 1, CV_AA);



		imshow("mhiimage", Mhi4);

		imshow("originalimage", current);



		if ((framecount >= totalframecount))

		{

			cvWaitKey(2000);

			action = "recording";

			framecount = 0;

			Mhi4 = Mat(frameheight, framewidth, CV_32FC1, Scalar(0, 0, 0));

		}





		cvReleaseCapture(&capture);



	}

}