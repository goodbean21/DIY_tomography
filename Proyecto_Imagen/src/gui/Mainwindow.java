package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mathworks.engine.MatlabEngine;

import imageProcessing.Processing;

public class Mainwindow extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
	
	private Thread matlabThread;
	private JMenuBar bar;
	private JMenu file, imOp, contrastOp;
	protected JMenuItem save, load, rst, neg, lin, log, gam, matlab, eraseLast, im3D;
	
	protected BufferedImage img;
	protected ImagePanel imPan;
	protected ImageIcon imgIcon;
	protected LabelPanel sthLab;

	private int width, height, red, green, blue;
	private double t;
	private long startTime, endTime, totalTime;
	
	public BufferedImage getImg() {
		return img;
		
	}
	
	private void generateThread() {
		
		matlabThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int p = 0;
				startTime = System.nanoTime();
				
				try {
					
					MatlabEngine matEng = MatlabEngine.startMatlab();
				
//					matEng.eval("file = 'C:\\Users\\Usuario\\Desktop\\8vo Semestre\\Procesamiento de Imagen\\Proyecto_Parcial';",null,null);
//					matEng.eval("addpath(file);", null , null);
//					matEng.eval("[I, t] = Celulitas_brillantes2;", null, null);
					
					matEng.eval("file = '"+ PROJECT_DIRECTORY + "\\matlab_codes';",null,null);
					matEng.eval("addpath(file);", null , null);
					matEng.eval("Echo_Arduino();", null, null);
					
					matEng.eval("[m, n, z] = size(IR);");
					matEng.eval("[m1, n2, ~] = size(vol);");
					
					double Slices = matEng.getVariable("z");
					double[][][] I = matEng.getVariable("IR");
					double[][][] I2 = matEng.getVariable("vol");
					
		        	double m = matEng.getVariable("m");
					double n = matEng.getVariable("n");
					
		        	double m1 = matEng.getVariable("m1");
					double n1 = matEng.getVariable("n2");
					
					int k = 0;
					int p1 = 0;
					
					for(int z = 0; z < Slices * 2 ; z++) {	
						
				        if(z%2 == 0 || z == 0) {
							
					        img = new BufferedImage((int) m, (int) n, BufferedImage.TYPE_BYTE_GRAY);
					        
					        for(int i = 0;i < m; i++) {
					        	 for (int j = 0; j < n; j++) {
					        		 if(I[i][j][k] >= 255) I[i][j][k] = 255;
					        		 if(I[i][j][k] <= 0) I[i][j][k] = 0;
					        			 
					                 Color c = new Color((int) (I[i][j][k]*0.299),(int) (I[i][j][k]* 0.587),(int) (I[i][j][k]* 0.114));
					                 img.setRGB(i, j, c.getRGB());
					                 
					             }
					        	
					        }
					        k = k + 1;
					        
				        } else {
							
					        img = new BufferedImage((int) m1, (int) n1, BufferedImage.TYPE_INT_RGB);
					        
				        	for(int i = 0;i < m1; i++) {
					        	 for (int j = 0; j < n1; j++) {
					        		 if(I2[i][j][p1] >= 255) I2[i][j][p1] = 255;
					        		 if(I2[i][j][p1] <= 0) I2[i][j][p1] = 0;
					        			 
					                 Color c = new Color((int) (I2[i][j][p1]),(int) (I2[i][j][p1]),(int) (I2[i][j][p1]));
					                 img.setRGB(i, j, c.getRGB());
					                 
					             }
					        	
					        }
				        	
				        	p1 = p1 + 1;
				        }
				        
						ImagePanel.addList(img);
					
					}
					
					if(imPan.getImage() == null) {
				        Processing.setOriginal(img);
				        
						imgIcon = new ImageIcon(img);		
						
				        imPan.setImage(imgIcon);
					}
					
					matEng.close();
					
					endTime = System.nanoTime();
					
					totalTime = (endTime - startTime);
					totalTime = TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS);
					
					if(totalTime >= 60) totalTime = totalTime/60;
					
					sthLab.bottomLabel.setText("Tiempo total: " + Long.toString(totalTime));
	
					
				} catch (IllegalArgumentException | IllegalStateException | InterruptedException | ExecutionException e) {
					e.printStackTrace();
				
				}
				
			}
		});
		
	}

	public Mainwindow() {
		super("Proyecto de Imagenología médica");
		
		this.generateThread();
		
		bar = new JMenuBar();
		file = new JMenu("Archivo...");
		imOp = new JMenu("Imagen...");
		contrastOp = new JMenu("Tipo de contraste...");
		
		rst = new JMenuItem("Reset");
		neg = new JMenuItem("Negativo");
		eraseLast = new JMenuItem("Remover última imagen");
		im3D = new JMenuItem("Reconstrucción Volumétrica");
		
		lin = new JMenuItem("Lineal");
		log = new JMenuItem("Logarítmico");
		gam = new JMenuItem("Correción gamma");
		
		save = new JMenuItem("Guardar...");
		load = new JMenuItem("Cargar...");
		matlab = new JMenuItem("Correr Matlab...");
		
		save.addActionListener(this);
		load.addActionListener(this);
		matlab.addActionListener(this);
		
		lin.addActionListener(this);
		log.addActionListener(this);
		gam.addActionListener(this);
		rst.addActionListener(this);
		neg.addActionListener(this);
		eraseLast.addActionListener(this);
		im3D.addActionListener(this);

		file.add(load);
		file.add(save);
		file.add(matlab);
		
		imOp.add(rst);
		imOp.add(neg);
		imOp.add(contrastOp);
		imOp.add(eraseLast);
		imOp.add(im3D);
		
		contrastOp.add(lin);
		contrastOp.add(log);
		contrastOp.add(gam);
		
		bar.add(file);
		bar.add(imOp);
		
		setJMenuBar(bar);
				
		imPan = new ImagePanel();
		sthLab = new LabelPanel("BIENVENIDO");
		MonitorPanel monitor = new MonitorPanel();
		
		monitor.setMyImagePanel(imPan);
		monitor.setMyMainwindow(this);
		
		imPan.setMysouth(sthLab);
		
		BorderLayout b = new BorderLayout();
		setLayout(b);
		
		monitor.setPreferredSize(new Dimension(300, 800));

		add(imPan, BorderLayout.CENTER);
		add(sthLab, BorderLayout.SOUTH);
		add(monitor, BorderLayout.EAST);
		
		setSize(1000,800);
		setLocation(500, 200);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		switch (s) {
			case "Guardar...":
				if(img != null) {
					
					BufferedImage Img = ImagePanel.convertToBufferedImage(imPan.getImage());
					
					JFileChooser fc = new JFileChooser("C:\\Users\\Usuario\\Desktop\\");
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.setFileFilter(new FileNameExtensionFilter("Imagen(*.jpg, *.png, *.gif, *.bmp)", "jpg", "png", "gif", "bmp"));
					
					fc.setAcceptAllFileFilterUsed(false);
					
					int returnVal = fc.showSaveDialog(this);
					
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						String file = fc.getSelectedFile().getName();
						
						if(!file.endsWith(".jpg") && !file.endsWith(".png") && 
								!file.endsWith(".gif") && !file.endsWith(".bmp") ) file = file + ".jpg";
						
						int ind = file.indexOf(".");
						String ext = file.substring(ind+1);
						
						try {
							ImageIO.write(Img, ext, new File(fc.getSelectedFile().getPath()+"."+ext));
							
						} catch (IOException e1) {
							e1.printStackTrace();
							
						}
					}
				}
				
				break;
	
			case "Cargar...":
				
				JFileChooser fc1 = new JFileChooser("C:\\Users\\Usuario\\Desktop\\");
				fc1.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc1.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
				fc1.setAcceptAllFileFilterUsed(false);
				
				int returnVal1 = fc1.showOpenDialog(this);		
				
				if(returnVal1 == JFileChooser.APPROVE_OPTION) {
					try {
						img = ImageIO.read(fc1.getSelectedFile());
						
						width = img.getWidth();
						height = img.getHeight();
 
						Processing.setOriginal(img);
						
						for(int i=0; i<height; i++){
							for(int j=0; j<width; j++) {
								Color c = new Color(img.getRGB(j, i));
								red = (int)(c.getRed() * 0.299);
								green = (int)(c.getGreen() * 0.587);
								blue = (int)(c.getBlue() * 0.114);
								Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
								img.setRGB(j,i,newColor.getRGB());
								
							}
						}
						
						ImagePanel.addList(img);
						
						imgIcon = new ImageIcon(img);						
						
						imPan.setImage(imgIcon);
						
					} catch (IOException e1) {
						e1.printStackTrace();
						
					}
				
				}
				
				break;
				
			case "Reset":
				if(imPan.getImage() != null) {
					BufferedImage I = ImagePanel.getCurrent();
					Processing.setOriginal(I);
					
					height = I.getHeight();
					width = I.getWidth();
					
					for(int i=0; i<height; i++){
						for(int j=0; j<width; j++) {
							Color c = new Color(I.getRGB(j, i));
							red = (int)(c.getRed() * 0.299);
							green = (int)(c.getGreen() * 0.587);
							blue = (int)(c.getBlue() *0.114);
							Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
							I.setRGB(j,i,newColor.getRGB());
							
						}
					}
					
					imgIcon = new ImageIcon(I);						
					
					imPan.setImage(imgIcon);
				}
				
				break;
				
			case "Negativo":
				Image Im = imPan.getImage();
				if(Im != null) {
					BufferedImage im = ImagePanel.convertToBufferedImage(Im);
					imgIcon = Processing.negativo(im);
					imPan.setImage(imgIcon);
				}
				
				break;
				
			case "Lineal":
				Processing.setType(s);
				
				break;
				
			case "Logarítmico":
				Processing.setType(s);
				
				break;
				
			case "Correción gamma":
				Processing.setType(s);
				
				break;
				
			case "Correr Matlab...":

				matlabThread.start();
				this.generateThread();
				
				break;
				
			case "Remover última imagen":
				ImagePanel.removefromList();
				
				break;
				
			case "Reconstrucción Volumétrica":
				
			try {

				String path = PROJECT_DIRECTORY + "\\matlab_codes\\3d.jpg";
				
				try {
					File file = new File(path);
					img = ImageIO.read(file);
					
					width = img.getWidth();
					height = img.getHeight();
					
					for(int i=0; i<height; i++){
						for(int j=0; j<width; j++) {
							Color c = new Color(img.getRGB(j, i));
							red = (int)(c.getRed() * 0.299);
							green = (int)(c.getGreen() * 0.587);
							blue = (int)(c.getBlue() * 0.114);
							Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
							img.setRGB(j,i,newColor.getRGB());
							
						}
					}
									
					imgIcon = new ImageIcon(img);						
					
					imPan.setImage(imgIcon);
					
					
				} catch (IOException e1) {
					e1.printStackTrace();
	
				}
				
			}catch (Exception e2) {

			}
				break;
				
		}
		
		
	}
		
}