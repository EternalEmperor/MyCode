package cn.lzh.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import cn.lzh.bll.BookAdmin;
import cn.lzh.vo.Book;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BookSearchPanel extends JPanel {
	private JTabbedPane tabbedPane;
	private JTextField SearchContent;
	private JTextField tfBookName;
	private JTextField tfBookAuthor;
	private JTextField tfBookPress;
	private JPanel SimpleSearchPanel;
	private JComboBox SearchKey;
	private JButton SearchBtn;
	private JPanel SupSearchPanel;
	private JButton SupSearchBtn;
	private JPanel CtrlPanel;
	public static JButton Update;
	private JButton Delete;
	private JButton ToExcel;
	private JButton Retrun;
	private NewBookPanel updateBookPanel;
	private BookAdmin bookBll = new BookAdmin();
	public static Map<String,String> BookKey;
	private JScrollPane ResultPanel;
	private JTable searchResultTable;
	private Book[] bks = null;
	private final String  UpdateBook= "UpdateBook";
	public BookSearchPanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);
		
		initMapSearch();
		initTabbedPane();
		initSimpleSearchPanel();
		initSupSearchPanel();
		initResultPanel();
		initCtrlPanel();
		addButtonClickEventHandlers();
		
		
		
	}
	
	private void updateResultTable(Book[] books){
		if(books == null){
			JOptionPane.showMessageDialog(null, "没有符合要求的记录！");
			return;
		}
		CustomizedTableModel<Book> tableModel = (CustomizedTableModel<Book>) searchResultTable.getModel();
		tableModel.setRecords(books);
		//更新表单
		tableModel.fireTableDataChanged();
	
	}
	
	private void addButtonClickEventHandlers(){
		SearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String value = BookKey.get(SearchKey.getSelectedItem());
				String content = SearchContent.getText().trim();
				try{
					updateResultTable(bookBll.getBooksBy(value, content));
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		SupSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bkName = tfBookName.getText().trim();
				String bkAuthor = tfBookAuthor.getText().trim();
				String bkPress = tfBookPress.getText().trim();
				try{
					updateResultTable(bookBll.getBookBy(bkName, bkAuthor, bkPress));
				}catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}
				Book book = ((CustomizedTableModel<Book>) searchResultTable.getModel()).getObjectAt(selectedRow);
				if(bookBll.deleteBook(book) > 0){
					JOptionPane.showMessageDialog(null, "删除成功");
					updateResultTable(bookBll.getBooks());;
				}else{
					JOptionPane.showMessageDialog(null, "删除失败");
				}
			}
		});
		
		Update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}
				Book book = ((CustomizedTableModel<Book>) searchResultTable.getModel()).getObjectAt(selectedRow);
			
				updateBookPanel = new NewBookPanel(book);
				updateBookPanel.setVisible(false);
				Main.cards.add(updateBookPanel,UpdateBook);
				CardLayout cl = (CardLayout)Main.cards.getLayout();
				cl.show(Main.cards, UpdateBook);
			}
		});
	}
	
	
	
	
	
	
	
	
	private void initMapSearch(){
		BookKey = new HashMap<String,String>();
		BookKey.put("书名", "bkName");
		BookKey.put("作者", "bkAuthor");
		BookKey.put("出版社", "bkPress");
		BookKey.put("简介", "bkBrief");
	}
	private void initTabbedPane(){
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(15, 37, 1170, 166);
		add(tabbedPane);
	}
	private void initCtrlPanel(){
		CtrlPanel = new JPanel();
		CtrlPanel.setBounds(15, 617, 1170, 109);
		add(CtrlPanel);
		CtrlPanel.setLayout(null);
		
		Update = new JButton("修改");
		
		Update.setBounds(149, 33, 123, 29);
		CtrlPanel.add(Update);
		
		Delete = new JButton("删除");
		
		Delete.setBounds(374, 33, 123, 29);
		CtrlPanel.add(Delete);
		
		ToExcel = new JButton("导出Excel");
		ToExcel.setBounds(584, 33, 123, 29);
		CtrlPanel.add(ToExcel);
		
		Retrun = new JButton("返回");
		Retrun.setBounds(796, 33, 123, 29);
		CtrlPanel.add(Retrun);
	}
	private void initResultPanel(){
		CustomizedTableModel<Book> tableModel = new CustomizedTableModel<>(
				bookBll.getDisplayColumnNames(),bookBll.getMethodNames());
		searchResultTable = new JTable(tableModel);
		ResultPanel = new JScrollPane(searchResultTable);
		ResultPanel.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ResultPanel.setBounds(25, 218, 1160, 396);
		add(ResultPanel);
		searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}
	private void initSupSearchPanel(){
		SupSearchPanel = new JPanel();
		tabbedPane.addTab("高级查询", null, SupSearchPanel, null);
		SupSearchPanel.setLayout(null);
		
		JLabel label_1 = new JLabel("图书名称：");
		label_1.setBounds(15, 53, 105, 21);
		SupSearchPanel.add(label_1);
		
		tfBookName = new JTextField();
		tfBookName.setBounds(116, 50, 144, 27);
		SupSearchPanel.add(tfBookName);
		tfBookName.setColumns(10);
		
		JLabel label_2 = new JLabel("图书作者：");
		label_2.setBounds(313, 53, 99, 21);
		SupSearchPanel.add(label_2);
		
		tfBookAuthor = new JTextField();
		tfBookAuthor.setBounds(422, 50, 130, 27);
		SupSearchPanel.add(tfBookAuthor);
		tfBookAuthor.setColumns(10);
		
		JLabel label_3 = new JLabel("出版社：");
		label_3.setBounds(619, 53, 81, 21);
		SupSearchPanel.add(label_3);
		
		tfBookPress = new JTextField();
		tfBookPress.setBounds(700, 50, 130, 27);
		SupSearchPanel.add(tfBookPress);
		tfBookPress.setColumns(10);
		
		SupSearchBtn = new JButton("查询");
		
		SupSearchBtn.setBounds(908, 49, 123, 29);
		SupSearchPanel.add(SupSearchBtn);
	}
	private void initSimpleSearchPanel(){
		SimpleSearchPanel = new JPanel();
		tabbedPane.addTab("简单查询", null, SimpleSearchPanel, null);
		SimpleSearchPanel.setLayout(null);
		
		JLabel label = new JLabel("检索字段：");
		label.setBounds(32, 44, 105, 37);
		SimpleSearchPanel.add(label);
		
		SearchKey = new JComboBox();
		SearchKey.setBounds(130, 49, 142, 27);
		Set set = BookKey.keySet();
		Iterator e = set.iterator();
        while(e.hasNext()){
        	SearchKey.addItem(e.next());
        }
		SimpleSearchPanel.add(SearchKey);
		
		SearchContent = new JTextField();
		SearchContent.setBounds(341, 49, 429, 27);
		SearchContent.setColumns(10);
		SimpleSearchPanel.add(SearchContent);
		
		SearchBtn = new JButton("查询");
		SearchBtn.setBounds(849, 48, 69, 29);
		SimpleSearchPanel.add(SearchBtn);
	}

	
}
