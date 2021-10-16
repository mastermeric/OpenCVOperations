package com.opencvoperations;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class KameraDisplay {

	private JFrame frame;

	private VideoCapture _captureIPCamera;
	private Mat _image ;
	private boolean _clicked = false;
	JLabel cameraScreen;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KameraDisplay window = new KameraDisplay();
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
	public KameraDisplay() {
		
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			initialize();
			System.out.println("LOAD OK..");
		} catch (Exception ex) {
			System.out.println("ERROR at Init :" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "ERROR at Init :" + ex.getMessage());
		}				
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 833, 612);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("START");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				_clicked= true;
				
				EventQueue.invokeLater( new Runnable() {			
					@Override
					public void run() {												
						new Thread(  new Runnable() {							
							@Override
							public void run() {
								threadMethod2();
							}
						}).start();
					}
				});			
			}
		});
		btnNewButton.setBounds(46, 42, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("SAVE");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String name = JOptionPane.showInputDialog(this,"Enter Image Name");
//				if(name.equals("Enter Image Name")) {
//					name = new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
//				}
				
				String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				Imgcodecs.imwrite("images/" + name + ".jpg",_image);
			}
		});
		btnNewButton_1.setBounds(46, 87, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		cameraScreen = new JLabel("New label");
		cameraScreen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cameraScreen.setBounds(198, 42, 487, 453);
		frame.getContentPane().add(cameraScreen);
	}
	
	
	private void threadMethod2() {		
		_captureIPCamera = new VideoCapture(0);// ilk kamerayi al..
		_image = new Mat();
		byte[] imageData;
		Image image2;
		ImageIcon _icon ;
		System.out.println("threadMethod Stage --> OK");		
		while(true) 
		{
			//System.out.println("-------infinite loop 1 ");
			_captureIPCamera.read(_image);
			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", _image, buf);
			imageData = buf.toArray();
			InputStream istream = new ByteArrayInputStream(imageData);
			
			_icon = new ImageIcon(imageData);
			cameraScreen.setIcon(_icon);			
		}
	} 
}
