package com.opencvoperations;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OCRDemo {

	private JFrame frame;
	Tesseract ts ; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OCRDemo window = new OCRDemo();
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
	public OCRDemo() {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
			initialize();						
		} catch (Exception ex) {
			System.out.println("ERROR at Init :" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "ERROR at Init :" + ex.getMessage());
		}
	}
	
	private BufferedImage getImage(String imgPath) {
		//read image
		try {
			Mat mat = Imgcodecs.imread(imgPath);
			
			//change image to gray scale
			Mat gray = new Mat();		
			Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
			
			//resize image
			Mat resized = new Mat();
			Size size = new Size(mat.width()*1.9 , mat.height()*1.9);
			Imgproc.resize(gray, resized, size);
			
			//convert to bufefred image
			MatOfByte mof = new MatOfByte();
			byte imgBytes[];
			Imgcodecs.imencode(".png", resized, mof);
			imgBytes = mof.toArray();
			BufferedImage buffImage = ImageIO.read(new ByteArrayInputStream(imgBytes));
			return buffImage;
			
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			System.out.println("ERROR at getImage :" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "ERROR at getImage :" + ex.getMessage());
			return null;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 792, 668);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnTryOCR = new JButton("START OCR");
		btnTryOCR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					ts = new Tesseract();
					ts.setDatapath("");
					ts.setLanguage("eng");
					
					String txt = ts.doOCR(getImage("images/kart.jpg")); //sample image in images folder to read..
					System.out.println("OCR :  " + txt);
					JOptionPane.showMessageDialog(null, "OCR :  " + txt);
					
				} catch (TesseractException ex) {
					System.out.println("ERROR at TryOCR :" + ex.getMessage());
				}
			}
		});
		btnTryOCR.setBounds(10, 64, 161, 23);
		frame.getContentPane().add(btnTryOCR);
	}
}
