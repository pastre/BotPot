package common.screen;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class CaptureWindow {

	public static CaptureWindow windowReader;
	
	public static void main(String[] args){
		windowReader = new CaptureWindow();
	}
	
	public CaptureWindow(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		
		if(!gd.isWindowTranslucencySupported(TRANSLUCENT)){
			System.err.println("Nao tem transparencia");
			System.exit(0);
		}
		
		TransparentFrame tw = new TransparentFrame();
		tw.setOpacity(0.55F);
		tw.setVisible(true);
		
	}
	
	class TransparentFrame extends JFrame implements MouseMotionListener, ActionListener{

		public  TransparentFrame() {
			addMouseMotionListener( this);
			setUndecorated(true);
			setLayout(new GridBagLayout());
			setSize(150, 150);
			setLocation(200,  200);
			ThreadReadData t = new ThreadReadData();
			t.windowReference = this;
			t.start();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			this.setLocation(e.getLocationOnScreen().x - this.getSize().width/2, e.getLocationOnScreen().y - this.getSize().height/2 );
			
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {}


		@Override
		public void mouseMoved(MouseEvent arg0) {}
		
	}
	
	class ThreadReadData extends Thread{
		public TransparentFrame windowReference;
		
		public void run(){
			try{
				while(true){
					if(this.windowReference.isShowing()){
						Robot robot;
						try{
							robot = new Robot();
							BufferedImage screenShot = robot.createScreenCapture(new Rectangle(windowReference.getLocationOnScreen().x,  windowReference.getLocationOnScreen().y, windowReference.getSize().width, windowReference.getSize().height));
							Graphics2D graphics = screenShot.createGraphics();
							final File f = new File("res\\");
							ImageIO.write(screenShot, "png", new File(f.getAbsolutePath() + "\\nome.png"));
						}catch(IOException e){
							e.printStackTrace();
						}
					}
					this.sleep(1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


}
