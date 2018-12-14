package cn.lzh.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import cn.lzh.bll.ReaderAdmin;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangePwdPanel extends JPanel {
	private JPasswordField tfoldPwd;
	private JPasswordField tfnewPwd;
	private JPanel ChangePanel;
	private JButton Enter;
	private JButton Back;
	private ReaderAdmin readerBll = new ReaderAdmin();
	
	public ChangePwdPanel() {
		setSize(new Dimension(1200, 500));
		setLayout(null);
		
		initChangePanel();
		addButtonClickEventHandlers();
		
	}
	
	private void addButtonClickEventHandlers(){
		Enter.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String oldpassword = String.valueOf(tfoldPwd.getPassword());
				String newpassword = String.valueOf(tfnewPwd.getPassword());
				if(BlankPanel.reader.getRdPwd().equals(oldpassword)){
					if(readerBll.updatePwd(BlankPanel.reader, newpassword) > 0){
						JOptionPane.showMessageDialog(null, "修改成功,请重新登录");
						CardLayout cl = (CardLayout)Main.cards.getLayout();
						cl.show(Main.cards, Main.BlankPanelName);
//						tfoldPwd.setText("");
//						tfnewPwd.setText("");
					}else{
						JOptionPane.showMessageDialog(null, "修改失败");
					}
				}else{
					JOptionPane.showMessageDialog(null, "密码输入错误");
				}
			}
		});
		
		Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfoldPwd.setText("");
				tfnewPwd.setText("");
			}
		});
		
		
	}
	
	private void initChangePanel(){
		ChangePanel = new JPanel();
		ChangePanel.setLayout(null);
		ChangePanel.setBorder(new TitledBorder(null, "\u5BC6\u7801\u4FEE\u6539", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ChangePanel.setBounds(15, 15, 522, 329);
		add(ChangePanel);
		
		JLabel label = new JLabel("原始密码：");
		label.setBounds(88, 92, 101, 21);
		ChangePanel.add(label);
		
		JLabel label_1 = new JLabel("新设密码：");
		label_1.setBounds(88, 187, 101, 21);
		ChangePanel.add(label_1);
		
		tfoldPwd = new JPasswordField();
		tfoldPwd.setBounds(258, 89, 110, 27);
		ChangePanel.add(tfoldPwd);
		
		tfnewPwd = new JPasswordField();
		tfnewPwd.setBounds(258, 184, 110, 27);
		ChangePanel.add(tfnewPwd);
		
		Enter = new JButton("确定");
		
		Enter.setBounds(86, 262, 123, 29);
		ChangePanel.add(Enter);
		
		Back = new JButton("取消");
		
		Back.setBounds(335, 262, 123, 29);
		ChangePanel.add(Back);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("./猫1.jpg");  
        Image img = icon.getImage();  
        g.drawImage(img, 0, -350, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver()); 

	}
}
