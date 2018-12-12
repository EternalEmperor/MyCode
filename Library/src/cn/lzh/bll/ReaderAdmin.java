package cn.lzh.bll;

import java.sql.SQLException;

import cn.lzh.dal.ReaderDAL;
import cn.lzh.vo.DepartmentType;
import cn.lzh.vo.Reader;
import cn.lzh.vo.ReaderType;

public class ReaderAdmin extends LibraryBLL{
	
	//private ReaderDAL dal = new ReaderDAL();
	
	public ReaderAdmin(){
		dal = new ReaderDAL();
	}
	
	public Reader getReader(int rdID){
		try{
			return (Reader)dal.getObjectByID(rdID);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Reader[] retrieveReaders(ReaderType rdType, DepartmentType deptType,
			String userName){
		try{
			return ((ReaderDAL)dal).getReadersBy(rdType, deptType, userName);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int addReader(Reader rd){
		try {
			return dal.add(rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateReader(Reader rd){
		try{
			return dal.update(rd);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteReader(Reader rd){
		try{
			return dal.delete(rd);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updatePwd(Reader rd,String pwd){
		try {
			return ((ReaderDAL)dal).updatePwd(rd, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateAdmin(Reader rd,int admin){
		try {
			return ((ReaderDAL)dal).updateAdmin(rd, admin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return 0;
	}
	
}
