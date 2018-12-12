package cn.lzh.gui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class Main extends JFrame {
	
	private ReaderPanel readerPanel;
	private ReaderTypePanel readerTypePanel;
	private NewBookPanel newBookPanel;
	private BookSearchPanel bookSearchPanel;
	private BorrowBookPanel borrowBookPanel;
	private BlankPanel blankPanel;
	private AuthorityPanel authorityPanel;
	
	public final static String ReaderPanelName  = "Reader";
	public final static String ReaderTypePanelName = "ReaderType";
	public final static String NewBookPanelName = "NewBook";
	public final static String BookSearchName = "BookSearch";
	public final static String BorrowBookName = "BorrowBook";
	public final static String BlankPanelName = "Blank";
	public final static String AuthorityPanelName = "Ahthority";
	
	public static JPanel cards;
	private JMenu MN_ReaderMgt;
	private JMenuItem MI_ReaderMgt;
	private JMenuItem MI_ReaderTypeMgt;
	private JMenu MN_BookMgt;
	private JMenuItem MI_NewBook;
	private JMenuItem MI_BookSearch;
	private JMenu MN_BorrorMgt;
	private JMenuItem MI_Borror;
	private JMenu MN_UserMgt;
	private JMenuItem MI_PermissionMgt;
	private JMenuItem MI_UpdatePassword;
	private JMenuBar menuBar;
	
	
	public Main() {
		setSize(new Dimension(1215, 800));
		initComponents();
		initMenu();
		initCardPanels();
	}
	
	/**
	 * 菜单跳转
	 */
	private void initComponents(){
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		MN_ReaderMgt = new JMenu("读者管理");
		menuBar.add(MN_ReaderMgt);
		
		MI_ReaderMgt = new JMenuItem("借书证管理");
		MI_ReaderMgt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, ReaderPanelName);
			}
		});
		MN_ReaderMgt.add(MI_ReaderMgt);
		
		MI_ReaderTypeMgt = new JMenuItem("读者类别管理");
		MI_ReaderTypeMgt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, ReaderTypePanelName);
			}
		});
		MN_ReaderMgt.add(MI_ReaderTypeMgt);
		
		MN_BookMgt = new JMenu("图书管理");
		menuBar.add(MN_BookMgt);
		
		MI_NewBook = new JMenuItem("新书入库");
		MI_NewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, NewBookPanelName);
			}
		});
		MN_BookMgt.add(MI_NewBook);
		
		MI_BookSearch = new JMenuItem("图书查询");
		MI_BookSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, BookSearchName);
			}
		});
		MN_BookMgt.add(MI_BookSearch);
		
		MN_BorrorMgt = new JMenu("借阅管理");
		menuBar.add(MN_BorrorMgt);
		
		MI_Borror = new JMenuItem("借阅管理");
		MI_Borror.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, BorrowBookName);
			}
		});
		MN_BorrorMgt.add(MI_Borror);
		
		MN_UserMgt = new JMenu("用户管理");
		menuBar.add(MN_UserMgt);
		
		MI_PermissionMgt = new JMenuItem("权限管理");
		MI_PermissionMgt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, AuthorityPanelName);
			}
		});
		MN_UserMgt.add(MI_PermissionMgt);
		
		MI_UpdatePassword = new JMenuItem("修改密码");
		MI_UpdatePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)cards.getLayout();
				cl.show(cards, AuthorityPanelName);
			}
		});
		MN_UserMgt.add(MI_UpdatePassword);
	}
	/**
	 * 菜单项管理
	 */
	private void initMenu(){
		MN_ReaderMgt.setEnabled(Login.reader.isReaderAdmin());
		MN_BookMgt.setEnabled(Login.reader.isBookAdmin());
		MN_BorrorMgt.setEnabled(Login.reader.isBorrowAdmin());
		//MN_UserMgt.setEnabled(Login.reader.isSysAdmin());
		MN_UserMgt.getMenuComponent(0).setEnabled(Login.reader.isSysAdmin());
		MN_UserMgt.getMenuComponent(1).setEnabled(Login.reader.isBorrowAdmin());
	}
	
	/*
	 * 各个板块的初始化
	 */
	private void initCardPanels(){
		
		blankPanel = new BlankPanel();
		blankPanel.setVisible(true);
		
		readerPanel = new ReaderPanel();
		readerPanel.setVisible(false);
		
		readerTypePanel = new ReaderTypePanel();
		readerTypePanel.setVisible(false);
		
		newBookPanel = new NewBookPanel();
		newBookPanel.setVisible(false);
		
		bookSearchPanel = new BookSearchPanel();
		bookSearchPanel.setVisible(false);
		
		borrowBookPanel = new BorrowBookPanel();
		borrowBookPanel.setVisible(false);
		
		authorityPanel = new AuthorityPanel();
		authorityPanel.setVisible(false);
		
		cards = new JPanel(new CardLayout());
		cards.add(blankPanel,BlankPanelName);
		cards.add(readerPanel,ReaderPanelName);
		cards.add(readerTypePanel,ReaderTypePanelName);
		cards.add(newBookPanel,NewBookPanelName);
		cards.add(bookSearchPanel,BookSearchName);
		cards.add(borrowBookPanel,BorrowBookName);
		cards.add(authorityPanel,AuthorityPanelName);
		
		getContentPane().add(cards);
	}
	
	
	
	
}
