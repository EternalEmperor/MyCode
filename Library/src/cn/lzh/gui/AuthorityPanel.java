package cn.lzh.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.mysql.cj.log.Log;

import cn.lzh.bll.DepartmentTypeAdmin;
import cn.lzh.bll.ReaderAdmin;
import cn.lzh.bll.ReaderTypeAdmin;
import cn.lzh.vo.DepartmentType;
import cn.lzh.vo.Reader;
import cn.lzh.vo.ReaderType;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AuthorityPanel extends JPanel {
	private JPasswordField oldPwd;
	private JPasswordField newPwd;
	private JTextField tfName;
	private JPanel PermissionPanel;
	private JCheckBox cbAuReader;
	private JCheckBox cbAuBook;
	private JCheckBox cbAuBorrow;
	private JCheckBox cbAuSystem;
	private JPanel ChangePwdPanel;
	private JPanel CtrlPanel;
	private JButton Enter;
	private JButton Back;
	private JPanel SearchPanel;
	private JComboBox cbReaderType;
	private JButton btnQuery;
	private JComboBox cbDepartmentType;
	private JTable SearchResultTable;
	private JScrollPane SearchResultPanel;
	private ReaderAdmin readerBll = new ReaderAdmin();
	private ReaderTypeAdmin readerTypeBll = new ReaderTypeAdmin();
	private DepartmentTypeAdmin deptTypeBll = new DepartmentTypeAdmin();
	private int[] Selected = new int[4];
	public static enum OpStatus{
		inPwd,inAuthority,nul
	}
	private OpStatus ops;
	private JButton btAuthority;
	private JButton btUpdatePwd;
	
	public AuthorityPanel() {
		setSize(new Dimension(1200, 750));
		
		initPermissionPanel();
		initChangePwdPanel();
		initCtrlPanel();
		initSearchPanel();
		initSearchResultPanel();
		addButtonClickEventHandlers();
		setStatus(OpStatus.nul);
		System.out.println();
		
	}
	
	
	private void setStatus(OpStatus opst){
		ops = opst;
		if(!Login.reader.isSysAdmin()){
			btAuthority.setVisible(false);
		}
		switch(ops){
			case inPwd:
//				SearchPanel.setEnabled(false);
				SearchResultPanel.setEnabled(false);
//				PermissionPanel.setEnabled(false);
				ChangePwdPanel.setEnabled(true);
				CtrlPanel.setEnabled(true);
				setComponentStatusInPanel(CtrlPanel, true);
				setComponentStatusInPanel(SearchPanel, false);
				setComponentStatusInPanel(PermissionPanel, false);
				setComponentStatusInPanel(ChangePwdPanel, true);
				break;
			case inAuthority:
				if(Login.reader.isSysAdmin()){
					SearchPanel.setEnabled(true);
					SearchResultPanel.setEnabled(true);
					PermissionPanel.setEnabled(true);
					ChangePwdPanel.setEnabled(false);
					CtrlPanel.setEnabled(true);
					//setComponentStatusInPanel(CtrlPanel, true);
					setComponentStatusInPanel(ChangePwdPanel, false);
					setComponentStatusInPanel(SearchPanel, true);
					setComponentStatusInPanel(PermissionPanel, true);
					Enter.setEnabled(true);
				}
				break;
			case nul:
				setComponentStatusInPanel(SearchPanel, false);
				SearchResultPanel.setEnabled(false);
				setComponentStatusInPanel(PermissionPanel, false);
				setComponentStatusInPanel(ChangePwdPanel, false);
				Enter.setEnabled(false);
		}
	}
	
	private void addButtonClickEventHandlers(){
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReaderType rdType = (ReaderType) cbReaderType.getSelectedItem();
				DepartmentType deptType = (DepartmentType) cbDepartmentType.getSelectedItem();
				String userName = tfName.getText().trim();
				//System.out.println("a" + userName + "a");
				Reader[] hits = readerBll.retrieveReaders(rdType, deptType, userName);
				//更新查询结果列表
				updateResultTable(hits);
				//setStatus(OpStatus.inSelect);
			}
		});
		
		Enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ops == OpStatus.inPwd){
					String oldpassword = String.valueOf(oldPwd.getPassword());
					String newpassword = String.valueOf(newPwd.getPassword());
					if(Login.reader.getRdPwd().equals(oldpassword)){
						if(readerBll.updatePwd(Login.reader, newpassword) > 0){
							JOptionPane.showMessageDialog(null, "修改成功");
							oldPwd.setText("");
							newPwd.setText("");
						}else{
							JOptionPane.showMessageDialog(null, "修改失败");
						}
					}else{
						JOptionPane.showMessageDialog(null, "密码输入错误");
					}
				}else if(ops == OpStatus.inAuthority){
					int selectedRow = SearchResultTable.getSelectedRow();
					if(selectedRow<0){
						JOptionPane.showMessageDialog(null, "请选中一条数据！");
						return;
					}
					Reader rd = ((CustomizedTableModel<Reader>)SearchResultTable.getModel()).getObjectAt(selectedRow);
					if(readerBll.updateAdmin(rd, getSelectedStatus()) > 0){
						JOptionPane.showMessageDialog(null, "修改权限成功");
					}else{
						JOptionPane.showMessageDialog(null, "修改失败");
					}
					
				}
			}
		});
		
		btAuthority.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStatus(OpStatus.inAuthority);
			}
		});
		
		btUpdatePwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStatus(OpStatus.inPwd);
			}
		});
	}
	
	private int getSelectedStatus(){
		int i = 0;
		int AuNum = 0; //权限
		for(Component comp : PermissionPanel.getComponents()){
			if(((JCheckBox)comp).isSelected()){
				Selected[i] = (int) Math.pow(2, i);
				i++;
			}else{
				Selected[i] = 0;
				i++;
			}
		}
		for(int x : Selected){
			AuNum += x;
		}
		return AuNum;
	}
	
	private void updateResultTable(Reader[] readers){
		if(readers == null){
			JOptionPane.showMessageDialog(null, "没有找到符合要求的记录");
			return;
		}
		CustomizedTableModel<Reader> tableModel = (CustomizedTableModel<Reader>)SearchResultTable.getModel();
		tableModel.setRecords(readers);
		//更新表格
		tableModel.fireTableDataChanged();
	}
	
	private void setComponentStatusInPanel(JPanel panel,boolean status){

		for(Component comp : panel.getComponents()){
			comp.setEnabled(status);
		}
	}
	
	
	
	
	
	
	
	
	
	

	
	
	private void initSearchResultPanel(){
		CustomizedTableModel<Reader> tabelModel = new CustomizedTableModel<>
		(readerBll.getDisplayColumnNames(), readerBll.getMethodNames());
		
		SearchResultTable = new JTable(tabelModel);
		SearchResultTable.setBounds(15, 15, 1140, 223);
		
		SearchResultPanel = new JScrollPane(SearchResultTable);
		SearchResultPanel.setBounds(15, 102, 1170, 258);
		
		add(SearchResultPanel);
		SearchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	private void initSearchPanel(){
		SearchPanel = new JPanel();
		SearchPanel.setBounds(15, 15, 1200, 72);
		SearchPanel.setLayout(null);
		add(SearchPanel);
		
		JLabel label_2 = new JLabel("读者类别：");
		label_2.setBounds(15, 15, 97, 42);
		SearchPanel.add(label_2);
		
		cbReaderType = new JComboBox(readerTypeBll.getReaderTypes());
		cbReaderType.setBounds(105, 23, 112, 27);
		SearchPanel.add(cbReaderType);
		
		JLabel label_3 = new JLabel("姓名:");
		label_3.setBounds(501, 26, 81, 21);
		SearchPanel.add(label_3);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(566, 23, 120, 27);
		SearchPanel.add(tfName);
		
		btnQuery = new JButton("查找");
		
		btnQuery.setBounds(751, 22, 123, 29);
		SearchPanel.add(btnQuery);
		
		JLabel label_4 = new JLabel("单位名称");
		label_4.setBounds(250, 26, 81, 21);
		SearchPanel.add(label_4);
		
		cbDepartmentType = new JComboBox(deptTypeBll.getDepartmentTypes());
		cbDepartmentType.setBounds(330, 23, 119, 27);
		SearchPanel.add(cbDepartmentType);
	}
	private void initCtrlPanel(){
		CtrlPanel = new JPanel();
		CtrlPanel.setBounds(15, 651, 1169, 84);
		add(CtrlPanel);
		CtrlPanel.setLayout(null);
		
		Enter = new JButton("确定");
		
		Enter.setBounds(755, 28, 123, 29);
		CtrlPanel.add(Enter);
		
		Back = new JButton("返回");
		Back.setBounds(893, 28, 123, 29);
		CtrlPanel.add(Back);
		
		btAuthority = new JButton("权限管理");
		
		btAuthority.setBounds(82, 28, 123, 29);
		CtrlPanel.add(btAuthority);
		
		btUpdatePwd = new JButton("密码修改");
		
		btUpdatePwd.setBounds(247, 28, 123, 29);
		CtrlPanel.add(btUpdatePwd);
	}
	private void initChangePwdPanel(){
		ChangePwdPanel = new JPanel();
		ChangePwdPanel.setBounds(15, 511, 1169, 133);
		ChangePwdPanel.setBorder(new TitledBorder(null, "\u5BC6\u7801\u4FEE\u6539", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(ChangePwdPanel);
		ChangePwdPanel.setLayout(null);
		
		JLabel label = new JLabel("原始密码：");
		label.setBounds(88, 39, 101, 21);
		ChangePwdPanel.add(label);
		
		JLabel label_1 = new JLabel("新设密码：");
		label_1.setBounds(88, 97, 101, 21);
		ChangePwdPanel.add(label_1);
		
		oldPwd = new JPasswordField();
		oldPwd.setBounds(204, 36, 110, 27);
		ChangePwdPanel.add(oldPwd);
		
		newPwd = new JPasswordField();
		newPwd.setBounds(204, 94, 110, 27);
		ChangePwdPanel.add(newPwd);
	}
	private void initPermissionPanel(){
		setLayout(null);
		PermissionPanel = new JPanel();
		PermissionPanel.setBounds(15, 375, 1157, 133);
		PermissionPanel.setBorder(new TitledBorder(null, "\u6743\u9650\u7BA1\u7406", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(PermissionPanel);
		PermissionPanel.setLayout(null);
		
		cbAuReader = new JCheckBox("读者管理");
		cbAuReader.setBounds(181, 60, 149, 29);
		PermissionPanel.add(cbAuReader);
		
		cbAuBook = new JCheckBox("图书管理");
		cbAuBook.setBounds(364, 60, 129, 29);
		PermissionPanel.add(cbAuBook);
		
		cbAuBorrow = new JCheckBox("借阅管理");
		cbAuBorrow.setBounds(537, 60, 149, 29);
		PermissionPanel.add(cbAuBorrow);
		
		cbAuSystem = new JCheckBox("系统管理");
		cbAuSystem.setBounds(686, 60, 149, 29);
		PermissionPanel.add(cbAuSystem);
	}
}
