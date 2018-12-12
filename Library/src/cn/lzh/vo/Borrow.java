package cn.lzh.vo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.lzh.db.SQLHelper;

public class Borrow extends AbstractModel{
	private int borrowID; //借书顺序号
	private int rdID;	 //读者序号
	private int bkID; 	 //图书序号
	private int continueTimes; //续借次数（第一次借时，记为0）
	private Date dateOut; //借书日期
	private Date dateRetPlan; //应还日期
	private Date dateRetAct; //实际还书日期
	private int overDay; //超期天数
	private float punishMoney; //罚款金额
	private boolean hasReturn = false;
	
	public Borrow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBorrowID() {
		return borrowID;
	}

	public void setBorrowID(int borrowID) {
		this.borrowID = borrowID;
	}

	public int getRdID() {
		return rdID;
	}

	public void setRdID(int rdID) {
		this.rdID = rdID;
	}

	public int getBkID() {
		return bkID;
	}

	public void setBkID(int bkID) {
		this.bkID = bkID;
	}

	public int getContinueTimes() {
		return continueTimes;
	}

	public void setContinueTimes(int continueTimes) {
		this.continueTimes = continueTimes;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public Date getDateRetPlan() {
		return dateRetPlan;
	}

	public void setDateRetPlan(Date dateRetPlan) {
		this.dateRetPlan = dateRetPlan;
	}

	public Date getDateRetAct() {
		return dateRetAct;
	}

	public void setDateRetAct(Date dateRetAct) {
		this.dateRetAct = dateRetAct;
	}

	public int getOverDay() {
		return overDay;
	}

	public void setOverDay(int overDay) {
		this.overDay = overDay;
	}

	public float getPunishMoney() {
		return punishMoney;
	}

	public void setPunishMoney(float punishMoney) {
		this.punishMoney = punishMoney;
	}

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}
	
	public String getBkName() throws SQLException {
		String bkName = null;
		if(bkID!=0){
			String sql = "select bkName from TB_Book where bkID = "+bkID;
			ResultSet rs = SQLHelper.getResultSet(sql);
			if(rs!=null){
				if(rs.next()){
					bkName = rs.getString("bkName");
				}
				rs.close();
			}
		}
		return bkName;
	}

	public String getBkAuthor() throws SQLException {
		String bkAuthor = null;
		if(bkID!=0){
			String sql = "select bkAuthor from TB_Book where bkID = "+bkID;
			ResultSet rs = SQLHelper.getResultSet(sql);
			if(rs!=null){
				if(rs.next()){
					bkAuthor = rs.getString("bkAuthor");
				}
				rs.close();
			}
		}
		return bkAuthor;
	}


	@Override
	public String toString() {
		return "Borrow [borrowID=" + borrowID + ", rdID=" + rdID + ", bkID=" + bkID + ", continueTimes=" + continueTimes
				+ ", dateOut=" + dateOut + ", dateRetPlan=" + dateRetPlan + ", dateRetAct=" + dateRetAct + ", overDay="
				+ overDay + ", punishMoney=" + punishMoney + ", hasReturn=" + hasReturn + "]";
	}
	
	

	
	
	
	
	
	
	
}
