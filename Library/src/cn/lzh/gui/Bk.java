package cn.lzh.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Bk extends JPanel {
	public Bk() {
		setSize(new Dimension(1215, 800));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("./猫1.jpg");  
        Image img = icon.getImage();  
        g.drawImage(img, 0, -350, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver()); 

	}
}
