package cn.lzh.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import cn.lzh.bll.DepartmentTypeAdmin;
import cn.lzh.bll.ReaderAdmin;
import cn.lzh.bll.ReaderTypeAdmin;
import cn.lzh.dal.ReaderDAL;
import cn.lzh.dal.ReaderTypeDAL;
import cn.lzh.vo.*;

import javax.swing.border.LineBorder;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;

import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

public class ReaderPanel extends JPanel {
	
	private enum OpStatus{
		inSelect,inNew,inChange
	}
	
	private OpStatus ops;
	
	private JTextField tfUserName;
	private JTable searchResultTable;
	private JTextField tfReaderID;
	private JTextField tfReaderName;
	private JTextField tfNumBorrowed;
	private JTextField tfStatus;
	private JTextField tfReaderRole;
	private JTextField tfDate;
	private JTextField tfPhone;
	private JPanel searchPanel;
	private JComboBox rdTypeComboBox;
	private JButton btnQuery;
	private JComboBox deptTypeComboBox;
	private JScrollPane searchResultPanel;
	private JPanel readerInfoPanel;
	private JComboBox cbGender;
	private JComboBox cbReaderType;
	private JComboBox cbDeptType;
	private JPanel functionCtrlPanel;
	private JButton btnNewReader;
	private JButton btnUpdateReader;
	private JButton btnLost;
	private JButton btnFound;
	private JButton btnCancelReader;
	private JButton btnClose;
	private JPanel editCtrlPanel;
	private JButton btnSubmitNew;
	private JButton btnSubmitUpdate;
	private JButton btnCancelEdit;
	private JPasswordField passwordField;
	
	private ReaderTypeAdmin readerTypeBll = new ReaderTypeAdmin();
	private DepartmentTypeAdmin deptTypeBll = new DepartmentTypeAdmin();
	private ReaderAdmin readerBll = new ReaderAdmin();
	private ReaderTypeDAL readertypedal = new ReaderTypeDAL();
	
//	private InputVerifier verifier = new InputVerifier() {
//		@Override
//		public boolean verify(JComponent comp) {
//			boolean returnValue;
//			JTextField textField = (JTextField)comp;
//			int a = -1;
////			try {
////				int m = Integer.parseInt(textField.getText());
////				returnValue = true;
////			} catch (NumberFormatException e) {
////				Toolkit.getDefaultToolkit().beep();
////				JOptionPane.showMessageDialog(null, "只能输入数字格式");
////				returnValue = false;
////			}
//			return returnValue;		
//			}
//		};
	
	
	public ReaderPanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);	
		initSearchPanel();
		initSearchResultPanel();
		initReaderInfoPanel();
		initFunctionControlsPanel();
		initEditControlsPanel();
		//设置初始状态操作
		setStatus(OpStatus.inSelect);
		//添加动作监听器
		addButtonClickEventHandlers();
		
		
	}
	
	
	
	
	private void setComponentStatusInPanel(JPanel panel,boolean status){

		for(Component comp : panel.getComponents()){
			comp.setEnabled(status);
		}
	}
	
	

	
	/**
	 * 设置面板状态
	 * @param opst 枚举值
	 */
	private void setStatus(OpStatus opst){

		ops = opst;
		switch (ops) {
		case inSelect:
			searchPanel.setEnabled(true);
			searchResultPanel.setEnabled(true);
			functionCtrlPanel.setEnabled(true);
			// 更改panel中控件的状态
			setComponentStatusInPanel(functionCtrlPanel,true);
			readerInfoPanel.setEnabled(false);
			readerInfoPanel.setVisible(false);
			editCtrlPanel.setEnabled(false);
			editCtrlPanel.setVisible(false);
			setComponentStatusInPanel(editCtrlPanel, false);
			break;
		case inNew:
			searchPanel.setEnabled(false);
			searchResultPanel.setEnabled(false);
			functionCtrlPanel.setEnabled(false);
			// 更改panel中控件的状态
			setComponentStatusInPanel(functionCtrlPanel,false);
			readerInfoPanel.setEnabled(true);
			readerInfoPanel.setVisible(true);
			editCtrlPanel.setEnabled(true);
			editCtrlPanel.setVisible(true);
			setComponentStatusInPanel(editCtrlPanel, true);
			btnSubmitUpdate.setEnabled(false);
			break;
		case inChange:
			searchPanel.setEnabled(false);
			searchResultPanel.setEnabled(false);
			functionCtrlPanel.setEnabled(false);
			setComponentStatusInPanel(functionCtrlPanel,false);
//			setComponentStatusInPanel(searchPanel,false);
			readerInfoPanel.setEnabled(true);
			readerInfoPanel.setVisible(true);
			editCtrlPanel.setEnabled(true);
			editCtrlPanel.setVisible(true);
			setComponentStatusInPanel(editCtrlPanel, true);
			btnSubmitNew.setEnabled(false);
			break;
		}
	}

	/**
	 * 按钮的监听操作
	 */
	private void addButtonClickEventHandlers(){
		
		btnNewReader.addActionListener(new ActionListener() { //办理借书证
			public void actionPerformed(ActionEvent e) {
				setStatus(OpStatus.inNew);
				tfReaderID.setText("");
				tfReaderName.setText("");
				passwordField.setText("");
				tfReaderRole.setText("");
				tfPhone.setText("");
				tfReaderID.setEditable(true);
				tfNumBorrowed.setText("0");
				tfStatus.setText("有效");
				tfReaderRole.setEditable(true);
				cbReaderType.setEnabled(true);
				java.util.Date day = new java.util.Date();
				SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
				tfDate.setText(dFormat.format(day));
			}
		});
		
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReaderType rdType = (ReaderType) rdTypeComboBox.getSelectedItem();
				DepartmentType deptType = (DepartmentType) deptTypeComboBox.getSelectedItem();
				String userName = tfUserName.getText().trim();
				Reader[] hits = readerBll.retrieveReaders(rdType, deptType, userName);
				//更新查询结果列表
				updateResultTable(hits);
				setStatus(OpStatus.inSelect);
			}
		});
		
		btnUpdateReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}
				
				setReaderToText(((CustomizedTableModel<Reader>)searchResultTable.getModel()).getObjectAt(selectedRow));
				tfReaderID.setEditable(false);
				tfReaderRole.setEditable(false);
				cbReaderType.setEditable(true);
				cbDeptType.setEditable(true);
				//cbReaderType.setEnabled(false);
				UIManager.put("ComboBox.disabledForeground", new Color(0, 0, 0));
				setStatus(OpStatus.inChange);
			}
		});
		
		btnSubmitNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(readerBll.addReader(getReaderFromText()) > 0){
					JOptionPane.showMessageDialog(null, "办理借书证成功！");
					setStatus(OpStatus.inSelect);
				}else{
					setStatus(OpStatus.inNew);
				}
				
			}
		});
		
		btnSubmitUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readerBll.updateReader(getReaderFromText());
				JOptionPane.showMessageDialog(null, "更改信息成果！");
				setStatus(OpStatus.inSelect);
			}
		});
		
		btnLost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}
				Reader rd = ((CustomizedTableModel<Reader>)searchResultTable.getModel()).getObjectAt(selectedRow);
				rd.setRdStatus("挂失");
				readerBll.updateReader(rd);
				JOptionPane.showMessageDialog(null, "挂失成功！");
				ReaderType rdType = (ReaderType) rdTypeComboBox.getSelectedItem();
				DepartmentType deptType = (DepartmentType) deptTypeComboBox.getSelectedItem();
				String userName = tfUserName.getText().trim();
				Reader[] hits = readerBll.retrieveReaders(rdType, deptType, userName);
				//更新查询结果列表
				updateResultTable(hits);
				
			}
		});
		
		btnFound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}
				Reader rd = ((CustomizedTableModel<Reader>)searchResultTable.getModel()).getObjectAt(selectedRow);
				rd.setRdStatus("有效");
				readerBll.updateReader(rd);
				JOptionPane.showMessageDialog(null, "解除成功！");
				ReaderType rdType = (ReaderType) rdTypeComboBox.getSelectedItem();
				DepartmentType deptType = (DepartmentType) deptTypeComboBox.getSelectedItem();
				String userName = tfUserName.getText().trim();
				Reader[] hits = readerBll.retrieveReaders(rdType, deptType, userName);
				//更新查询结果列表
				updateResultTable(hits);
			}
		});
		
		btnCancelReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow<0){
					JOptionPane.showMessageDialog(null, "请选中一条数据！");
					return;
				}
				Reader rd = ((CustomizedTableModel<Reader>)searchResultTable.getModel()).getObjectAt(selectedRow);
				rd.setRdStatus("注销");
				readerBll.updateReader(rd);
				JOptionPane.showMessageDialog(null, "注销成功！");
				ReaderType rdType = (ReaderType) rdTypeComboBox.getSelectedItem();
				DepartmentType deptType = (DepartmentType) deptTypeComboBox.getSelectedItem();
				String userName = tfUserName.getText().trim();
				Reader[] hits = readerBll.retrieveReaders(rdType, deptType, userName);
				//更新查询结果列表
				updateResultTable(hits);
			}
		});
		
		btnCancelEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStatus(ops.inSelect);
			}
		});
		
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)Main.cards.getLayout();
				cl.show(Main.cards, Main.BlankPanelName);
			}
		});

	}

	/**
	 * 从readerinfo面板中拿出注册信息保存为reader格式
	 * @return
	 */
	private Reader getReaderFromText(){
		Reader reader = new Reader();
		reader.setRdID(Integer.valueOf(tfReaderID.getText().trim()));
		reader.setRdName(tfReaderName.getText().trim());
		reader.setRdPwd(String.valueOf(passwordField.getPassword()));
		reader.setRdSex(cbGender.getSelectedItem().toString());
		reader.setRdType(((ReaderType)cbReaderType.getSelectedItem()).getRdType());
		reader.setRdDept(((DepartmentType)cbDeptType.getSelectedItem()).getRdDept());
		reader.setRdBorrowQty(Integer.valueOf(tfNumBorrowed.getText()));
		reader.setRdStatus(tfStatus.getText().trim());
		reader.setRdAdminRoles(Integer.valueOf(tfReaderRole.getText()));
		reader.setRdDateReg(strToDate(tfDate.getText().trim()));
		reader.setRdPhone(tfPhone.getText().trim());
		return reader;
	}
	
	/**
	 * 日期装换
	 * @param strDate 字符串格式日期
	 * @return
	 */
	
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

	/**
	 * 将得到的用户reader呈现在面板上
	 * @param reader
	 */
	private void setReaderToText(Reader reader) {
		// TODO 自动生成的方法存根
		tfReaderID.setText(String.valueOf(reader.getRdID()));
		System.out.println(String.valueOf(reader.getRdID()));
		tfReaderName.setText(reader.getRdName());
		passwordField.setText(reader.getRdPwd());
		tfNumBorrowed.setText(String.valueOf(reader.getRdBorrowQty()));
		tfStatus.setText(reader.getRdStatus());
		tfReaderRole.setText(String.valueOf(reader.getRdAdminRoles()));
		tfPhone.setText(String.valueOf(reader.getRdPhone()));
		tfDate.setText(String.valueOf(reader.getRdDateReg()));
		cbGender.setSelectedItem(reader.getRdSex());
		cbReaderType.setEditable(true);
		cbDeptType.setEditable(true);
		cbReaderType.setSelectedItem((readerTypeBll.getObjectById(reader.getRdType())).getRdTypeName());
		cbDeptType.setSelectedItem(deptTypeBll.getObjectById(reader.getRdDept()));
	}
	/**
	 * 面板初始化函数组
	 */
	private void initSearchPanel(){
		searchPanel = new JPanel();
		searchPanel.setBounds(0, 0, 1200, 72);
		add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("读者类别：");
		lblNewLabel.setBounds(15, 15, 97, 42);
		searchPanel.add(lblNewLabel);
		
		
		rdTypeComboBox = new JComboBox(readerTypeBll.getReaderTypes());
		rdTypeComboBox.setBounds(105, 23, 112, 27);
		searchPanel.add(rdTypeComboBox);
		
		JLabel label = new JLabel("姓名:");
		label.setBounds(501, 26, 81, 21);
		searchPanel.add(label);
		
		tfUserName = new JTextField();
		tfUserName.setBounds(558, 23, 120, 27);
		searchPanel.add(tfUserName);
		tfUserName.setColumns(10);
		
		btnQuery = new JButton("查找");
		
		btnQuery.setBounds(751, 22, 123, 29);
		searchPanel.add(btnQuery);
		
		JLabel label_10 = new JLabel("单位名称");
		label_10.setBounds(250, 26, 81, 21);
		searchPanel.add(label_10);
		
		deptTypeComboBox = new JComboBox(deptTypeBll.getDepartmentTypes());
		deptTypeComboBox.setBounds(330, 23, 119, 27);
		searchPanel.add(deptTypeComboBox);
	}
	
	private void updateResultTable(Reader[] readers){
		if(readers == null){
			JOptionPane.showMessageDialog(null, "没有找到符合要求的记录");
			return;
		}
		CustomizedTableModel<Reader> tableModel = (CustomizedTableModel<Reader>)searchResultTable.getModel();
		tableModel.setRecords(readers);
		//更新表格
		tableModel.fireTableDataChanged();
	}
	
	private void initSearchResultPanel(){
		CustomizedTableModel<Reader> tabelModel = new CustomizedTableModel<>
		(readerBll.getDisplayColumnNames(), readerBll.getMethodNames());
		searchResultTable = new JTable(tabelModel);
		searchResultTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		searchResultTable.setBounds(15, 27, 667, 535);
		
		searchResultPanel =new JScrollPane(searchResultTable);
		searchResultPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u67E5\u8BE2\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchResultPanel.setBounds(15, 76, 697, 582);
		add(searchResultPanel);
		
		searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
	}
	private void initReaderInfoPanel(){
		readerInfoPanel = new JPanel();
		readerInfoPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u8BFB\u8005\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		readerInfoPanel.setBounds(727, 81, 458, 571);
		add(readerInfoPanel);
		readerInfoPanel.setLayout(null);
		
		JLabel label_1 = new JLabel("借书证号");
		label_1.setBounds(99, 42, 81, 21);
		readerInfoPanel.add(label_1);
		
		tfReaderID = new JTextField();
		tfReaderID.setEditable(false);
		tfReaderID.setDocument(new NumberJudge());
		tfReaderID.setBounds(273, 39, 123, 27);
		readerInfoPanel.add(tfReaderID);
		tfReaderID.setColumns(10);
		
		JLabel label_2 = new JLabel("姓名：");
		label_2.setBounds(99, 82, 81, 21);
		readerInfoPanel.add(label_2);
		
		tfReaderName = new JTextField();
		tfReaderName.setBounds(273, 79, 123, 27);
		readerInfoPanel.add(tfReaderName);
		tfReaderName.setColumns(10);
		
		JLabel label_3 = new JLabel("密码：");
		label_3.setBounds(99, 128, 81, 21);
		readerInfoPanel.add(label_3);
		
		JLabel label_4 = new JLabel("性别：");
		label_4.setBounds(99, 178, 81, 21);
		readerInfoPanel.add(label_4);
		
		cbGender = new JComboBox();
		cbGender.setModel(new DefaultComboBoxModel(new String[] {"男", "女"}));
		cbGender.setSelectedIndex(0);
		cbGender.setBounds(273, 175, 70, 27);
		readerInfoPanel.add(cbGender);
		
		JLabel label_5 = new JLabel("已借书");
		label_5.setBounds(99, 220, 81, 21);
		readerInfoPanel.add(label_5);
		
		tfNumBorrowed = new JTextField();
		tfNumBorrowed.setEditable(false);
		tfNumBorrowed.setBounds(273, 217, 123, 27);
		readerInfoPanel.add(tfNumBorrowed);
		tfNumBorrowed.setColumns(10);
		
		JLabel label_6 = new JLabel("证件状态");
		label_6.setBounds(99, 274, 81, 21);
		readerInfoPanel.add(label_6);
		
		tfStatus = new JTextField();
		tfStatus.setEditable(false);
		tfStatus.setBounds(273, 271, 123, 27);
		readerInfoPanel.add(tfStatus);
		tfStatus.setColumns(10);
		
		JLabel label_7 = new JLabel("读者角色");
		label_7.setBounds(99, 328, 81, 21);
		readerInfoPanel.add(label_7);
		
		tfReaderRole = new JTextField();
		tfReaderRole.setEditable(false);
		tfReaderRole.setBounds(273, 325, 123, 27);
		readerInfoPanel.add(tfReaderRole);
		tfReaderRole.setColumns(10);
		
		JLabel label_8 = new JLabel("读者类别");
		label_8.setBounds(99, 384, 81, 21);
		readerInfoPanel.add(label_8);
		
		cbReaderType = new JComboBox(readerTypeBll.getReaderTypes());
		cbReaderType.setBounds(273, 367, 123, 27);
		readerInfoPanel.add(cbReaderType);
		
		JLabel label_9 = new JLabel("办证日期");
		label_9.setBounds(99, 535, 81, 21);
		readerInfoPanel.add(label_9);
		
		tfDate = new JTextField();
		tfDate.setEditable(false);
		tfDate.setBounds(274, 532, 155, 27);
		readerInfoPanel.add(tfDate);
		tfDate.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("单位名称");
		lblNewLabel_1.setBounds(99, 426, 81, 21);
		readerInfoPanel.add(lblNewLabel_1);
		
		cbDeptType = new JComboBox(deptTypeBll.getDepartmentTypes());
		cbDeptType.setBounds(273, 423, 123, 27);
		readerInfoPanel.add(cbDeptType);
		
		JLabel lblNewLabel_2 = new JLabel("电话号码");
		lblNewLabel_2.setBounds(99, 478, 81, 21);
		readerInfoPanel.add(lblNewLabel_2);
		
		tfPhone = new JTextField();
		tfPhone.setBounds(273, 475, 156, 27);
		tfPhone.setDocument(new NumberJudge());
		readerInfoPanel.add(tfPhone);
		tfPhone.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(273, 125, 123, 27);
		readerInfoPanel.add(passwordField);
	}
	private void initFunctionControlsPanel(){
		functionCtrlPanel = new JPanel();
		functionCtrlPanel.setBounds(15, 673, 730, 42);
		add(functionCtrlPanel);
		functionCtrlPanel.setLayout(null);
		
		btnNewReader = new JButton("办理借书证");
		
		btnNewReader.setBounds(0, 0, 133, 29);
		functionCtrlPanel.add(btnNewReader);
		
		btnUpdateReader = new JButton("变更借书证");
		
		
		btnUpdateReader.setBounds(148, 0, 123, 29);
		functionCtrlPanel.add(btnUpdateReader);
		
		btnLost = new JButton("挂失");
		
		btnLost.setBounds(286, 0, 99, 29);
		functionCtrlPanel.add(btnLost);
		
		btnFound = new JButton("解除挂失");
		
		btnFound.setBounds(400, 0, 111, 29);
		functionCtrlPanel.add(btnFound);
		
		btnCancelReader = new JButton("注销");
		
		btnCancelReader.setBounds(526, 0, 85, 29);
		functionCtrlPanel.add(btnCancelReader);
		
		btnClose = new JButton("退出");
		btnClose.setBounds(626, 0, 94, 29);
		functionCtrlPanel.add(btnClose);
	}
	private void initEditControlsPanel(){
		editCtrlPanel = new JPanel();
		editCtrlPanel.setBounds(760, 673, 425, 42);
		add(editCtrlPanel);
		editCtrlPanel.setLayout(null);
		
		btnSubmitNew = new JButton("确认办证");
		
		btnSubmitNew.setBounds(15, 0, 123, 29);
		editCtrlPanel.add(btnSubmitNew);
		
		btnSubmitUpdate = new JButton("确认变更");
		
		btnSubmitUpdate.setBounds(151, 0, 123, 29);
		editCtrlPanel.add(btnSubmitUpdate);
		
		btnCancelEdit = new JButton("取消");
		
		btnCancelEdit.setBounds(287, 0, 123, 29);
		editCtrlPanel.add(btnCancelEdit);
	}
	
	private String[] getReaderTypeNameLitm(){
		ReaderType[] rdt = readerTypeBll.getReaderTypes();  
		String[] nameItem = new String[rdt.length];
		for(int i = 0 ; i < rdt.length; i++){
			nameItem[i] = rdt[i].getRdTypeName();
		}
		return nameItem;
	}
}
