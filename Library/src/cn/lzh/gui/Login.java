package cn.lzh.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import cn.lzh.bll.ReaderAdmin;
import cn.lzh.vo.Reader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Login extends JFrame {
	
	private int loginTimes = 0; //登录次数
	private ReaderAdmin readerBLL = new ReaderAdmin();
	
	private JTextField tfUserName;	//输入的用户名
	private JPasswordField pwdField; //输入的密码
	private JButton btnLogin;	//登录按钮
	private JButton btnClose;	//退出按钮
	private JLabel labelLoginInfo;	//登录信息提示
	public static Reader reader = null; //登录用户信息
	
	public Login() {
		initLoginPanel();
		this.setLocationRelativeTo(null);
	}
	
	private void addButtonClickEventHandlers(){
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				++loginTimes;
				String errorMsg = "";
				int rdID = -1;
				try{
					rdID = Integer.valueOf(tfUserName.getText().trim());
				}catch (NumberFormatException e) {
					errorMsg = "用户名无效";
					tfUserName.requestFocus();
				}
				if(rdID != -1){
					reader = readerBLL.getReader(rdID);
					if(reader == null){
						errorMsg = "用户名无效";
						tfUserName.requestFocus();
					}else if(reader.getRdPwd().equals(
							new String(pwdField.getPassword()).trim())){
						loadMainGUI();
					}else{
						errorMsg = "密码有误";
						pwdField.requestFocus();
					}
				}
				if(errorMsg.length() > 0)
					labelLoginInfo.setText(errorMsg);
				
			}
		});
		
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose(); //关闭当前窗体
			}
		});
		
	}
	
	private void loadMainGUI(){
		this.dispose();
		
		Main mainGUI = new Main();
		mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainGUI.setLocationRelativeTo(null);
		mainGUI.setVisible(true);
	}
		
	private void initLoginPanel(){
		setSize(new Dimension(548, 350));
		setResizable(false);
		setTitle("长江大学图书馆管理信息系统");
		getContentPane().setLayout(null);
		
		JLabel labelUserName = new JLabel("用户编号");
		labelUserName.setBounds(136, 84, 81, 34);
		getContentPane().add(labelUserName);
		
		JLabel labelPassword = new JLabel("用户密码");
		labelPassword.setBounds(136, 146, 81, 21);
		getContentPane().add(labelPassword);
		
		tfUserName = new JTextField();
		tfUserName.setBounds(246, 88, 122, 27);
		getContentPane().add(tfUserName);
		tfUserName.setColumns(10);
		
		pwdField = new JPasswordField();
		pwdField.setBounds(246, 143, 122, 27);
		getContentPane().add(pwdField);
		
		btnLogin = new JButton("登录");
		
		btnLogin.setBounds(118, 236, 123, 29);
		getContentPane().add(btnLogin);
		
		btnClose = new JButton("退出");
		
		btnClose.setBounds(333, 236, 123, 29);
		getContentPane().add(btnClose);
		
		labelLoginInfo = new JLabel("");
		labelLoginInfo.setBackground(Color.RED);
		labelLoginInfo.setBounds(235, 32, 133, 21);
		getContentPane().add(labelLoginInfo);
		
		addButtonClickEventHandlers();
	}
	
	
	
	public void start(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		Login login = new Login();
		login.start();
	}
	
	
}
