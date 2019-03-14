package gui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class LabelPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	protected JLabel bottomLabel;
	final private GridLayout g;
	public String text;
	
	public String getText() {
		return text;
	
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public LabelPanel(String text) {
		bottomLabel = new JLabel(text);
		bottomLabel.setFont(new Font("Times New Roman", 24, 20));
		
		g = new GridLayout(1, 1);
		setLayout(g);
		add(bottomLabel);
		
	}

}
