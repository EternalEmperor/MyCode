package cn.lzh.vo;

import java.sql.Date;

public class Reader extends AbstractModel{
	private int rdID; //读者ID
	private String rdName; //读者姓名
	private String rdSex; //读者性别
	private int rdType; //读者类别
	private String rdDept; //单位名称
	private String rdPhone;//电话号码
	private Date rdDateReg;//读者登记日期
	private String rdStatus;//证件状态，有效，挂失，注销
	private int rdBorrowQty; //已借书数量，默认为0
	private String rdPwd;//读者密码
	private int rdAdminRoles;//管理角色，0-读者、1-借书证管理、2-图书管理、4-借阅管理、8-系统管理，可组合
	
	public Reader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRdID() {
		return rdID;
	}

	public void setRdID(int rdID) {
		this.rdID = rdID;
	}

	public String getRdName() {
		return rdName;
	}

	public void setRdName(String rdName) {
		this.rdName = rdName;
	}

	public String getRdSex() {
		return rdSex;
	}

	public void setRdSex(String rdSex) {
		this.rdSex = rdSex;
	}

	public int getRdType() {
		return rdType;
	}

	public void setRdType(int rdType) {
		this.rdType = rdType;
	}

	public Date getRdDateReg() {
		return rdDateReg;
	}

	public void setRdDateReg(Date rdDateReg) {
		this.rdDateReg = rdDateReg;
	}

	public String getRdStatus() {
		return rdStatus;
	}

	public void setRdStatus(String rdStatus) {
		this.rdStatus = rdStatus;
	}

	public int getRdBorrowQty() {
		return rdBorrowQty;
	}

	public void setRdBorrowQty(int rdBorrowQty) {
		this.rdBorrowQty = rdBorrowQty;
	}

	public String getRdPwd() {
		return rdPwd;
	}

	public void setRdPwd(String rdPwd) {
		this.rdPwd = rdPwd;
	}
	
	

	public String getRdDept() {
		return rdDept;
	}

	public void setRdDept(String rdDept) {
		this.rdDept = rdDept;
	}

	public String getRdPhone() {
		return rdPhone;
	}

	public void setRdPhone(String rdPhone) {
		this.rdPhone = rdPhone;
	}

	public int getRdAdminRoles() {
		return rdAdminRoles;
	}

	public void setRdAdminRoles(int rdAdminRoles) {
		this.rdAdminRoles = rdAdminRoles;
	}
	
	public boolean isReaderAdmin(){
		return (this.rdAdminRoles & 1) > 0;
	}
	
	public boolean isBookAdmin(){
		return (this.rdAdminRoles & 2) > 0;
	}
	
	public boolean isBorrowAdmin(){
		return (this.rdAdminRoles & 4) > 0;
	}
	
	public boolean isSysAdmin(){
		return (this.rdAdminRoles & 8) > 0;
	}

	@Override
	public String toString() {
		return "Reader [rdID=" + rdID + ", rdName=" + rdName + ", rdSex=" + rdSex + ", rdType=" + rdType
				+ ", rdDateReg=" + rdDateReg + ", rdStatus=" + rdStatus + ", rdBorrowQty=" + rdBorrowQty + ", rdPwd="
				+ rdPwd + "]";
	}
	
	
	
	
}
