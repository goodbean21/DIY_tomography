package gui;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import imageProcessing.Processing;

public class ImagePanel extends JPanel implements MouseWheelListener{

	private static final long serialVersionUID = 1L;
	protected Image image;
	static int current = 0;
	protected static ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
	protected LabelPanel mySouth;
	
	public Image getImage() {
		return image;
		
	}
	
	public void setMysouth(LabelPanel mySouth) {
		 this.mySouth = mySouth;
		
	}
	
	public static BufferedImage convertToBufferedImage(Image image){
	    BufferedImage newImage = new BufferedImage(
	    		image.getWidth(null), image.getHeight(null),
	    		BufferedImage.TYPE_INT_RGB);
	    
	    Graphics2D g = newImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    
	    return newImage;
	
	}
	
	public static void addList(BufferedImage im) {
		list.add(im);
		
	}
	
	public static void removefromList() {
		if(!list.isEmpty()) list.remove(list.size()-1);
		
	}

	public void setImage(ImageIcon imIcon) {
		this.image = imIcon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
		repaint();
		
	}
	
	public static BufferedImage getCurrent() {
		return list.get(current);
		
	}

	public ImagePanel(){
		addMouseWheelListener(this);
		setBackground(Color.WHITE);
		
	}
	
	public void paint(Graphics g) {
		g.drawImage(image, this.getX(), this.getY(), getWidth(), getHeight(), this);
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(image != null) {
		
			int change = e.getWheelRotation();
			
			current = current + change;
			
			if(current <= 0) {
				current = 0;
				
			}else if(current >= list.size()) {
				current = list.size()-1;
				
			}
					
			ImageIcon imIcon = new ImageIcon(list.get(current));
			this.setImage(imIcon);
			
			Processing.setOriginal(list.get(current));
			
		}

	}
		
	
}
