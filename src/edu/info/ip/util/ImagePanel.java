package edu.info.ip.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel{

	private BufferedImage image = null;
	
	boolean aspectRatio = true;
	boolean fitToScreen = true;
	boolean centerImage = true;
	double scaleValue = 1.0;
	
	public ImagePanel() {
		super();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
//		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		if(image == null) return;
		
		if(fitToScreen) {
			if(aspectRatio) {
				
				double scaleW = (double)getWidth() / image.getWidth();
				double scaleH = (double)getHeight() / image.getHeight();
				scaleValue = Math.min(scaleW, scaleH);
				
				int w = (int)(scaleValue * image.getWidth());
				int h = (int)(scaleValue * image.getHeight());
				
				if(centerImage) {
					g.drawImage(image, (getWidth()-w)/2, (getHeight()-h)/2, w, h , null);
				}
				else {
				g.drawImage(image,0,0,(int)(scaleValue * image.getWidth()),
						(int)(scaleValue * image.getHeight()),null);
				}
				
 			}
			else {
				g.drawImage(image,0,0,getWidth(),getHeight(),null);
			}
		}
		else {
			g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}

	public boolean isAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(boolean aspectRatio) {
		this.aspectRatio = aspectRatio;
		repaint();
	}

	public boolean isFitToScreen() {
		return fitToScreen;
	}

	public void setFitToScreen(boolean fitToScreen) {
		this.fitToScreen = fitToScreen;
		repaint();
	}

	public double getScaleValue() {
		return scaleValue;
	}

	public void setScaleValue(double scaleValue) {
		this.scaleValue = scaleValue;
		repaint();
	}

	public boolean isCenterImage() {
		return centerImage;
	}

	public void setCenterImage(boolean centerImage) {
		this.centerImage = centerImage;
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (image == null)
			return new Dimension(200, 200);
		if (fitToScreen)
			return new Dimension(0, 0);
		
		int width = (int) (scaleValue * image.getWidth());
		int height = (int) (scaleValue * image.getHeight());
		return new Dimension(width, height);
	}

//	@Override
//	public Dimension getMinimumSize() {
//		return new Dimension(200, 200);
//	}
//
//	@Override
//	public Dimension getMaximumSize() {
//		return (image != null)
//				? new Dimension((int) (scaleValue * image.getWidth()), (int) (scaleValue * image.getHeight()))
//				: new Dimension(200, 200);
//	}
}
