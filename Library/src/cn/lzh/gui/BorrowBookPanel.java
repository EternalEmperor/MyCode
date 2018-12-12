package cn.lzh.gui;

import java.awt.Dimension;
import java.sql.Date;
//import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import cn.lzh.bll.BookAdmin;
import cn.lzh.bll.BorrowAdmin;
import cn.lzh.bll.ReaderAdmin;
import cn.lzh.bll.ReaderTypeAdmin;
import cn.lzh.vo.Book;
import cn.lzh.vo.Borrow;
import cn.lzh.vo.Reader;
import cn.lzh.vo.ReaderType;

import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BorrowBookPanel extends JPanel {
	private JTextField tfRdID;
	private JTextField tfRdName;
	private JTextField tfRdDept;
	private JTextField tfRdType;
	private JTextField tfCanLendQty;
	private JTextField tfCanLendDay;
	private JTextField tfBorrowQty;
	private JTable BorrowedBookTable;
	private JTable SearchBookTable;
	private JTextField tfBkID;
	private JTextField tfBkName;
	private JPanel ReaderInfoPanel;
	private JButton ReaderSearchBtn;
	private JScrollPane BorrowedBookPanel;
	private JScrollPane SearchResultPanel;
	private JPanel CtrlPanel;
	private JButton BorrowBookBtn;
	private JButton ReturnBookBtn;
	private JButton ContinueBookBtn;
	private JButton CancelBtn;
	private JPanel SearchBookPanel;
	private JButton BookSearchBtn;
	
	private BorrowAdmin borrowBll = new BorrowAdmin();
	private BookAdmin bookBll = new BookAdmin();
	private ReaderTypeAdmin readerTypeBll = new ReaderTypeAdmin();
	private ReaderAdmin readerBll = new ReaderAdmin();
	private Borrow[] Allrecords = null;
	private Reader reader = null;
	private ReaderType readerType = null;
	
	public BorrowBookPanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);
		
		initReaderInfoPanel();
		initBorrowBookPanel();
		initSearchBookPanel();
		initSearchResultPanel();
		initCtrlPanel();
		addButtonClickEventHandlers();
		
	}
	
	private void addButtonClickEventHandlers(){
		ReaderSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					reader = readerBll.getReader(Integer.valueOf(tfRdID.getText().trim()));
					if(reader != null && isValid(reader)){
						readerType = readerTypeBll.getObjectById(reader.getRdType());
						setReaderToText(reader);
						Allrecords = borrowBll.getBorrowByRdID(reader.getRdID());
						updateBorrowedBookTable(Allrecords);
						
					}else{
						JOptionPane.showMessageDialog(null, "不存在该用户");
					}
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		BookSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					Book[] bks = bookBll.getBookByIdAndName(
							tfBkID.getText().trim(),tfBkName.getText().trim());
					if(bks != null){
						updateSearchResultTable(bks);
					}else{
						JOptionPane.showMessageDialog(null, "没有找到符合要求的记录");
					}
				}catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		BorrowBookBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = SearchBookTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}else if(reader == null){
					JOptionPane.showMessageDialog(null, "找不到借阅读者！");
					return;
				}
				int BorrowedNums = Integer.valueOf(tfBorrowQty.getText().trim()) + 1;
				if(BorrowedNums > readerType.getCanLendQty()){
					JOptionPane.showMessageDialog(null, "超出借书额度！");
					return;
				}else{
					reader.setRdBorrowQty(BorrowedNums);
					readerBll.updateReader(reader);
					Book book = ((CustomizedTableModel<Book>) SearchBookTable.getModel()).getObjectAt(selectedRow);
					Borrow borrowRecord = NewBorrowRecord(reader, book);
					try{
						if(borrowBll.isBorrowed(borrowRecord)){
							JOptionPane.showMessageDialog(null, "该用户已经借阅过此书，请选择续借！");
							return;
						}
						if(!book.getBkStatus().equals("在馆")){
							JOptionPane.showMessageDialog(null, "该书不在馆或因其他原因无法出借！");
							return;
						}
						
					}catch (Exception e3) {
						e3.printStackTrace();
					}
					if(borrowBll.addBorrowRecord(borrowRecord) > 0){
						JOptionPane.showMessageDialog(null, "借阅成功！");
						Allrecords = borrowBll.getBorrowByRdID(reader.getRdID());
						updateBorrowedBookTable(Allrecords);
						tfBorrowQty.setText(String.valueOf(BorrowedNums));
					}else{
						JOptionPane.showMessageDialog(null, "借阅失败");
					}
				}		
			}
		});
		
		ReturnBookBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = BorrowedBookTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}else if(reader == null){
					JOptionPane.showMessageDialog(null, "找不到借阅读者！");
					return;
				}
				int BorrowedNums = Integer.valueOf(tfBorrowQty.getText().trim()) - 1;
				reader.setRdBorrowQty(BorrowedNums);
				readerBll.updateReader(reader);
				Borrow borrowRecord = ((CustomizedTableModel<Borrow>) BorrowedBookTable.getModel()).getObjectAt(selectedRow);
				if(borrowBll.deleteBorrowRecord(borrowRecord) > 0){
					JOptionPane.showMessageDialog(null, "还书成功！");
					Allrecords = borrowBll.getBorrowByRdID(reader.getRdID());
					updateBorrowedBookTable(Allrecords);
					tfBorrowQty.setText(String.valueOf(BorrowedNums));
				}else{
					JOptionPane.showMessageDialog(null, "还书失败！");
				}
				
			}
		});
		
		ContinueBookBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = BorrowedBookTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}else if(reader == null){
					JOptionPane.showMessageDialog(null, "找不到借阅读者！");
					return;
				}
				Borrow borrowRecord = ((CustomizedTableModel<Borrow>) BorrowedBookTable.getModel()).getObjectAt(selectedRow);
				int Continuetimes = borrowRecord.getContinueTimes() + 1;
				if(Continuetimes > readerType.getCanContinueTimes()){
					JOptionPane.showMessageDialog(null, "超出续借次数！");
				}else{
					Calendar c = Calendar.getInstance();
					c.setTime(borrowRecord.getDateRetPlan());
					int canBorrowDays = Integer.valueOf(tfCanLendDay.getText().trim());
					c.add(Calendar.DATE, canBorrowDays);
					String ContinueDay = c.get(Calendar.YEAR)+"-"+( c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE);
					borrowRecord.setContinueTimes(Continuetimes);
					borrowRecord.setDateRetPlan(strToDate(ContinueDay));
					borrowBll.updateBorrowRecord(borrowRecord);
					Allrecords = borrowBll.getBorrowByRdID(reader.getRdID());
					updateBorrowedBookTable(Allrecords);
					JOptionPane.showMessageDialog(null, "续借成功！");
				}
			}
		});
		
		
	}
	
	private boolean isValid(Reader reader){
		if(reader.getRdStatus().equals("有效")){
			return true;
		}
		return false;
	}
	
	private Borrow NewBorrowRecord(Reader reader, Book book){
		Borrow borrow = new Borrow();
		borrow.setRdID(reader.getRdID());
		borrow.setBkID(book.getBkID());
		Calendar c = Calendar.getInstance();
		String NowDay = c.get(Calendar.YEAR)+"-"+( c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE);
		c.add(Calendar.DATE, Integer.valueOf(tfCanLendDay.getText().trim()));
		String PlanDay = c.get(Calendar.YEAR)+"-"+( c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE);
		borrow.setDateOut(strToDate(NowDay));
		borrow.setDateRetPlan(strToDate(PlanDay));
		return borrow;
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
	
	private void setReaderToText(Reader reader){
		tfRdName.setText(reader.getRdName());
		tfRdDept.setText(reader.getRdDept());
		//ReaderType readerType = readerTypeBll.getObjectById(reader.getRdType());
		tfRdType.setText(readerType.getRdTypeName());
		tfCanLendQty.setText(String.valueOf(readerType.getCanLendQty()));
		tfCanLendDay.setText(String.valueOf(readerType.getCanLendDay()));
		tfBorrowQty.setText(String.valueOf(reader.getRdBorrowQty()));
		
	}
	
	private void updateBorrowedBookTable(Borrow[] borrows) {
		// TODO 自动生成的方法存根
		if(borrows == null){
			return;
		}
		CustomizedTableModel<Borrow> tableModel = (CustomizedTableModel<Borrow>) BorrowedBookTable.getModel();
		tableModel.setRecords(borrows);
		//更新表格
		tableModel.fireTableDataChanged();
	}
	
	
	private void updateSearchResultTable(Book[] books) {
		// TODO 自动生成的方法存根
		if(books == null){
			JOptionPane.showMessageDialog(null, "没有找到符合要求的记录！");
			return;
		}
		CustomizedTableModel<Book> tableModel = (CustomizedTableModel<Book>) SearchBookTable.getModel();
		tableModel.setRecords(books);
		//更新表格
		tableModel.fireTableDataChanged();
	}
	
	
	
	private void initSearchBookPanel(){
		SearchBookPanel = new JPanel();
		SearchBookPanel.setBorder(new TitledBorder(null, "\u56FE\u4E66\u67E5\u8BE2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SearchBookPanel.setBounds(15, 350, 1170, 95);
		add(SearchBookPanel);
		SearchBookPanel.setLayout(null);
		
		JLabel label_6 = new JLabel("图书序号：");
		label_6.setBounds(105, 40, 90, 21);
		SearchBookPanel.add(label_6);
		
		tfBkID = new JTextField();
		tfBkID.setBounds(197, 37, 96, 27);
		SearchBookPanel.add(tfBkID);
		tfBkID.setColumns(10);
		
		JLabel label_7 = new JLabel("图书名称：");
		label_7.setBounds(370, 40, 90, 21);
		SearchBookPanel.add(label_7);
		
		tfBkName = new JTextField();
		tfBkName.setBounds(475, 37, 96, 27);
		SearchBookPanel.add(tfBkName);
		tfBkName.setColumns(10);
		
		BookSearchBtn = new JButton("查询");
		
		BookSearchBtn.setBounds(645, 36, 123, 29);
		SearchBookPanel.add(BookSearchBtn);
	}
	private void initCtrlPanel(){
		CtrlPanel = new JPanel();
		CtrlPanel.setBounds(15, 669, 1170, 66);
		add(CtrlPanel);
		CtrlPanel.setLayout(null);
		
		BorrowBookBtn = new JButton("借阅");
		
		BorrowBookBtn.setBounds(220, 15, 123, 29);
		CtrlPanel.add(BorrowBookBtn);
		
		ReturnBookBtn = new JButton("还书");
		
		ReturnBookBtn.setBounds(391, 15, 123, 29);
		CtrlPanel.add(ReturnBookBtn);
		
		ContinueBookBtn = new JButton("续借");
		
		ContinueBookBtn.setBounds(550, 15, 123, 29);
		CtrlPanel.add(ContinueBookBtn);
		
		CancelBtn = new JButton("返回");
		CancelBtn.setBounds(719, 15, 123, 29);
		CtrlPanel.add(CancelBtn);
	}
	private void initSearchResultPanel(){
		CustomizedTableModel<Book> tableModel = new CustomizedTableModel<>(
				bookBll.getDisplayColumnNames(),bookBll.getMethodNames());
		SearchBookTable = new JTable(tableModel);
		SearchBookTable.setBounds(15, 26, 1140, 177);
		
		SearchResultPanel = new JScrollPane(SearchBookTable);
		SearchResultPanel.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SearchResultPanel.setBounds(15, 448, 1170, 218);
		add(SearchResultPanel);
	}
	private void initBorrowBookPanel(){
		CustomizedTableModel<Borrow> tableModel = new CustomizedTableModel<>(
				borrowBll.getDisplayColumnNames(),borrowBll.getMethodNames());
		BorrowedBookTable = new JTable(tableModel);
		BorrowedBookTable.setBounds(15, 24, 1140, 167);
		
		BorrowedBookPanel = new JScrollPane(BorrowedBookTable);
		BorrowedBookPanel.setBorder(new TitledBorder(null, "\u5DF2\u501F\u56FE\u4E66", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		BorrowedBookPanel.setBounds(15, 134, 1170, 206);
		add(BorrowedBookPanel);
	}
	private void initReaderInfoPanel(){
		ReaderInfoPanel = new JPanel();
		ReaderInfoPanel.setBorder(new TitledBorder(null, "\u8BFB\u8005\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ReaderInfoPanel.setBounds(15, 15, 1170, 120);
		add(ReaderInfoPanel);
		ReaderInfoPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("读者编号：");
		lblNewLabel.setBounds(15, 46, 96, 21);
		ReaderInfoPanel.add(lblNewLabel);
		
		tfRdID = new JTextField();
		tfRdID.setBounds(112, 43, 96, 27);
		ReaderInfoPanel.add(tfRdID);
		tfRdID.setColumns(10);
		
		ReaderSearchBtn = new JButton("查询");
		
		ReaderSearchBtn.setBounds(225, 42, 123, 29);
		ReaderInfoPanel.add(ReaderSearchBtn);
		
		JLabel label = new JLabel("读者姓名：");
		label.setBounds(392, 26, 96, 21);
		ReaderInfoPanel.add(label);
		
		JLabel label_1 = new JLabel("读者单位：");
		label_1.setBounds(680, 26, 96, 21);
		ReaderInfoPanel.add(label_1);
		
		JLabel label_2 = new JLabel("读者类型：");
		label_2.setBounds(929, 26, 90, 21);
		ReaderInfoPanel.add(label_2);
		
		JLabel label_3 = new JLabel("可借书数量：");
		label_3.setBounds(389, 71, 116, 21);
		ReaderInfoPanel.add(label_3);
		
		JLabel label_4 = new JLabel("可借书天数：");
		label_4.setBounds(659, 71, 117, 21);
		ReaderInfoPanel.add(label_4);
		
		JLabel label_5 = new JLabel("已借数量：");
		label_5.setBounds(929, 71, 92, 21);
		ReaderInfoPanel.add(label_5);
		
		tfRdName = new JTextField();
		tfRdName.setBounds(503, 23, 96, 27);
		ReaderInfoPanel.add(tfRdName);
		tfRdName.setColumns(10);
		tfRdName.setEnabled(false);
		
		tfRdDept = new JTextField();
		tfRdDept.setBounds(776, 23, 96, 27);
		ReaderInfoPanel.add(tfRdDept);
		tfRdDept.setColumns(10);
		tfRdDept.setEnabled(false);
		
		tfRdType = new JTextField();
		tfRdType.setBounds(1020, 23, 96, 27);
		ReaderInfoPanel.add(tfRdType);
		tfRdType.setColumns(10);
		tfRdType.setEnabled(false);
		
		tfCanLendQty = new JTextField();
		tfCanLendQty.setBounds(503, 68, 96, 27);
		ReaderInfoPanel.add(tfCanLendQty);
		tfCanLendQty.setColumns(10);
		tfCanLendQty.setEnabled(false);
		
		tfCanLendDay = new JTextField();
		tfCanLendDay.setBounds(776, 68, 96, 27);
		ReaderInfoPanel.add(tfCanLendDay);
		tfCanLendDay.setColumns(10);
		tfCanLendDay.setEnabled(false);
		
		tfBorrowQty = new JTextField();
		tfBorrowQty.setBounds(1020, 68, 96, 27);
		ReaderInfoPanel.add(tfBorrowQty);
		tfBorrowQty.setColumns(10);
		tfBorrowQty.setEnabled(false);
	}
	
}
