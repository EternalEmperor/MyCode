package cn.lzh.gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import cn.lzh.bll.ReaderTypeAdmin;
import cn.lzh.vo.ReaderType;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ReaderTypePanel extends JPanel {
	
	private JTextField tfRdTypeName;
	private JTextField tfRdType;
	private JTextField tfCanLendQty;
	private JTextField tfCanLendDay;
	private JTextField tfCanContinueTimes;
	private JTextField tfPunishRate;
	private JTextField tfDateValid;
	private JTable searchResultTable;
	private JPanel InfoPanel;
	private JScrollPane ResultPanel;
	private JPanel FunctionPanel;
	private JButton addType;
	private JButton UpdateType;
	private JButton DeleteType;
	private JButton Cancel;
	private JButton Enter;
	
	private ReaderType[] hits = null;
	private ReaderTypeAdmin readerTypeBll = new ReaderTypeAdmin();
	
	private enum OpStatus{
		inNew,inChange,inDetele,inSelect
	}
	private OpStatus ops;
	
	
	public ReaderTypePanel() {
		setSize(new Dimension(1200, 750));
		setLayout(null);	
		
		initInfoPanel();
		initResultPanel();
		initFunctionPanel();
		setStatus(ops.inSelect);
		addButtonClickEventHandlers();
			
	}
	
	
	
	private void setStatus(OpStatus opst){
		ops = opst;
		switch(ops){
			case inSelect:
				ResultPanel.setEnabled(true);
				tfRdType.setText("");
				tfRdTypeName.setText("");
				tfCanLendQty.setText("");
				tfCanLendDay.setText("");
				tfCanContinueTimes.setText("");
				tfPunishRate.setText("");
				tfDateValid.setText("");
				InfoPanel.setEnabled(false);
				FunctionPanel.setEnabled(true);
				setComponentStatusInPanel(InfoPanel, false);
				setComponentStatusInPanel(FunctionPanel, true);
				break;
			case inNew:
				InfoPanel.setEnabled(true);
				ResultPanel.setEnabled(false);
				FunctionPanel.setEnabled(true);
				setComponentStatusInPanel(InfoPanel, true);
				setComponentStatusInPanel(FunctionPanel, true);
				UpdateType.setEnabled(false);
				DeleteType.setEnabled(false);
				addType.setEnabled(false);
				break;
			case inChange:
				InfoPanel.setEnabled(true);
				ResultPanel.setEnabled(true);
				FunctionPanel.setEnabled(true);
				setComponentStatusInPanel(InfoPanel, true);
				setComponentStatusInPanel(FunctionPanel, true);
				tfRdType.setEnabled(false);
				UpdateType.setEnabled(false);
				addType.setEnabled(false);
				DeleteType.setEnabled(false);
				break;
		}
			
	}
	
	private void setComponentStatusInPanel(JPanel panel, boolean status) {
		for(Component comp: panel.getComponents()){
			comp.setEnabled(status);
		}
		
	}
	
	private void addButtonClickEventHandlers(){
		
		addType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStatus(ops.inNew);
			}
		});
		
		UpdateType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow < 0){
					JOptionPane.showMessageDialog(null, "请选中一条数据");
					return;
				}
				setReaderTypeToText(((CustomizedTableModel<ReaderType>) searchResultTable.getModel()).getObjectAt(selectedRow));
				setStatus(ops.inChange);
			}
		});
		
		DeleteType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = searchResultTable.getSelectedRow();
				if(selectedRow < 0){
					JOptionPane.showMessageDialog(null, "请选中一条数据");
					return;
				}
				if(readerTypeBll.deleteReaderType
						(((CustomizedTableModel<ReaderType>) searchResultTable.getModel()).getObjectAt(selectedRow)) > 0){
					JOptionPane.showMessageDialog(null, "删除成功");
					hits = readerTypeBll.getReaderTypes();
					updateResultTable(hits);
					setStatus(ops.inSelect);
				}else{
					JOptionPane.showMessageDialog(null, "删除失败");
				}
				
				
			}
		});
		
		Enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ops == OpStatus.inNew){
					if(!isjudgeTextHaveNull()){
						ReaderType readertype = getReaderTypeFromText();
						if(readerTypeBll.addReaderType(readertype) > 0){
							JOptionPane.showMessageDialog(null, "添加成功");
							hits = readerTypeBll.getReaderTypes();
							updateResultTable(hits);
							setStatus(ops.inSelect);
						}
					}else{
						JOptionPane.showMessageDialog(null, "存在文本框为输入");
					}
				}else if(ops == OpStatus.inChange){
					ReaderType readertype = getReaderTypeFromText();
					if(readerTypeBll.updateReaderType(readertype) > 0){
						JOptionPane.showMessageDialog(null, "修改成功");
						hits = readerTypeBll.getReaderTypes();
						updateResultTable(hits);
					}else{
						JOptionPane.showMessageDialog(null, "修改失败");
					}
					
					setStatus(ops.inSelect);
				}
			}
		});
		
		Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ops != OpStatus.inSelect){
					setStatus(ops.inSelect);
				}
			}
		});
		
		
	}
	
	private void updateResultTable(ReaderType[] readerTypes){
		if(readerTypes == null){
			return;
		}
		CustomizedTableModel<ReaderType> tableModel = (CustomizedTableModel<ReaderType>) searchResultTable.getModel();
		tableModel.setRecords(readerTypes);
		tableModel.fireTableDataChanged();
	}
	
	private ReaderType getReaderTypeFromText(){
		ReaderType readerType = new ReaderType();
		readerType.setRdType(Integer.valueOf(tfRdType.getText()));
		readerType.setRdTypeName(tfRdTypeName.getText());
		readerType.setCanLendQty(Integer.valueOf(tfCanLendQty.getText()));
		readerType.setCanLendDay(Integer.valueOf(tfCanLendDay.getText()));
		readerType.setCanContinueTimes(Integer.valueOf(tfCanContinueTimes.getText()));
		readerType.setPunishRate(Float.valueOf(tfPunishRate.getText()));
		readerType.setDateValid(Integer.valueOf(tfDateValid.getText()));
		
		return readerType;
		
	}
	
	public boolean isjudgeTextHaveNull(){
		if(tfRdType.getText().trim().equals("") || 
		   tfRdTypeName.getText().trim().equals("") || 
		   tfCanLendQty.getText().trim().equals("") || 
		   tfCanLendDay.getText().trim().equals("") ||
		   tfCanContinueTimes.getText().trim().equals("") ||
		   tfPunishRate.getText().trim().equals("") ||
		   tfDateValid.getText().trim().equals("")){
			return true;
		}
		return false;
	}
	
	private void setReaderTypeToText(ReaderType readerType){
		
		tfRdType.setText(String.valueOf(readerType.getRdType()));
		tfRdTypeName.setText(readerType.getRdTypeName());
		tfCanLendQty.setText(String.valueOf(readerType.getCanLendQty()));
		tfCanLendDay.setText(String.valueOf(readerType.getCanLendDay()));
		tfCanContinueTimes.setText(String.valueOf(readerType.getCanContinueTimes()));
		tfPunishRate.setText(String.valueOf(readerType.getPunishRate()));
		tfDateValid.setText(String.valueOf(readerType.getDateValid()));
	}
	
	
	
	
	
	
	
	/**
	 * 面板初始化
	 */
	private void initInfoPanel(){
		InfoPanel = new JPanel();
		InfoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u8BFB\u8005\u7C7B\u578B", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		InfoPanel.setBounds(15, 15, 1149, 225);
		add(InfoPanel);
		InfoPanel.setLayout(null);
		
		JLabel label = new JLabel("类型名称：");
		label.setBounds(313, 48, 105, 21);
		InfoPanel.add(label);
		
		JLabel label_1 = new JLabel("可续借次数：");
		label_1.setBounds(46, 128, 117, 21);
		InfoPanel.add(label_1);
		
		JLabel label_2 = new JLabel("可借数量：");
		label_2.setBounds(618, 48, 105, 21);
		InfoPanel.add(label_2);
		
		JLabel label_3 = new JLabel("可借天数：");
		label_3.setBounds(880, 48, 105, 21);
		InfoPanel.add(label_3);
		
		JLabel label_4 = new JLabel("罚金率(元/天)：");
		label_4.setBounds(313, 128, 135, 21);
		InfoPanel.add(label_4);
		
		JLabel label_5 = new JLabel("证件有效期：");
		label_5.setBounds(644, 128, 128, 21);
		InfoPanel.add(label_5);
		
		tfRdTypeName = new JTextField();
		tfRdTypeName.setBounds(412, 45, 117, 27);
		InfoPanel.add(tfRdTypeName);
		tfRdTypeName.setColumns(10);
		
		JLabel lblid = new JLabel("类型ID：");
		lblid.setBounds(46, 45, 81, 21);
		InfoPanel.add(lblid);
		
		tfRdType = new JTextField();
		tfRdType.setBounds(124, 45, 117, 27);
		tfRdType.setDocument(new NumberJudge());
		InfoPanel.add(tfRdType);
		tfRdType.setColumns(10);
		
		tfCanLendQty = new JTextField();
		tfCanLendQty.setBounds(722, 45, 105, 27);
		tfCanLendQty.setDocument(new NumberJudge());
		InfoPanel.add(tfCanLendQty);
		tfCanLendQty.setColumns(10);
		
		tfCanLendDay = new JTextField();
		tfCanLendDay.setBounds(974, 45, 110, 27);
		tfCanLendDay.setDocument(new NumberJudge());
		InfoPanel.add(tfCanLendDay);
		tfCanLendDay.setColumns(10);
		
		tfCanContinueTimes = new JTextField();
		tfCanContinueTimes.setBounds(145, 125, 105, 27);
		tfCanContinueTimes.setDocument(new NumberJudge());
		InfoPanel.add(tfCanContinueTimes);
		tfCanContinueTimes.setColumns(10);
		
		tfPunishRate = new JTextField();
		tfPunishRate.setBounds(448, 125, 118, 27);
		tfPunishRate.setDocument(new NumberJudge());
		InfoPanel.add(tfPunishRate);
		tfPunishRate.setColumns(10);
		
		tfDateValid = new JTextField();
		tfDateValid.setBounds(753, 125, 96, 27);
		tfDateValid.setDocument(new NumberJudge());
		InfoPanel.add(tfDateValid);
		tfDateValid.setColumns(10);
	}
	private void initResultPanel(){
		CustomizedTableModel<ReaderType> tableModel = new CustomizedTableModel<ReaderType>(
				readerTypeBll.getDisplayColumnNames(), readerTypeBll.getMethodNames());	
		searchResultTable = new JTable(tableModel);
		searchResultTable.setBounds(15, 26, 1119, 310);
		
		ResultPanel = new JScrollPane(searchResultTable);
		ResultPanel.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ResultPanel.setBounds(15, 261, 1149, 351);
		add(ResultPanel);
		//ResultPanel.setLayout(null);
		hits = readerTypeBll.getReaderTypes();
		updateResultTable(hits);
		
		//设置一次只能选择单条记录
		searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	private void initFunctionPanel(){
		FunctionPanel = new JPanel();
		FunctionPanel.setBounds(15, 615, 1149, 96);
		add(FunctionPanel);
		FunctionPanel.setLayout(null);
		
		addType = new JButton("添加");
		
		addType.setBounds(86, 15, 123, 29);
		FunctionPanel.add(addType);
		
		UpdateType = new JButton("修改");
		
		UpdateType.setBounds(264, 15, 123, 29);
		FunctionPanel.add(UpdateType);
		
		DeleteType = new JButton("删除");
		
		DeleteType.setBounds(424, 15, 123, 29);
		FunctionPanel.add(DeleteType);
		
		Cancel = new JButton("取消");
		
		Cancel.setBounds(965, 15, 123, 29);
		FunctionPanel.add(Cancel);
		
		Enter = new JButton("确定");
		
		Enter.setBounds(827, 15, 123, 29);
		FunctionPanel.add(Enter);
	}
}
