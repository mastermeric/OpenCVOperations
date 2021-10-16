package com.opencvoperations;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JRadioButton;

public class DrawShapeOnImage {

	private JFrame frame;
	Mat _image , tempImage, tempImagePolyLine;
	JLabel imageView;		
	JRadioButton rdLine , rdCircle, rdRectangle, rdbPolyLine, rdPolyBrush;	
	private Point originPoint ;
	private List<Point> _points = new ArrayList<>();

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawShapeOnImage window = new DrawShapeOnImage();
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
	public DrawShapeOnImage() {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			initialize();
			
			rdLine.setSelected(true); //  Default cizim icin Line sec						
			myDrawType = myLine;
			
		} catch (Exception ex) {
			System.out.println("ERROR at Init :" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "ERROR at Init :" + ex.getMessage());
		}
	}
	
	public static String myCircle = "circle";
	public static String myRectangle ="rectangle";
	public static String myLine ="line";
	public static String myPolyLine = "PolyLine";
	public static String myPolyBrush = "PolyBrush";
	public static String myDrawType = "";
	
	void loadImage(Mat img) {
		final MatOfByte mof = new MatOfByte();
		Imgcodecs.imencode(".jpg", img, mof);
		final byte[] imageData = mof.toArray();
		final ImageIcon icon = new ImageIcon(imageData);
		imageView.setIcon(icon);
	}	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 862, 828);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		imageView = new JLabel("IMAGE  ...");
		imageView.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				originPoint = new Point(e.getX(), e.getY());
				
				
				if(myDrawType.equals(myPolyLine)) {
					//polygon cizim  ==>  No need of mouse dragged event
					tempImage = _image.clone();
					_points.add(originPoint);
					final Point[] arrayPoint = _points.toArray(new Point[0]);
					Imgproc.polylines(tempImage, 
							Arrays.asList(new MatOfPoint(arrayPoint)),
							true, // closed
							new Scalar(0,0,255), // color
							5 // tickness
							);					
				}
				
				if(tempImage != null) {
					loadImage(tempImage);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				//When mouse released replace/update the image ..				
				if(tempImage != null) {
				_image = tempImage.clone();
				}				
			}
		});
		
		imageView.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
				final Point point = new Point(e.getX(), e.getY());
				
				tempImage = _image.clone();								
				
				if(myDrawType.equals(myLine)) {
				//line draw :
					Imgproc.line(tempImage, originPoint, point, new Scalar(0,0,255),5);
				}
				
				if(myDrawType.equals(myRectangle)) {
				//rectangle draw :
					Imgproc.rectangle(tempImage, originPoint, point, new Scalar(255,0,0),5);
				}
				
				if(myDrawType.equals(myCircle)) {
				// circle draw :  first find distance
				double ab2 = Math.pow(originPoint.x - point.x, 2) + Math.pow(originPoint.y - point.y, 2) ;
				int distance = (int)Math.sqrt(ab2);
				Imgproc.circle(tempImage, originPoint, distance, new Scalar(0,255,0),5);
				}
				
				
				if(myDrawType.equals(myPolyBrush)) {
					//polygon cizim  ==>  No need of mouse dragged event
					tempImage = _image.clone();
					
					_points.add(point);
					final Point[] arrayPoint = _points.toArray(new Point[0]);
					Imgproc.polylines(tempImage, 
							Arrays.asList(new MatOfPoint(arrayPoint)),
							false, // closed area draw
							new Scalar(50,50,50), // color
							5 // tickness
							);
					}		
								
				
				// Save modified image ..
				loadImage(tempImage);
			}
		});
		
		imageView.setBounds(150, 171, 641, 516);
		frame.getContentPane().add(imageView);
		
		rdLine = new JRadioButton("Line");
		rdLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(rdLine.isSelected()) {
					myDrawType = myLine;
					rdRectangle.setSelected(false);
					rdCircle.setSelected(false);
					rdbPolyLine.setSelected(false);
					rdPolyBrush.setSelected(false);
				}
			}
		});
		rdLine.setBounds(6, 51, 111, 23);
		frame.getContentPane().add(rdLine);
		
		rdRectangle = new JRadioButton("Rectangle");
		rdRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdRectangle.isSelected()) {
					myDrawType = myRectangle;
					rdLine.setSelected(false);
					rdCircle.setSelected(false);
					rdbPolyLine.setSelected(false);
					rdPolyBrush.setSelected(false);
				}
			}
		});
		rdRectangle.setBounds(6, 77, 111, 23);
		frame.getContentPane().add(rdRectangle);
		
		rdCircle = new JRadioButton("Circle");
		rdCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdCircle.isSelected()) {
					myDrawType = myCircle;
					rdLine.setSelected(false);
					rdRectangle.setSelected(false);
					rdbPolyLine.setSelected(false);
					rdPolyBrush.setSelected(false);
				}
			}
		});
		rdCircle.setBounds(6, 103, 111, 23);
		frame.getContentPane().add(rdCircle);
		
		
		rdbPolyLine = new JRadioButton("Poly Line");
		rdbPolyLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawType = myPolyLine;
				rdLine.setSelected(false);
				rdRectangle.setSelected(false);				
				rdCircle.setSelected(false);
				rdPolyBrush.setSelected(false);
			}
		});
		rdbPolyLine.setBounds(6, 129, 111, 23);
		frame.getContentPane().add(rdbPolyLine);
		
		rdPolyBrush = new JRadioButton("Poly Brush");
		rdPolyBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				myDrawType = myPolyBrush;
				rdLine.setSelected(false);
				rdRectangle.setSelected(false);				
				rdCircle.setSelected(false);
				rdbPolyLine.setSelected(false);
			}
		});
		rdPolyBrush.setBounds(6, 155, 111, 23);
		frame.getContentPane().add(rdPolyBrush);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("ISLEMLER");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Dosya AC");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				_image = Imgcodecs.imread("images/kart.jpg");
				loadImage(_image);
				imageView.setVisible(true);
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Kapa");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				imageView.setVisible(false);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Kaydet");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				Imgcodecs.imwrite("images/out.jpg", _image);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);
	}
}
