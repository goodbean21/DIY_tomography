package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import imageProcessing.Processing;

public class MonitorPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	Mainwindow myMainwindow;
	ImagePanel myImagePanel;
	
	protected JButton[] btn = new JButton[4];
	private String[] btnName = {
			"Aplicar", "Mediana", "Media ponderada",
			
	};
	
	protected JSlider Rcomp = new JSlider(JSlider.HORIZONTAL, 0, 150, 75);
	protected JSlider Gcomp = new JSlider(JSlider.HORIZONTAL, 0, 150, 75);
	protected JSlider Bcomp = new JSlider(JSlider.HORIZONTAL, 0, 150, 75);
	
	protected JSlider contraste = new JSlider(JSlider.VERTICAL, 0, 100, 50);
	
	protected JLabel[] operacion = new JLabel[5]; 
	private String[] lblName = {
			"CONTRASTE", "ESCALA DE GRISES", "BINARIZACION",
			"FILTROS","ESPACIO DE COLOR"
	};
	
	
	protected JLabel[] leyendas = new JLabel[3];
	private String[] lydName = {
			"Min.", "Max.", "Threshold"
	};
	
	protected JLabel[] RGB = new JLabel[3];
	private String[] rgbName = {
			"Rojo", "Verde", "Azul"
	};
	
	
	protected JFormattedTextField[] limites = new JFormattedTextField[3];
	protected String[] defaultLimites = {
			"0", "255", "0.5"
	};
	
	private ImageIcon imIcon;
	
	public void setMyMainwindow(Mainwindow myMainwindow) {
		this.myMainwindow = myMainwindow;
		
	}

	public void setMyImagePanel(ImagePanel myImagePanel) {
		this.myImagePanel = myImagePanel;
		
	}

	public MonitorPanel() {
		btn[0] = new JButton(btnName[0]);
		btn[0].addActionListener(this);
		
		for(int i = 1; i < btn.length; i++) {
			btn[i] = new JButton(btnName[i-1]);
			btn[i].addActionListener(this);
			
		}
		
		for(int i = 0; i < operacion.length; i++) {
			operacion[i] = new JLabel(lblName[i]);
			
		}
		

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		
		formatter.setValueClass(Integer.class);
		formatter.setMaximum(255);
		formatter.setMinimum(0);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(true);
		
		for(int i = 0; i < leyendas.length-1; i++) {
			leyendas[i] = new JLabel(lydName[i]);
			limites[i] = new JFormattedTextField(formatter);
			limites[i].setText(defaultLimites[i]);
			
		}
		
		for(int i = 0; i < leyendas.length; i++) {
			RGB[i] = new JLabel(rgbName[i]);
			RGB[i].setAlignmentX(RIGHT_ALIGNMENT);
		}
		
		NumberFormat format1 = NumberFormat.getInstance();
		NumberFormatter formatter1 = new NumberFormatter(format1);
		
		formatter1.setValueClass(Double.class);
		formatter1.setMaximum(1.00);
		formatter1.setMinimum(0.00);
		
		leyendas[2] = new JLabel(lydName[2]);
		limites[2] = new JFormattedTextField(formatter1);
		limites[2].setText(defaultLimites[2]);
		
		contraste.setMinorTickSpacing(1);
		contraste.setMajorTickSpacing(10);
		contraste.setPaintTicks(true);
		contraste.setPaintLabels(true);
		
		Rcomp.setMinorTickSpacing(5);
		Rcomp.setMajorTickSpacing(10);
		Rcomp.setPaintTicks(true);
		
		Gcomp.setMinorTickSpacing(5);
		Gcomp.setMajorTickSpacing(10);
		Gcomp.setPaintTicks(true);
		
		Bcomp.setMinorTickSpacing(5);
		Bcomp.setMajorTickSpacing(10);
		Bcomp.setPaintTicks(true);
		
		GridBagLayout gBl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		// Setup buttons
		c.insets = new Insets(0, 10, 0, 10);
		
		setLayout(gBl);
		
		c.fill = GridBagConstraints.VERTICAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0.1;
		c.weighty = 0.1;
		
		gBl.setConstraints(operacion[0], c);
		add(operacion[0]);
		
		c.gridy = 1;
		c.gridheight = 6;
		
		gBl.setConstraints(contraste, c);
		add(contraste);
		
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		
		gBl.setConstraints(operacion[1], c);
		add(operacion[1]);
		
		c.gridwidth = 1;
		c.gridy = 1;
		
		for(int i = 0; i < 2; i++) {
			c.gridx = i+2;
			gBl.setConstraints(leyendas[i], c);
			add(leyendas[i]);
			
		} 
		
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		for(int i = 0; i < 2; i++) {
			c.gridx = i+2;
			gBl.setConstraints(limites[i], c);
			add(limites[i]);
			
		} 
		
		c.gridy = 3;
		c.gridx = 2;
		c.gridwidth = 2;
		gBl.setConstraints(btn[0], c);
		add(btn[0]);
		
		c.gridy = 4;
		c.gridwidth = 2;
		gBl.setConstraints(operacion[2], c);
		add(operacion[2]);
		
		c.gridy = 5;
		c.gridwidth = 1;
		gBl.setConstraints(leyendas[2], c);
		add(leyendas[2]);
		
		c.gridy = 6;
		gBl.setConstraints(limites[2], c);
		add(limites[2]);
		
		c.gridx = 3;
		gBl.setConstraints(btn[1], c);
		add(btn[1]);
		
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 4;
		gBl.setConstraints(operacion[3], c);
		add(operacion[3]);

		c.gridy = 8;
		c.gridwidth = 1;
		gBl.setConstraints(btn[2], c);
		add(btn[2]);
		
		c.gridx=1;
		c.gridwidth = 3;
		gBl.setConstraints(btn[3], c);
		add(btn[3]);
		
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 4;
		gBl.setConstraints(operacion[4], c);
		add(operacion[4]);
		
		JPanel pan = new JPanel();
		GridLayout g = new GridLayout(3, 3);
		pan.setLayout(g);
		
		pan.add(RGB[0]);
		pan.add(Rcomp);
		
		pan.add(RGB[1]);
		pan.add(Gcomp);

		pan.add(RGB[2]);
		pan.add(Bcomp);
		
		c.gridy = 10;
		c.gridwidth = 4;
		c.gridheight = 1;
		gBl.setConstraints(pan, c);
		add(pan);		
		
		// Ends setting up buttons

		contraste.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {			
				double val = contraste.getValue();
				double c = (2.0*val)/100;
				
				if(myImagePanel.getImage() != null) {
					ImageIcon imIcon = Processing.contraste(c);
					
					myImagePanel.setImage(imIcon);
				}
			}
		});
		
		Rcomp.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				modifySpaceColor();
				
			}
		});
		
		Gcomp.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {				
				modifySpaceColor();
				
			}
		});
		
		Bcomp.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {				
				modifySpaceColor();
				
			}
		});

		
	}
	
	public void modifySpaceColor () {
		double valR = Rcomp.getValue();
		double valG = Gcomp.getValue();
		double valB = Bcomp.getValue();
		
		Image image1 = myImagePanel.getImage();
		
		if(image1 !=  null) {
		    BufferedImage newImage = new BufferedImage(
		    		image1.getWidth(null), image1.getHeight(null),
		    		BufferedImage.TYPE_INT_RGB);
		    
		    Graphics2D g = newImage.createGraphics();
		    g.drawImage(image1, 0, 0, null);
		    g.dispose();
		    
			imIcon = Processing.makeFalse(newImage, valR, valG, valB);
			
			myImagePanel.setImage(imIcon);
			
			Processing.setOriginal(newImage);
		
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object s = e.getSource();
				
		if(s == btn[0]) {
			String Min = limites[0].getText();
			String Max = limites[1].getText();
			
			Image image = myImagePanel.getImage();
			
			if(image != null) {
				BufferedImage im = ImagePanel.convertToBufferedImage(image);
				
				int min = Integer.parseInt(Min);
				int max = Integer.parseInt(Max);
				
				imIcon = Processing.Grises(im, min, max);
				
				myImagePanel.setImage(imIcon);
			}
			
		}else if(s == btn[1]){
			String thresh = limites[2].getText();
			
			if(!thresh.isEmpty()) {
				double Thresh = Double.parseDouble(thresh);
				
				Image image1 = myImagePanel.getImage();
				
				if(image1 != null) {
					BufferedImage im1 = ImagePanel.convertToBufferedImage(image1);
					
					imIcon = Processing.Binarizar(im1, Thresh);
					
					myImagePanel.setImage(imIcon);
				}
			}
			
		}else if(s == btn[2]) {
			Image image1 = myImagePanel.getImage();
			
				if(image1 != null) {
				
				BufferedImage im1 = ImagePanel.convertToBufferedImage(image1);
				
				imIcon = Processing.mediana(im1);
				
				myImagePanel.setImage(imIcon);
				
			}
			
		}else if(s == btn[3]) {
			
			Image image1 = myImagePanel.getImage();
			
			if(image1 != null) {
				BufferedImage im1 = ImagePanel.convertToBufferedImage(image1);
				
				imIcon = Processing.mediaPonderada(im1);
				
				myImagePanel.setImage(imIcon);
			}
			
		}
		
	}

}