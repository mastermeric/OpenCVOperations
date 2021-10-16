package com.opencvoperations;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FaceDetectionDemo {

	private JFrame frame;
	Mat _image;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FaceDetectionDemo window = new FaceDetectionDemo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FaceDetectionDemo() {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
			initialize();						
		} catch (Exception ex) {
			System.out.println("ERROR at Init :" + ex.getMessage());
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 788, 675);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnDetectFace = new JButton("DETECT FACES");
		btnDetectFace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				_image = Imgcodecs.imread("images/faces.jpg");
				detectAndSave(_image);				
			}
		});
		btnDetectFace.setBounds(10, 33, 109, 23);
		frame.getContentPane().add(btnDetectFace);
	}
	
	private void detectAndSave(Mat image) {
		
		try {
			//Store more than one faces
			MatOfRect faces = new MatOfRect(); 
			
			//Convert to gray scale
			Mat grayFrame = new Mat();
			Imgproc.cvtColor(_image, grayFrame, Imgproc.COLOR_BGR2GRAY);
			
			//improve conrast for better result
			Imgproc.equalizeHist(grayFrame, grayFrame);
			
			int height = grayFrame.height();
			int absoluteFaceSize = 0;
			if(Math.round(height)*0.2 >0 ) {
				absoluteFaceSize = (int)Math.round(height * 0.2);
			}
			
			//Detect faces
			CascadeClassifier faceCascade = new CascadeClassifier();		
					
			//load trained data
			faceCascade.load("data/haarcascade_frontalface_alt2.xml");
			
			//detect faces
			faceCascade.detectMultiScale(
					grayFrame, // image: Matrix of the type CV_8U containing an image where objects are detected. 
					faces, // MatOfRect objects : Vector of rectangles where each rectangle contains the detected
					1.1, //scaleFactor: Parameter specifying how much the image size is reduced at each image scale.
					2,  //minNeighbors: Parameter specifying how many neighbors each candidate rectangle should haveto retain it.
					0|Objdetect.CASCADE_SCALE_IMAGE, // flags Parameter with the same meaning for an old cascade as in the functioncvHaarDetectObjects. It is not used for a new cascade.
					new Size(absoluteFaceSize,absoluteFaceSize), //minSize Minimum possible object size
					new Size()  //maxSize Maximum possible object size
					);
			
			//write rectangle on the file of each face
			Rect[] faceArray = faces.toArray();
			for (int i=0; i<faceArray.length;i++) {
				Imgproc.rectangle(image, faceArray[i], new Scalar(0,200,0), 3);			
			}
			
			//write file as output jpeg..
			Imgcodecs.imwrite("images/detectedfaces.jpg", image);
			
	        	        
			System.out.println("Success");
			//JOptionPane.showMessageDialog(null, "I am happy.");
			
			
			String[] options = {"Ýþlem Baþarýlý\n","Sorun Oluþtu\n","Diðer..\n"};			
			Object selection = JOptionPane.showInputDialog(
                    null,
                    "Tercih yapabilirsiniz: ",
                    "Sonuç",
                    // JOptionPane.DEFAULT_OPTION, // Removed this to make it work
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
			
			System.out.println(selection);
			
		} catch (Exception ex) {						
			System.out.println("ERROR at detectAndSave :" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "ERROR at detectAndSave :" + ex.getMessage());
		}
	}

}
