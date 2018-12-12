package cn.lzh.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import cn.lzh.bll.BookAdmin;
import cn.lzh.dal.BookDAL;
import cn.lzh.vo.Book;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class NewBookPanel extends JPanel {
	
	
	private JTextField tfbkID;
	private JTextField tfbkName;
	private JTextField tfbkAuthor;
	private JTextField tfbkPress;
	private JTextField tfbkDatePress;
	private JTextField tfbkPrice;
	private JTextField tfbkDateIn;
	private JTextField tfbkBrief;
	private JPanel BaseInfoPanel;
	private JPanel IntroducePanel;
	private JPanel PhotoPanel;
	private JPanel NewCtrlPanel;
	private JButton add;
	private JButton Enter;
	private JButton Back;
	private JButton addCover;
	private JLabel bkCover;
	private JButton clear;
	
	private BookAdmin bookBll = new BookAdmin();
	
	private enum OpStatus{
		inSelect,inNew,inChange
	}
	
	private OpStatus ops = null;
	private JComboBox cbbkStatus;
	
	public NewBookPanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);	
		
		initBaseInfoPanel();
		initIntroducePanel();
		initPhotoPanel();
		initNewCtrlPanel();
		addNewButtonClickEventHandlers();
		
		setStatus(ops.inSelect);
		
	}
	
	public NewBookPanel(Book book){
		this();
		setBookToText(book);
		setStatus(ops.inChange);
	}
	
	private void setComponentStatusInPanel(JPanel panel,boolean status){

		for(Component comp : panel.getComponents()){
			comp.setEnabled(status);
		}
	}
	
	private void setStatus(OpStatus opst){
		ops = opst;
		switch(ops){
		case inSelect:
			setComponentStatusInPanel(BaseInfoPanel,false);
			setComponentStatusInPanel(IntroducePanel,false);
			setComponentStatusInPanel(PhotoPanel,false);
			add.setEnabled(true);
			clear.setEnabled(false);
			Enter.setEnabled(false);
			Back.setEnabled(false);
			addCover.setEnabled(false);
			break;
		case inNew:
			setComponentStatusInPanel(BaseInfoPanel,true);
			setComponentStatusInPanel(IntroducePanel,true);
			setComponentStatusInPanel(PhotoPanel,true);
			add.setEnabled(false);
			clear.setEnabled(true);
			Enter.setEnabled(true);
			Back.setEnabled(false);
			addCover.setEnabled(true);
			break;
		case inChange:
			setComponentStatusInPanel(BaseInfoPanel,true);
			tfbkID.setEnabled(false);
			tfbkDateIn.setEnabled(false);
			setComponentStatusInPanel(IntroducePanel,true);
			setComponentStatusInPanel(PhotoPanel,true);
			clear.setEnabled(false);
			add.setEnabled(false);
			Enter.setEnabled(true);
			Back.setEnabled(true);
			addCover.setEnabled(true);
			break;
		}
		
		
	}
	
	
	
	
	
	private void setBookToText(Book book){
		tfbkID.setText(String.valueOf(book.getBkID()));
		tfbkName.setText(book.getBkName());
		tfbkAuthor.setText(book.getBkAuthor());
		tfbkPress.setText(book.getBkPress());
		tfbkDatePress.setText(String.valueOf(book.getBkDatePress()));
		tfbkPrice.setText(String.valueOf(book.getBkPrice()));
		tfbkDateIn.setText(String.valueOf(book.getBkDateIn()));
//		tfbkStatus.setText(book.getBkStatus());
		cbbkStatus.setSelectedItem(book.getBkStatus());
		tfbkBrief.setText(book.getBkBrief());
		if(book.getBkCover() != null){
			Image image = null;
			try{
				InputStream sbs = new ByteArrayInputStream(book.getBkCover());
				image = ImageIO.read(sbs);
			}catch (IOException e) {
				e.printStackTrace();
			}
			bkCover.setIcon(new ImageIcon(image));
			
		}
	}
	
	private  Date strToDate(String strDate) {  
        String str = strDate;  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        java.util.Date d = null;  
        try {  
            d = format.parse(str);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        java.sql.Date date = new java.sql.Date(d.getTime());  
        return date;  
    } 
	
	private byte[] toByteArray(InputStream input) throws IOException {
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    byte[] buffer = new byte[4096];
	    int n = 0;
	    while (-1 != (n = input.read(buffer))) {
	        output.write(buffer, 0, n);
	    }
	    return output.toByteArray();
	}
	
	private Book getBookFromText() throws IOException{
		Book book = new Book();
		book.setBkID(Integer.valueOf(tfbkID.getText()));
		book.setBkName(tfbkName.getText().trim());
		book.setBkAuthor(tfbkAuthor.getText().trim());
		book.setBkPress(tfbkPress.getText().trim());
		book.setBkDatePress(strToDate(tfbkDatePress.getText().trim()));
		book.setBkPrice(Float.valueOf(tfbkPrice.getText().trim()));
		book.setBkDateIn(strToDate(tfbkDateIn.getText().trim()));
		book.setBkBrief(tfbkBrief.getText().trim());
		book.setBkStatus((String)cbbkStatus.getSelectedItem());
		if(bkCover.getIcon() != null){
			Image image = ((ImageIcon)bkCover.getIcon()).getImage();
			BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bi.createGraphics();
			g2.drawImage(image, 0, 0, null);
			g2.dispose();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try{
				ImageIO.write(bi, "png", os);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			InputStream fis = new ByteArrayInputStream(os.toByteArray());
			book.setBkCover(toByteArray(fis));
		}
		return book;
	}
	
	
	private void initIntroducePanel(){
		IntroducePanel = new JPanel();
		IntroducePanel.setBorder(new TitledBorder(null, "\u5185\u5BB9\u7B80\u4ECB", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		IntroducePanel.setBounds(441, 36, 429, 585);
		add(IntroducePanel);
		IntroducePanel.setLayout(null);
		
		tfbkBrief = new JTextField();
		tfbkBrief.setBounds(15, 32, 399, 538);
		IntroducePanel.add(tfbkBrief);
		tfbkBrief.setColumns(10);
	}
	private void initBaseInfoPanel(){
		BaseInfoPanel = new JPanel();
		BaseInfoPanel.setBorder(new TitledBorder(null, "\u57FA\u672C\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		BaseInfoPanel.setBounds(15, 36, 411, 585);
		add(BaseInfoPanel);
		BaseInfoPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("图书序号：");
		lblNewLabel.setBounds(34, 37, 109, 21);
		BaseInfoPanel.add(lblNewLabel);
		
		JLabel label = new JLabel("书名：");
		label.setBounds(34, 100, 81, 21);
		BaseInfoPanel.add(label);
		
		JLabel label_1 = new JLabel("作者：");
		label_1.setBounds(34, 168, 81, 21);
		BaseInfoPanel.add(label_1);
		
		JLabel label_2 = new JLabel("出版社：");
		label_2.setBounds(34, 230, 81, 21);
		BaseInfoPanel.add(label_2);
		
		JLabel label_3 = new JLabel("出版日期：");
		label_3.setBounds(34, 301, 109, 21);
		BaseInfoPanel.add(label_3);
		
		JLabel label_4 = new JLabel("价格：");
		label_4.setBounds(34, 370, 81, 21);
		BaseInfoPanel.add(label_4);
		
		JLabel label_5 = new JLabel("入馆日期:");
		label_5.setBounds(34, 444, 81, 21);
		BaseInfoPanel.add(label_5);
		
		JLabel label_6 = new JLabel("图书状态：");
		label_6.setBounds(34, 513, 109, 21);
		BaseInfoPanel.add(label_6);
		
		tfbkID = new JTextField();
		tfbkID.setBounds(144, 34, 114, 27);
		tfbkID.setDocument(new NumberJudge());
		BaseInfoPanel.add(tfbkID);
		tfbkID.setColumns(10);
		
		tfbkName = new JTextField();
		tfbkName.setBounds(144, 97, 114, 27);
		BaseInfoPanel.add(tfbkName);
		tfbkName.setColumns(10);
		
		tfbkAuthor = new JTextField();
		tfbkAuthor.setBounds(144, 165, 114, 27);
		BaseInfoPanel.add(tfbkAuthor);
		tfbkAuthor.setColumns(10);
		
		tfbkPress = new JTextField();
		tfbkPress.setBounds(144, 227, 114, 27);
		BaseInfoPanel.add(tfbkPress);
		tfbkPress.setColumns(10);
		
		tfbkDatePress = new JTextField();
		tfbkDatePress.setBounds(144, 298, 114, 27);
		BaseInfoPanel.add(tfbkDatePress);
		tfbkDatePress.setColumns(10);
		
		tfbkPrice = new JTextField();
		tfbkPrice.setBounds(144, 367, 114, 27);
		tfbkPrice.setDocument(new NumberJudge());
		BaseInfoPanel.add(tfbkPrice);
		tfbkPrice.setColumns(10);
		
		tfbkDateIn = new JTextField();
		tfbkDateIn.setText("");
		tfbkDateIn.setBounds(144, 441, 114, 27);
		BaseInfoPanel.add(tfbkDateIn);
		tfbkDateIn.setColumns(10);
		
		cbbkStatus = new JComboBox();
		cbbkStatus.setModel(new DefaultComboBoxModel(new String[] {"在馆", "出借", "遗失", "变卖", "销毁"}));
		cbbkStatus.setEditable(true);
		cbbkStatus.setBounds(144, 510, 114, 27);
		BaseInfoPanel.add(cbbkStatus);
	}
	private void initPhotoPanel(){
		PhotoPanel = new JPanel();
		PhotoPanel.setBorder(new TitledBorder(null, "\u5C01\u9762", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		PhotoPanel.setBounds(885, 117, 300, 423);
		add(PhotoPanel);
		PhotoPanel.setLayout(null);
		
		bkCover = new JLabel();
		bkCover.setBounds(15, 27, 270, 381);
		PhotoPanel.add(bkCover);
	}
	private void initNewCtrlPanel(){
		NewCtrlPanel = new JPanel();
		NewCtrlPanel.setBounds(15, 624, 1170, 111);
		add(NewCtrlPanel);
		NewCtrlPanel.setLayout(null);
		
		add = new JButton("添加");
		
		add.setBounds(448, 27, 123, 29);
		NewCtrlPanel.add(add);
		
		Enter = new JButton("确定");
		
		Enter.setBounds(586, 27, 123, 29);
		NewCtrlPanel.add(Enter);
		
		Back = new JButton("返回");
		
		Back.setBounds(722, 27, 123, 29);
		NewCtrlPanel.add(Back);
		
		addCover = new JButton("上次照片");
		
		addCover.setBounds(978, 27, 123, 29);
		NewCtrlPanel.add(addCover);
		
		clear = new JButton("清空");
		
		clear.setBounds(106, 27, 123, 29);
		NewCtrlPanel.add(clear);
	}

	private void addNewButtonClickEventHandlers(){
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStatus(ops.inNew);
				clear();
			}
		});
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		
		Enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ops == OpStatus.inNew){
						try {
							if(!isjudgeTextHaveNull()){
								if(bookBll.addBook(getBookFromText()) > 0){
									JOptionPane.showMessageDialog(null, "添加成功");
									clear();
									setStatus(ops.inSelect);
								}
							}else{
								JOptionPane.showMessageDialog(null, "存在未输入的文本框");
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				}else if(ops == OpStatus.inChange){
					try {
						if(bookBll.updateBook(getBookFromText()) > 0){
							JOptionPane.showMessageDialog(null, "修改成功");
						}else{
							JOptionPane.showMessageDialog(null, "修改失败");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					CardLayout cl = (CardLayout)Main.cards.getLayout();
					cl.show(Main.cards, Main.BookSearchName);
					
				}
			}
		});
		
		Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)Main.cards.getLayout();
				cl.show(Main.cards, Main.BookSearchName);
			}
		});
		
		addCover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new ImageFilter());
				int returnVal = fc.showSaveDialog(NewBookPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					try{
						BufferedImage image = ImageIO.read(file);
						
						Image dImage  = image.getScaledInstance(bkCover.getWidth(),bkCover.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(dImage);
						bkCover.setIcon(icon);
					}catch (Exception ex) {
						// TODO: handle exception
						ex.printStackTrace();
					}
				}
			}
		});
		
		
		
	}
	
	public boolean isjudgeTextHaveNull(){
		if(tfbkID.getText().trim().equals("") || 
		   tfbkName.getText().trim().equals("") ||
		   tfbkAuthor.getText().trim().equals("") ||
		   tfbkPress.getText().trim().equals("") ||
		   tfbkDatePress.getText().trim().equals("") ||
		   tfbkPrice.getText().trim().equals("") ||
		   tfbkDateIn.getText().trim().equals("")){
			return true;
		}
		return false;
	}
	
	private void clear(){
		tfbkID.setText("");
		tfbkName.setText("");
		tfbkAuthor.setText("");
		tfbkPress.setText("");
		tfbkDatePress.setText("");
		tfbkPrice.setText("");
		java.util.Date day = new java.util.Date();
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		tfbkDateIn.setText(dFormat.format(day));
		tfbkBrief.setText("");
		bkCover.setIcon(null);
	}
}
