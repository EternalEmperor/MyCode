package cn.lzh.bll;

import java.sql.SQLException;

import cn.lzh.dal.BorrowDAL;
import cn.lzh.vo.Borrow;

public class BorrowAdmin extends LibraryBLL {

	public BorrowAdmin(){
		dal = new BorrowDAL();
	}
	
	public Borrow[] getBorrowByRdID(int rdid){
		try {
			return ((BorrowDAL)dal).getObjectByRdID(rdid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isBorrowed(Borrow borrow){
		try {
			return ((BorrowDAL)dal).isBorrowed(borrow);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public int addBorrowRecord(Borrow borrow){
		try {
			return dal.add(borrow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteBorrowRecord(Borrow borrow){
		try {
			return dal.delete(borrow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateBorrowRecord(Borrow borrow){
		try {
			return dal.update(borrow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
