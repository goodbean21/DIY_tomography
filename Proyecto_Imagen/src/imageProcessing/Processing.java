package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;

import gui.ImagePanel;

public class Processing {
	
	private static String type;
	private static int width, height, red, green, blue, sum;
	protected static BufferedImage original; //loaded image

	protected static ImagePanel myImagePanel;
	private static BufferedImage New;
	static ImageIcon imgIcon;
	
	public static final double RED_FACTOR = 0.299;
	public static final double GREEN_FACTOR = 0.587;
	public static final double BLUE_FACTOR = 0.114;
	
	public static void setOriginal(BufferedImage original) {
		Processing.original = original;
	
	}

	public static void setMyImagePanel(ImagePanel myImagePanel) {
		Processing.myImagePanel = myImagePanel;
	
	}
	
	public static void setType(String type) {
		Processing.type = type;
	}

	public static ImageIcon contraste(double contrast) {
		if(type == null) type = "Lineal";
		
		width = original.getWidth(myImagePanel);
		height = original.getHeight(myImagePanel);
		
		New = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		if(type == "Lineal") {
			for(int i=0; i < height; i++){
				for(int j=0; j < width; j++) {
					Color c = new Color(original.getRGB(j, i));
					red = (int)(c.getRed() * RED_FACTOR * contrast);
					green = (int)(c.getGreen() * GREEN_FACTOR * contrast);
					blue = (int)(c.getBlue() *BLUE_FACTOR * contrast);
					
					sum = red+blue+green;
					
					if(sum > 255) sum = 255;
					
					Color newColor = new Color(sum,sum,sum);
					New.setRGB(j,i,newColor.getRGB());

				}
			}
			
		}else if(type == "Logarítmico") {
			for(int i=0; i < height; i++){
				for(int j=0; j < width; j++) {
					Color c = new Color(original.getRGB(j, i));
					red = (int)(c.getRed() * RED_FACTOR);
					green = (int)(c.getGreen() * GREEN_FACTOR);
					blue = (int)(c.getBlue() *BLUE_FACTOR);
					
					sum = red+blue+green;
					
					double Sum = sum/255.0;
					
					sum = (int) (255*(contrast * Math.log(1+Math.abs(Sum))));
					
					if(sum > 255) sum = 255;
					
					Color newColor = new Color(sum,sum,sum);
					New.setRGB(j,i,newColor.getRGB());
					
				}
			}
			
		}else if(type == "Correción gamma") {
			for(int i=0; i < height; i++){
				for(int j=0; j < width; j++) {
					Color c = new Color(original.getRGB(j, i));
					red = (int)(c.getRed() * RED_FACTOR * contrast);
					green = (int)(c.getGreen() * GREEN_FACTOR * contrast);
					blue = (int)(c.getBlue() *BLUE_FACTOR * contrast);

					sum = red+blue+green;
					double Sum = sum/255.0;
					
					sum = (int) (255 * Math.pow(Sum, (double) contrast));
					
					if(sum > 255) sum = 255;
					
					Color newColor = new Color(sum,sum,sum);
					New.setRGB(j,i,newColor.getRGB());
					
				}
			}
		}
		
		imgIcon = new ImageIcon(New);
		
		return imgIcon;
		
	}
	
	public static ImageIcon Grises(BufferedImage im, int min, int max) {
		
		width = im.getWidth(myImagePanel);
		height = im.getHeight(myImagePanel);
		
		New = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for(int i=0; i < height; i++){
			for(int j=0; j < width; j++) {
				Color c = new Color(im.getRGB(j, i));
				red = (int)(c.getRed() * RED_FACTOR);
				green = (int)(c.getGreen() * GREEN_FACTOR);
				blue = (int)(c.getBlue() * BLUE_FACTOR);
				
				sum = red+blue+green;
				
				if(sum <= min) sum = 0;
				
				if(sum >= max) sum = 0;
				
				Color newColor = new Color(sum,sum,sum);
				New.setRGB(j,i,newColor.getRGB());
				
			}
			
			setOriginal(New);
			
		}
		
		imgIcon = new ImageIcon(New);
		
		return imgIcon;
		
	}
	
	public static ImageIcon Binarizar(BufferedImage im, double thresh) {
		
		width = im.getWidth(myImagePanel);
		height = im.getHeight(myImagePanel);
	
		New = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for(int i=0; i < height; i++){
			for(int j=0; j < width; j++) {
				Color c = new Color(im.getRGB(j, i));
				red = (int)(c.getRed() * RED_FACTOR);
				green = (int)(c.getGreen() * GREEN_FACTOR);
				blue = (int)(c.getBlue() * BLUE_FACTOR);
				
				sum = red+blue+green;
				
				if(sum <= thresh*255) sum = 0;
				else sum = 255;
				
				Color newColor = new Color(sum,sum,sum);
				New.setRGB(j,i,newColor.getRGB());
				
			}
				
		}
		
		setOriginal(New);
		
		imgIcon = new ImageIcon(New);
		
		return imgIcon;		
		
	}
	
	public static ImageIcon mediana(BufferedImage im) {
		
		width = im.getWidth(myImagePanel);
		height = im.getHeight(myImagePanel);
		
		New = new BufferedImage(width-1, height-1, BufferedImage.TYPE_BYTE_GRAY);
		
		int[] R = new int[9];
		int[] G = new int[9];
		int[] B = new int[9];
		Color[] pixel = new Color[9];
		int p = 0;
	
		for(int i = 1; i < width-1; i++) {
			for(int j = 1; j < height-1; j++) {
				for(int s = -1; s <= 1; s++) {
					for(int l = -1; l <= 1; l++) {
						pixel[p]=new Color(im.getRGB(i-s,j-l));
						p++;
						
					}
				}
				p = 0;
               
				for(int k = 0; k < 9; k++){
					R[k] = pixel[k].getRed();
					B[k] = pixel[k].getBlue();
					G[k] = pixel[k].getGreen();
					
				}
				
				Arrays.sort(R);
				Arrays.sort(G);
				Arrays.sort(B);
				
				New.setRGB(i, j, new Color(R[4],G[4],B[4]).getRGB());
			}
			
		}
		
		setOriginal(New);
		
		imgIcon = new ImageIcon(New);
		
		return imgIcon;
		
	}
	
	public static ImageIcon negativo(BufferedImage im) {
		
		width = im.getWidth(myImagePanel);
		height = im.getHeight(myImagePanel);
	
		New = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for(int i=0; i < height; i++){
			for(int j=0; j < width; j++) {
				Color c = new Color(im.getRGB(j, i));
				red = (int)(c.getRed() * RED_FACTOR);
				green = (int)(c.getGreen() * GREEN_FACTOR);
				blue = (int)(c.getBlue() * BLUE_FACTOR);
				
				sum = 255 - (red+blue+green);
				
				Color newColor = new Color(sum,sum,sum);
				New.setRGB(j,i,newColor.getRGB());
				
			}
				
		}
		
		setOriginal(New);
		imgIcon = new ImageIcon(New);
		
		return imgIcon;
		
	}
	
	public static ImageIcon makeFalse (BufferedImage im, double redPercentage,
			double greenPercentage, double bluePercentage) {
		int r,g,b, r2, g2, b2, p;
		int w = original.getWidth();
		int h = original.getHeight();
		BufferedImage fcImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color colorRGB = new Color(original.getRGB(i, j));
				r = (int)(colorRGB.getRed());//* RED_FACTOR);
				g = (int)(colorRGB.getGreen());//* GREEN_FACTOR); //AQUIIIIIII
				b = (int)(colorRGB.getBlue() );//* BLUE_FACTOR);
				
					r2 = (int) Math.round((redPercentage/100)*r) ;
					g2 = (int) Math.round((greenPercentage/100)*g) ;
					b2 = (int) Math.round((bluePercentage/100)*b) ;
					
					p = r2<<16 | g2 << 8 | b2;

					fcImage.setRGB(i, j, p);
				}
	
			}
		
		imgIcon = new ImageIcon(fcImage);
		
		return imgIcon;	
	
	}

	public static ImageIcon mediaPonderada (BufferedImage im) {
		width = im.getWidth(myImagePanel);
		height = im.getHeight(myImagePanel);

		New = new BufferedImage(width-1, height-1, BufferedImage.TYPE_BYTE_GRAY );
		
		double[] R = new double [9];
		double[] G = new double [9];
		double[] B = new double [9];
		Color[] pixel = new Color[9];
		double sumR=0, sumG=0, sumB=0;
		int r2,g2,b2;
		int color;
		
		int p = 0;
		
		for (int i = 1; i < width-1 ; i++) {
			for (int j=1 ; j < height-1 ; j++) {
				for (int s = -1; s <= 1; s++) {
					for (int l = -1; l <= 1; l++) {
						pixel[p] = new Color(im.getRGB(i-s, j-l));
						p++;
					}
				}
				
				p = 0;
				
				for (int k = 0; k < 9; k++) {
					R[k] = pixel[k].getRed()*RED_FACTOR ;
					G[k] = pixel[k].getGreen()*GREEN_FACTOR;
					B[k] = pixel[k].getBlue()*BLUE_FACTOR;

				}
				
				
				for (int m=0; m<9; m++) {
					sumR +=  R[m];
					sumG +=  G[m];
					sumB +=  B[m];
				}
				
				r2 = (int)(sumR/R.length);
				g2 = (int)(sumG/G.length);
				b2 = (int)(sumB/B.length);
				
				sumR = 0;
				sumG = 0;
				sumB = 0;
									
				color = r2<<16 | g2 << 8 | b2;
				
				New.setRGB(i, j, color);

			}
		}
		
		setOriginal(New);
		imgIcon = new ImageIcon(New);
		return imgIcon;
		
	}

	
}