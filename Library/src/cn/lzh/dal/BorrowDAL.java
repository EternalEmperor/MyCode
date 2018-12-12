package cn.lzh.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.lzh.db.SQLHelper;
import cn.lzh.vo.AbstractModel;
import cn.lzh.vo.Borrow;
import cn.lzh.vo.ReaderType;

public class BorrowDAL extends AbstractDAL {

	private String[] dispColNames = new String[]{
			"图书序号","图书名称","图书作者","续借次数","借阅日期","应还日期","超期天数","超期金额"	
		};
		private String[] methodNames = new String[]{
				"getBkID","getBkName","getBkAuthor","getContinueTimes","getDateOut",
				"getDateRetPlan","getOverDay","getPunishMoney"
				
		};
	
	@Override
	public AbstractModel[] getAllObjects() throws Exception {
		ArrayList<Borrow> objects = new ArrayList<Borrow>();
		ResultSet rs = SQLHelper.getResultSet("select * from tb_borrow");
		if(rs != null){
			while(rs.next()){
				Borrow bw = initBorrow(rs);
				objects.add(bw);
			}
			rs.close();
		}
		Borrow[] borrows = new Borrow[objects.size()];
		objects.toArray(borrows);
		return borrows;
	}

	@Override
	public int add(AbstractModel object) throws Exception {
		if(object instanceof Borrow == false){
			throw new Exception("Can only handle Borrow");
		}
		Borrow bw = (Borrow)object;
		String sql = "insert into tb_borrow ("
				+ "BorrowID,rdID,bkID,ContinueTimes,"
				+ "DateOut,DateRetPlan,DateRetAct,"
				+ "OverDay,PunishMoney,HasReturn) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[]{
			bw.getBorrowID(),
			bw.getRdID(),
			bw.getBkID(),
			bw.getContinueTimes(),
			bw.getDateOut(),
			bw.getDateRetPlan(),
			bw.getDateRetAct(),
			bw.getOverDay(),
			bw.getPunishMoney(),
			bw.isHasReturn()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int delete(AbstractModel object) throws Exception {
		if(object instanceof Borrow == false){
			throw new Exception("Can only handle Borrow");
		}
		Borrow bw = (Borrow)object;
		String sql = "delete from tb_borrow where BorrowID = ?";
		Object[] params = new Object[]{bw.getBorrowID()};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public int update(AbstractModel object) throws Exception {
		if(object instanceof Borrow == false){
			throw new Exception("Can only handle Borrow");
		}
		Borrow bw = (Borrow)object;
		String sql = "update tb_borrow set rdID = ?,bkID = ?,"
				+ "ContinueTimes = ?,DateOut = ?,DateRetPlan = ?,"
				+ "DateRetAct = ?,OverDay = ?,PunishMoney = ?,"
				+ "HasReturn = ? where BorrowID = ?";
		Object[] params = new Object[]{
			bw.getRdID(),
			bw.getBkID(),
			bw.getContinueTimes(),
			bw.getDateOut(),
			bw.getDateRetPlan(),
			bw.getDateRetAct(),
			bw.getOverDay(),
			bw.getPunishMoney(),
			bw.isHasReturn(),
			bw.getBorrowID()
		};
		return SQLHelper.ExecSql(sql,params);
	}

	@Override
	public AbstractModel getObjectByID(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Borrow[] getObjectByRdID(int rdid) throws SQLException{
		ArrayList<Borrow> objects = new ArrayList<Borrow>();
		ResultSet rs = SQLHelper.getResultSet("select * from tb_borrow where rdID = " + rdid);
		if(rs != null){
			while(rs.next()){
				Borrow bw = initBorrow(rs);
				objects.add(bw);
			}
			rs.close();
		}
		Borrow[] borrows = new Borrow[objects.size()];
		objects.toArray(borrows);
		return borrows;
	}
	
	private Borrow initBorrow(ResultSet rs) throws SQLException{
		Borrow bw = new Borrow();
		bw.setBorrowID(rs.getInt("BorrowID"));
		bw.setRdID(rs.getInt("rdID"));
		bw.setBkID(rs.getInt("bkID"));
		bw.setContinueTimes(rs.getInt("ContinueTimes"));
		bw.setDateOut(rs.getDate("DateOut"));
		bw.setDateRetPlan(rs.getDate("DateRetPlan"));
		bw.setDateRetAct(rs.getDate("DateRetAct"));
		bw.setOverDay(rs.getInt("OverDay"));
		bw.setPunishMoney(rs.getFloat("PunishMoney"));
		bw.setHasReturn(rs.getBoolean("HasReturn"));
		return bw;
	}
	
public  boolean isBorrowed(Borrow borrow) throws SQLException {
		
		ResultSet rs  = SQLHelper.getResultSet("select rdID,bkID from tb_Borrow");
		if(rs !=null){
			while(rs.next()){
				int rdID = rs.getInt("rdID");
				int bkID = rs.getInt("bkID");
				if((borrow.getRdID()==rdID)&&borrow.getBkID()==bkID){
					return true;
				}
			}
			rs.close();
	 }
		return false;
	}

	@Override
	public String[] getPrettyColumnNames() {
		return this.dispColNames;
	}

	@Override
	public String[] getMethodNames() {
		return this.methodNames;
	}


}
