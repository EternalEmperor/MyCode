package cn.lzh.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;

public class BlankPanel extends JPanel {
	public BlankPanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);	
		
		JLabel label = new JLabel("欢迎来到长江大学图书管理系统");
		label.setFont(new Font("宋体", Font.BOLD, 25));
		label.setBounds(44, 57, 517, 28);
		add(label);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("./猫1.jpg");  
        Image img = icon.getImage();  
        g.drawImage(img, 0, -350, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver()); 

	}
}
