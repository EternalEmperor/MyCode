package cn.lzh.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import cn.lzh.bll.ReaderAdmin;
import cn.lzh.vo.Reader;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class BlankPanel extends JPanel {
	private JTextField tfID;
	private JButton Enter;
	private JButton Back;
	public static Reader reader = null; //登录用户信息
	private int loginTimes = 0; //登录次数
	private ReaderAdmin readerBLL = new ReaderAdmin();
	private JPasswordField pfPwd;
	private JLabel labelLoginInfo;	//登录信息提示
	
	public BlankPanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);
		initBlankPanel();
		
		
	}
	
	
	
	private void initBlankPanel(){
		JLabel label = new JLabel("欢迎来到长江大学图书管理系统");
		label.setBounds(409, 25, 517, 28);
		label.setFont(new Font("宋体", Font.BOLD, 25));
		add(label);
		
		JLabel label_1 = new JLabel("用户编号：");
		label_1.setBounds(442, 104, 101, 21);
		add(label_1);
		
		tfID = new JTextField();
		tfID.setBounds(642, 101, 96, 27);
		add(tfID);
		tfID.setColumns(10);
		
		JLabel label_2 = new JLabel("用户密码：");
		label_2.setBounds(442, 169, 101, 21);
		add(label_2);
		
		Enter = new JButton("登录");
		Enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				++loginTimes;
				String errorMsg = "";
				int rdID = -1;
				try{
					rdID = Integer.valueOf(tfID.getText().trim());
				}catch (NumberFormatException e1) {
					errorMsg = "用户名无效";
					tfID.requestFocus();
				}
				if(rdID != -1){
					reader = readerBLL.getReader(rdID);
					if(reader == null){
						errorMsg = "用户名无效";
						tfID.requestFocus();
					}else if(reader.getRdPwd().equals(
							new String(pfPwd.getPassword()).trim())){
						Main.initMenu();
						CardLayout cl = (CardLayout)Main.cards.getLayout();
						cl.show(Main.cards, Main.BkName);
						
					}else{
						errorMsg = "密码有误";
						pfPwd.requestFocus();
					}
				}
				if(errorMsg.length() > 0){
					JOptionPane.showMessageDialog(null, errorMsg);
				}
					
			}
		});
		Enter.setBounds(442, 231, 123, 29);
		add(Enter);
		
		Back = new JButton("取消");
		Back.setBounds(617, 231, 123, 29);
		add(Back);
		
		pfPwd = new JPasswordField();
		pfPwd.setBounds(642, 166, 96, 27);
		add(pfPwd);
	}
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("./猫1.jpg");  
        Image img = icon.getImage();  
        g.drawImage(img, 0, -350, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver()); 

	}
}
